package org.cetide.hibiscus.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局 CORS（跨域资源共享）配置类
 * 用于解决前端跨域请求问题，适用于 Spring Boot 后端接口。
 *
 * @author heathcetide
 */
@Configuration
public class GlobalCorsConfig {

    /**
     * 创建一个全局的 CORS 过滤器 Bean
     * Spring Boot 会自动将此过滤器注册到容器中，使其在每个请求前进行跨域处理。
     *
     * @return CorsFilter 跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();

        // 设置允许访问的前端域名（支持通配符，如 http://localhost:3000）
        // 这里允许所有域名访问（不推荐用于生产）
        config.addAllowedOriginPattern("*");

        // 是否允许发送 Cookie（如 session、token 等）
        config.setAllowCredentials(true);

        // 设置允许的请求头（如 Authorization、自定义 Token 头等）
        config.addAllowedHeader("*"); // 表示允许所有请求头

        // 设置允许的 HTTP 请求方法
        config.addAllowedMethod("*"); // 表示允许所有 HTTP 方法：GET、POST、PUT、DELETE 等

        // 创建 URL 映射路径对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 将上述 CORS 配置应用到所有请求路径（/**）
        source.registerCorsConfiguration("/**", config);

        // 创建并返回 CorsFilter 实例，应用全局跨域配置
        return new CorsFilter(source);
    }
}
