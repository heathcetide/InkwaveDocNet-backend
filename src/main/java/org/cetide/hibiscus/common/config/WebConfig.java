package org.cetide.hibiscus.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.cetide.hibiscus.common.interceptor.AnonymousInterceptor;
import org.cetide.hibiscus.common.interceptor.UserInterceptor;
import org.cetide.hibiscus.infrastructure.web.RequestIdFilter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.TimeZone;

/**
 * Web配置
 *
 * @author heathcetide
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    private final AnonymousInterceptor anonymousInterceptor;

    private final UserInterceptor userInterceptor;

    public WebConfig(AnonymousInterceptor anonymousInterceptor, UserInterceptor userInterceptor) {
        this.anonymousInterceptor = anonymousInterceptor;
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/api/users/delete-account","/api/users/info","/api/users/update","/api/users/logout","/api/users/upload-avatar","/api/users/delete-account","/api/users/preferences")
                .addPathPatterns("/api/organization/**")
                .addPathPatterns("/api/notification/**")
                .addPathPatterns("/api/org_invite/**").excludePathPatterns("/api/org_invite/link/**")
                .addPathPatterns("/api/organization_member/my-organizations")
                .addPathPatterns("/api/knowledge_base/**")
                .addPathPatterns("/api/document/**")
                .addPathPatterns("/api/document_template/create");
    }

    /**
     * swagger文档配置
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("墨协 Backend API 文档")
                .description("墨协 Backend")
                .version("1.0.0")
                .termsOfServiceUrl("https://hibiscus.fit")
                .contact(new Contact("Hibiscus 开发团队", "https://hibiscus.fit", "19511899044@163.com"))
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                //指定生成接口需要扫描的包
                .apis(RequestHandlerSelectors.basePackage("org.cetide.hibiscus.interfaces.rest.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public FilterRegistrationBean<RequestIdFilter> requestIdFilter() {
        FilterRegistrationBean<RequestIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestIdFilter());
        registration.setOrder(1);
        return registration;
    }

    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module()); // 如果你有 Optional 等
    }

    /**
     * 资源处理
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
    }
}
