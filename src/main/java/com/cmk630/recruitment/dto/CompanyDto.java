package com.cmk630.recruitment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 기업 및 채용공고 관련 DTO를 정의하는 클래스입니다.
 */
public record CompanyDto() {

	/**
	 * 기업 추가 API의 요청 DTO 입니다.
	 */
	public record CreateCompanyRequest(
		@NotBlank(message = "기업명은 필수입니다.")
		@Size(min = 1, max = 20, message = "기업명은 1자 이상 20자 이하로 입력해주세요.")
		String name,

		@NotBlank(message = "사업자 등록 번호는 필수입니다.")
		@Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 등록 번호 형식을(NNN-NN-NNNNN) 지켜주세요.")
		String companyRegistrationNumber
	) {}

	/**
	 * 채용공고 등록 API의 요청 DTO 입니다.
	 */
	public record JobDescriptionCreateRequest(
		@NotBlank(message = "채용공고명은 필수입니다.")
		String title
	) {}
}
