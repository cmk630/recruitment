package com.cmk630.recruitment.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cmk630.recruitment.filter.CachingFilter;
import com.cmk630.recruitment.interceptor.LogInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LogInterceptor logInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/css/**", "/*.ico", "/error", "/h2-console/**");
	}

	// 이 Bean을 추가하여 필터를 등록하고 순서를 지정합니다.
	@Bean
	public FilterRegistrationBean<CachingFilter> cachingFilter() {
		FilterRegistrationBean<CachingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CachingFilter()); // 우리가 만든 필터를 등록
		registrationBean.addUrlPatterns("/*"); // 모든 요청에 대해 필터를 적용
		registrationBean.setOrder(1); // 필터 체인에서 가장 먼저 실행되도록 순서를 1로 지정
		return registrationBean;
	}
}
