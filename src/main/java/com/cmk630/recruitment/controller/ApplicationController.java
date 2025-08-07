package com.cmk630.recruitment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmk630.recruitment.dto.ApplicationDto;
import com.cmk630.recruitment.exception.ErrorResponse;
import com.cmk630.recruitment.service.ApplicationService;

/**
 * 서류 지원 API를 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/applies")
public class ApplicationController {

	private final ApplicationService applicationService;

	/**
	 * 8. 서류 지원하기 API
	 * @param request 지원 정보 DTO
	 * @return 201 CREATED
	 */
	@Operation(summary = "서류 지원하기", description = "구직자가 특정 채용공고에 지원합니다.") // API 설명
	@ApiResponses(value = { // API 응답 시나리오 정의
		@ApiResponse(responseCode = "201", description = "지원 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (중복 지원 등)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 (구직자, 기업, 채용공고 등)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping
	public ResponseEntity<Void> apply(@RequestBody @Valid ApplicationDto.Request request) {
		applicationService.createApplication(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
