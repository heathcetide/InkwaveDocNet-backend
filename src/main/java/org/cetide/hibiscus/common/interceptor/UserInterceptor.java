package org.cetide.hibiscus.common.interceptor;

import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.context.RequestContext;
import org.cetide.hibiscus.common.exception.AuthorizationException;
import org.cetide.hibiscus.common.security.JwtUtils;
import org.cetide.hibiscus.domain.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.cetide.hibiscus.common.constants.UserConstants.CURRENT_USERNAME;

/**
 * 普通用户拦截器
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    private final UserService userService;

    public UserInterceptor(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token == null) {
                throw new AuthorizationException("未登录或token已过期");
            }
            // 验证token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (!jwtUtils.validateToken(token)) {
                throw new AuthorizationException("token无效");
            }
            AuthContext.setCurrentUser(userService.getUserByEmail(jwtUtils.getUserFromToken(token).getEmail()));
            AuthContext.setCurrentToken(token);
            RequestContext.put(CURRENT_USERNAME, AuthContext.getCurrentUser().getUsername());
            return true;
        } catch (AuthorizationException e) {
            // 返回401未授权错误
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"" + e.getMessage() + "\",\"data\":null}");
            return false;
        } catch (Exception e) {
            // 返回500服务器内部错误
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":500,\"message\":\"" + e.getMessage() + "\",\"data\":null}");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContext.clear();
    }
} 