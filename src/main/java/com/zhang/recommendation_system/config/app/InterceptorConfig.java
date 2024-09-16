package com.zhang.recommendation_system.config.app;

import com.zhang.recommendation_system.config.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: fxy
 * @Date: 2021/9/12 update
 * @Descripe: 拦截请求
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TokenArgumentResolver tokenArgumentResolver;

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/data/**")
        .excludePathPatterns("/downloadFile/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解决定是否需要登录
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/data/**").addResourceLocations("classpath:/data/");
        registry.addResourceHandler("/downloadFile/**").addResourceLocations("file:/uploads/");
//        registry.addResourceHandler("/downloadFile/**").addResourceLocations("file:G:/uploads/");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

}
