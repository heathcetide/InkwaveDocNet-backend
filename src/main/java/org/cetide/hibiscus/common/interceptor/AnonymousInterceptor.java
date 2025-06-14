package org.cetide.hibiscus.common.interceptor;


import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.exception.AuthorizationException;
import org.cetide.hibiscus.common.security.JwtUtils;
import org.cetide.hibiscus.domain.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 匿名访客访问拦截器
 */
@Component
public class AnonymousInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    private final UserService userService;

    public AnonymousInterceptor(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取 token
        String token = request.getHeader("Authorization");
        if (token != null) {
            // 验证token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (!jwtUtils.validateToken(token)) {
                throw new AuthorizationException("token无效");
            }
            AuthContext.setCurrentUser(userService.getById(jwtUtils.getUserFromToken(token).getId()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContext.clear();
    }
}