package com.cmk630.recruitment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.cmk630.recruitment.entity.JobSeeker;

/**
 * 구직자 관련 DTO를 정의하는 클래스입니다.
 */
public record JobSeekerDto() {

	/**
	 * 구직자 추가 API의 요청 DTO 입니다.
	 */
	public record CreateUserRequest(
		@NotBlank(message = "이름은 필수 입력입니다.")
		@Size(min = 1, max = 16, message = "이름은 1자 이상 16자 이하로 입력해주세요.")
		String name,

		@NotBlank(message = "이메일은 필수 입력입니다.")
		@Email(message = "이메일 형식을 지켜주세요.")
		String email,

		@NotBlank(message = "비밀번호는 필수 입력입니다.")
		@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$",
			message = "비밀번호는 8~16자, 영문 대소문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다.")
		String password
	) {}

	/**
	 * 구직자 목록 조회 API의 응답 DTO 입니다.
	 */
	public record GetResponse(
		Long id,
		String name,
		String email
	) {
		/**
		 * JobSeeker 엔티티를 GetResponse DTO로 변환하는 정적 팩토리 메소드입니다.
		 * @param jobSeeker JobSeeker 엔티티
		 * @return GetResponse DTO
		 */
		public static GetResponse from(JobSeeker jobSeeker) {
			return new GetResponse(jobSeeker.getId(), jobSeeker.getName(), jobSeeker.getEmail());
		}
	}

	/**
	 * 특정 구직자 서류지원 이력 조회 API의 응답 DTO 입니다.
	 */
	public record ApplicationHistoryResponse(
		long totalApplicationCount,
		List<ApplicationHistoryDto> applies
	) {}

}
