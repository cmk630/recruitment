package com.cmk630.recruitment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 애플리케이션에서 발생할 수 있는 비즈니스 예외 상황을 열거형으로 정의합니다.
 * 각 에러 코드는 HTTP 응답에 사용될 코드 문자열과 메시지를 가집니다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 400 BAD_REQUEST: 잘못된 요청
	BAD_REQUEST_BODY("BAD_REQUEST_BODY", "요청 Body의 형식이 잘못되었습니다."),
	DUPLICATE_EMAIL("BAD_REQUEST_BODY", "이미 존재하는 이메일입니다."),
	DUPLICATE_COMPANY_REGISTRATION("BAD_REQUEST_BODY", "이미 등록된 사업자 번호입니다."),
	ALREADY_APPLY("ALREADY_APPLY", "이미 해당 공고에 지원했습니다."),

	// 403 FORBIDDEN: 권한 없음
	NO_PERMISSION("NO_PERMISSION", "해당 기업의 채용공고가 아닙니다."),

	// 404 NOT_FOUND: 리소스를 찾을 수 없음
	COMPANY_NOT_FOUND("COMPANY_NOT_FOUND", "존재하지 않는 기업입니다."),
	JOB_DESCRIPTION_NOT_FOUND("JOB_DESCRIPTION_NOT_FOUND", "존재하지 않는 채용공고입니다."),
	JOB_SEEKER_NOT_FOUND("JOB_SEEKER_NOT_FOUND", "존재하지 않는 구직자입니다."),
	APPLICATION_NOT_FOUND("APPLICATION_NOT_FOUND", "존재하지 않는 지원 이력입니다.");

	private final String code;
	private final String message;
}
