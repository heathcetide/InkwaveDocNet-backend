package org.cetide.hibiscus.application.service;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hibiscus.signal.spring.configuration.SignalContextCollector;
import org.cetide.hibiscus.application.command.CreateUserCommand;
import org.cetide.hibiscus.application.command.DeleteUserCommand;
import org.cetide.hibiscus.application.command.SendVerificationCommand;
import org.cetide.hibiscus.application.command.UpdateUserCommand;
import org.cetide.hibiscus.application.dto.UserDTO;
import org.cetide.hibiscus.common.exception.BusinessException;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.security.JwtUtils;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.repository.UserRepository;
import org.cetide.hibiscus.domain.service.EmailService;
import org.cetide.hibiscus.infrastructure.cache.RedisUtils;
import org.cetide.hibiscus.infrastructure.persistence.converter.UserConverter;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.UserMapper;
import org.cetide.hibiscus.interfaces.rest.dto.UserRegisterEmailRequest;
import org.cetide.hibiscus.interfaces.rest.dto.UserVO;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.cetide.hibiscus.common.constants.CommonEventConstants.EVENT_INTER_MEDIATE_REQUEST;
import static org.cetide.hibiscus.common.constants.UserConstants.*;
import static org.cetide.hibiscus.common.constants.UserEventConstants.EVENT_INTER_MEDIATE_USER;
import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.RATE_LIMIT_EXCEEDED;
import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.SYSTEM_ERROR;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final EmailService emailService;

    private final RedisUtils redisUtils;

    private final JwtUtils jwtUtils;

    public UserApplicationService(UserRepository userRepository, UserMapper userMapper, EmailService emailService, RedisUtils redisUtils, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.redisUtils = redisUtils;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public void createUser(CreateUserCommand command) {
        User user = new User(
                null,
                command.getUsername(),
                command.getEmail(),
                command.getPassword(),
                command.getAvatarUrl(),
                "ACTIVE",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UpdateUserCommand command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        user.update(command.getUsername(), command.getEmail(), command.getAvatarUrl(), command.getStatus());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(DeleteUserCommand command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        user.markDeleted();
        userRepository.save(user);
    }


    public IPage<UserDTO> getUserPage(PageRequest request) {
        Page<UserEntity> page = new Page<>(request.getPage(), request.getSize());
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.like("username", request.getKeyword());
        }

        return userMapper.selectPage(page, wrapper)
                .convert(UserConverter::toDTO); // 返回 Page<UserDTO> 也是 IPage<UserDTO>
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserConverter::toEntity)      // 从聚合转为实体
                .map(UserConverter::toDTO)         // 再转为 DTO
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    public Boolean sendVerificationCode(SendVerificationCommand command) {
        String redisKeyCode = generateRedisKey(command.email(), SEND_EMAIL_CODE);
        String redisKeySendTime = generateRedisKey(command.email(), SEND_EMAIL_CODE_SEND_TIME);

        // 1.检查是否在发送间隔内（例如 1 分钟）
        Boolean isSent = redisUtils.setIfAbsent(redisKeySendTime, "1", SEND_INTERVAL, TimeUnit.MINUTES);
        if (Boolean.FALSE.equals(isSent)) {
            throw new BusinessException(RATE_LIMIT_EXCEEDED.code(), "验证码发送过于频繁，请稍后再试");
        }

        // 2️.生成验证码
        String code = generateRandomCode();
        try {
            // 3.1存储验证码（10 分钟有效期）
            redisUtils.set(redisKeyCode, code, SEND_EMAIL_CODE_TIME, TimeUnit.MINUTES);
            // 3.2设置发送时间间隔（1 分钟有效期）
            redisUtils.set(redisKeySendTime, "1", SEND_INTERVAL, TimeUnit.MINUTES);
            // 4.发送邮件（带重试机制）
            emailService.sendEmailWithRetry(command.email(), code, 3, 1000);
            return true;
        } catch (RedisConnectionFailureException redisEx) {
            throw new RuntimeException("系统错误，请稍后重试");
        } catch (Exception e) {
            // 删除 Redis 中的数据，防止冗余
            redisUtils.delete(redisKeyCode);
            redisUtils.delete(redisKeySendTime);
            return false;
        }
    }

    public UserVO registerByEmail(UserRegisterEmailRequest userRegisterEmailRequest, HttpServletRequest request) {
        String email = userRegisterEmailRequest.email();
        String redisKeyCode = generateRedisKey(email, SEND_EMAIL_CODE);
        if (Boolean.FALSE.equals(redisUtils.exists(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "验证码过期, 请重新发送验证码");
        }
        if (!userRegisterEmailRequest.code().equals(redisUtils.get(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "验证码错误, 请重新发送验证码");
        }
        LambdaQueryWrapper<UserEntity> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(UserEntity::getEmail, email);
        UserEntity userByEmail = userMapper.selectOne(userLambdaQueryWrapper);
        if (null != userByEmail) {
            throw new BusinessException(SYSTEM_ERROR.code(), "邮箱已被注册");
        }
        UserEntity user = BuildNewUser(userRegisterEmailRequest.email());
        try {
            if (userMapper.insert(user) != 1) {
                throw new BusinessException(SYSTEM_ERROR.code(), "注册失败, 请联系站长");
            }
            SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, user);
            SignalContextCollector.collect(EVENT_INTER_MEDIATE_REQUEST, request);
            redisUtils.set(USER_CACHE_KEY + user.getId(), user.getEmail(), USER_CACHE_TIME);
            return new UserConverter().toUserVO(user);
        } catch (Exception e) {
            throw new BusinessException(SYSTEM_ERROR.code(), "操作失败, 请联系站长" + e.getMessage());
        }
    }

    public String loginByEmail(UserRegisterEmailRequest userRegisterEmailRequest, HttpServletRequest request) {
        String email = userRegisterEmailRequest.email();
        if (!isValidEmail(email)) {
            throw new BusinessException(SYSTEM_ERROR.code(), "邮箱格式不正确");
        }
        if (!userMapper.existsByEmail(email)) {
            throw new BusinessException(SYSTEM_ERROR.code(), "邮箱未注册");
        }
        String redisKeyCode = generateRedisKey(email, SEND_EMAIL_CODE);
        if (Boolean.FALSE.equals(redisUtils.exists(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "操作失败，请重新发送验证码");
        }
        if (!userRegisterEmailRequest.code().equals(redisUtils.get(redisKeyCode))) {
            throw new BusinessException(SYSTEM_ERROR.code(), "验证码错误");
        }
        User user = userMapper.selectByEmail(userRegisterEmailRequest.email());
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, user);
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_REQUEST, request);
        String token = jwtUtils.generateToken(String.valueOf(user.getId()));
        // 缓存token和用户信息
        redisUtils.set(TOKEN_CACHE_KEY + user.getId(), token, TOKEN_CACHE_TIME);
        redisUtils.set(USER_CACHE_KEY + user.getId(), user.getEmail(), USER_CACHE_TIME);
        return token;
    }


    /**
     * 构建md5加密的redis-key
     */
    private String generateRedisKey(String email, String prefix) {
        String hashedEmail = DigestUtils.md5DigestAsHex(email.getBytes(StandardCharsets.UTF_8)); // 使用 Spring 提供的 MD5 工具
        return prefix + hashedEmail;
    }

    /**
     * 生成安全的六位验证码
     */
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int length = 6; // 验证码长度
        StringBuilder sb = new StringBuilder(length);
        String chars = "0123456789"; // 可扩展为字母和数字组合，如 "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    /**
     * 验证邮箱格式是否正确
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return email != null && email.matches(emailRegex);
    }
}