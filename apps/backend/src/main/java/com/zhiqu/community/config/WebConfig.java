package com.zhiqu.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/health",
                        "/api/categories",
                        "/api/categories/**",
                        "/api/tags",
                        "/api/tags/**",
                        "/api/questions",
                        "/api/questions/**",
                        "/api/answers/**",
                        "/api/comments/**",
                        "/api/hotlist",
                        "/api/hotlist/**",
                        "/api/sensitive-words/**",
                        "/api/achievements/leaderboard"
                );
    }
}
