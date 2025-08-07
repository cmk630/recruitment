package com.cmk630.recruitment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

/**
 * 지원 관련 데이터 전송 객체(DTO)를 정의하는 클래스입니다.
 * record를 사용하여 불변 객체로 간결하게 정의합니다.
 */
public record ApplicationDto() {

	/**
	 * 서류 지원 API의 요청(Request) DTO 입니다.
	 */
	public record Request(
		@NotNull(message = "구직자 ID는 필수입니다.")
		Long userId,

		@NotNull(message = "기업 ID는 필수입니다.")
		Long companyId,

		@NotNull(message = "채용공고 ID는 필수입니다.")
		Long jobDescriptionId
	) {}
}
