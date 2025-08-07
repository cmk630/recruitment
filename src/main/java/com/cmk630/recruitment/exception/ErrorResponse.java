package com.cmk630.recruitment.exception;

import lombok.Getter;

/**
 * 예외 발생 시 클라이언트에게 반환될 에러 응답의 형식을 정의하는 클래스입니다.
 */
@Getter
public class ErrorResponse {

	private final String code;
	private final String message;

	public ErrorResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
	}

	public static ErrorResponse of(String code, String message) {
		return new ErrorResponse(code, message);
	}
}
