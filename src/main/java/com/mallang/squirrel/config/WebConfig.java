package com.mallang.squirrel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mallang.squirrel.domain.auth.token.UserTokenService;
import com.mallang.squirrel.interceptor.ApiInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	private final SquirrelConfig squirrelConfig;
	private final UserTokenService userTokenService;

	@Value("${origin}")
	private String origin;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiInterceptor(squirrelConfig, userTokenService))
			// all
			.addPathPatterns("/api/**")
			// swagger
			.excludePathPatterns("/v3/api-docs")
			.excludePathPatterns("/swagger-resources/**")
			.excludePathPatterns("/swagger-ui/**")
			.excludePathPatterns("/webjars/**")
			// OAuth
			.excludePathPatterns("/api/oauth2/callback/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(origin);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/img/**",
				"/css/**",
				"/js/**")
			.addResourceLocations("classpath:/static/img/",
				"classpath:/static/css/",
				"classpath:/static/js/");
	}

}
