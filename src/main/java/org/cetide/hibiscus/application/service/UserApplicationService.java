package org.cetide.hibiscus.application.service;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hibiscus.signal.spring.anno.SignalEmitter;
import com.hibiscus.signal.spring.configuration.SignalContextCollector;
import org.apache.commons.lang3.SerializationUtils;
import org.cetide.hibiscus.application.command.SendVerificationCommand;
import org.cetide.hibiscus.application.dto.UserDTO;
import org.cetide.hibiscus.common.anno.Loggable;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.exception.BusinessException;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.security.JwtUtils;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.model.enums.LogType;
import org.cetide.hibiscus.domain.repository.UserRepository;
import org.cetide.hibiscus.domain.service.EmailService;
import org.cetide.hibiscus.domain.service.UserService;
import org.cetide.hibiscus.infrastructure.cache.RedisUtils;
import org.cetide.hibiscus.infrastructure.persistence.converter.UserConverter;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.UserMapper;
import org.cetide.hibiscus.infrastructure.storage.FileStorageAdapter;
import org.cetide.hibiscus.infrastructure.utils.SensitiveDataUtils;
import org.cetide.hibiscus.interfaces.rest.dto.UpdatePreferencesRequest;
import org.cetide.hibiscus.interfaces.rest.dto.UserRegisterEmailRequest;
import org.cetide.hibiscus.interfaces.rest.dto.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.cetide.hibiscus.common.constants.CommonEventConstants.EVENT_INTER_MEDIATE_REQUEST;
import static org.cetide.hibiscus.common.constants.UserConstants.*;
import static org.cetide.hibiscus.common.constants.UserEventConstants.EVENT_INTER_MEDIATE_USER;
import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.RATE_LIMIT_EXCEEDED;
import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.SYSTEM_ERROR;

@Service
public class UserApplicationService {

    private final FileStorageAdapter fileStorageAdapter;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserService userService;

    private final EmailService emailService;

    private final RedisUtils redisUtils;

    private final JwtUtils jwtUtils;

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static final Logger log = LoggerFactory.getLogger(UserApplicationService.class);

    public UserApplicationService(FileStorageAdapter fileStorageAdapter, UserRepository userRepository, UserMapper userMapper, UserService userService, EmailService emailService, RedisUtils redisUtils, JwtUtils jwtUtils) {
        this.fileStorageAdapter = fileStorageAdapter;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.emailService = emailService;
        this.redisUtils = redisUtils;
        this.jwtUtils = jwtUtils;
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










    @Loggable(type = LogType.THIRD_PARTY, value = "发送验证码")
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

    @Loggable(type = LogType.USER_REGISTER, value = "用户注册")
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
            redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
            return new UserConverter().toUserVO(user);
        } catch (Exception e) {
            throw new BusinessException(SYSTEM_ERROR.code(), "操作失败, 请联系站长" + e.getMessage());
        }
    }

    @SignalEmitter("user.login")
    @Loggable(type = LogType.USER_LOGIN, value = "用户登录")
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
        String token = jwtUtils.generateToken(user);
        // 缓存token和用户信息
        redisUtils.set(TOKEN_CACHE_KEY + user.getId(), token, TOKEN_CACHE_TIME);
        redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
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

    /**
     * 更新用户信息
     */
    @Transactional
    @Loggable(type = LogType.USER_UPDATE, value = "用户修改信息")
    public String updateUserInfo(User user) {
        if (user == null) {
            throw new IllegalArgumentException("error.username.empty");
        }

        UserEntity existingUser = AuthContext.getCurrentUser();
        // 验证当前用户是否有权限修改
        UserEntity currentUser = userService.getUserByEmail(existingUser.getEmail());  // 使用 email 查询
        if (!Objects.equals(currentUser.getId(), existingUser.getId())) {
            throw new IllegalArgumentException("error.user.permission.denied");
        }
        UserEntity bean = BeanUtil.toBean(user, UserEntity.class);

        // 确保 ID 不被修改
        bean.setId(existingUser.getId());
        // 防止敏感字段被修改
        bean.setPassword(existingUser.getPassword());

        // 使用 email 查询用户信息
        User updatedUser = userMapper.selectByEmail(user.getEmail());  // 使用 email 查询
        if (updatedUser == null) {
            throw new BusinessException(SYSTEM_ERROR.code(), "用户不存在");
        }

        // 删除旧缓存
        redisUtils.delete(USER_CACHE_KEY + updatedUser.getEmail());

        // 延时删除缓存
        scheduler.schedule(() -> {
            redisUtils.delete(USER_CACHE_KEY + updatedUser.getEmail());
        }, 500, TimeUnit.MILLISECONDS);

        // 更新数据库
        int updateCount = userMapper.updateById(bean);  // 执行数据库更新
        if (updateCount == 0) {
            throw new BusinessException(SYSTEM_ERROR.code(), "更新失败");
        }

        // 清除缓存并重新添加
        redisUtils.delete(USER_CACHE_KEY + user.getEmail());
        redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
        String token = jwtUtils.generateToken(user);
        // 缓存token和用户信息
        redisUtils.set(TOKEN_CACHE_KEY + user.getId(), token, TOKEN_CACHE_TIME);
        redisUtils.set(USER_CACHE_KEY + user.getEmail(), user.getEmail(), USER_CACHE_TIME);
        return token;
    }

    /**
     * 根据用户名获取用户信息
     */
    public User getUserByUsername(String username) {
        // 参数校验
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        // 构建缓存键
        String cacheKey = USER_CACHE_KEY + username;

        // 从缓存获取数据，避免缓存穿透
        User cached = userMapper.selectByUsername((String) redisUtils.get(cacheKey));
        if (cached != null) {
            log.info("Cache hit for username: {}", username);
            // 深拷贝防止缓存对象被误修改
            return SerializationUtils.clone(cached);
        }
        synchronized (("CACHE_LOCK_" + username).intern()) {
            // 双重检查，确保其他线程未更新缓存
            cached = userMapper.selectByUsername((String) redisUtils.get(cacheKey));
            if (cached != null) {
                log.info("Cache hit (after lock) for username: {}", username);
                return SerializationUtils.clone(cached);
            }

            // 从数据库查询
            log.info("Cache miss for username: {}. Querying database...", username);
            User user = userMapper.selectByUsername(username);

            // 如果用户不存在，抛出异常
            if (user == null) {
                log.warn("User not found for username: {}. Adding placeholder to cache.", username);
                redisUtils.set(cacheKey, "null", 60); // 缓存空值 60 秒
                throw new BusinessException(SYSTEM_ERROR.code(), "用户不存在");
            }

            // 写入缓存，设置超时时间，防止缓存雪崩
            // 写入缓存时，增加随机过期时间
            long randomExpiry = USER_CACHE_TIME + new Random().nextInt(300);
            redisUtils.set(cacheKey, username, randomExpiry);
            log.info("User email: {}", SensitiveDataUtils.maskEmail(user.getEmail()));

            return user;
        }
    }

    /**
     * 用户退出登录
     */
    @Transactional
    @Loggable(type = LogType.USER_LOGOUT, value = "用户退出登录")
    public void logoutUser(String token) {
        redisUtils.delete(TOKEN_CACHE_KEY + jwtUtils.getUserFromToken(token).getId());
    }

    /**
     * 用户申请注销账号
     */
    @Transactional
    @Loggable(type = LogType.USER_DELETE, value = "注销用户")
    public void requestAccountDeletion() {
        UserEntity user = userMapper.selectById(AuthContext.getCurrentUser().getId());
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        user.setDeleted(true);
        userMapper.updateById(user);
    }

    /**
     * 重置用户密码
     */
    @Loggable(type = LogType.USER_UPDATE, value = "重置密码")
    public void resetPassword(String emailOrPhone, String newPassword) {
        userService.resetPassword(emailOrPhone, newPassword);
    }


    @Loggable(type = LogType.USER_UPDATE, value = "上传头像")
    public String uploadAvatar(MultipartFile file) {
        String upload = fileStorageAdapter.upload(file);
        UserEntity user = userMapper.selectById(AuthContext.getCurrentUser().getId());
        user.setAvatarUrl(upload);
        userMapper.updateById(user);
        // 删除旧缓存
        redisUtils.delete(USER_CACHE_KEY + user.getEmail());

        // 延时删除缓存
        scheduler.schedule(() -> {
            redisUtils.delete(USER_CACHE_KEY + user.getEmail());
        }, 500, TimeUnit.MILLISECONDS);
        return upload;
    }

    @Loggable(type = LogType.USER_UPDATE, value = "用户更新偏好")
    public void updatePreferences(UpdatePreferencesRequest command) {
        UserEntity currentUser = AuthContext.getCurrentUser();
        if (command.themeDark() != null) {
            currentUser.setThemeDark(command.themeDark());
        }
        if (command.emailNotifications() != null) {
            currentUser.setEmailNotifications(command.emailNotifications());
        }
        userMapper.updateById(currentUser);
    }
}