package com.cmk630.recruitment.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Swagger (OpenAPI) 설정을 위한 클래스
 */
@OpenAPIDefinition(
	info = @Info(
		title = "채용 TEST 서비스 API 명세서",
		description = "채용 TEST 서비스 API 문서입니다.",
		version = "v1.0.0"
	)
)
@Configuration
public class SwaggerConfig {
}
