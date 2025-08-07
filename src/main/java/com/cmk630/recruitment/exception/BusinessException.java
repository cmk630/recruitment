package com.cmk630.recruitment.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 상의 예외를 나타내기 위한 커스텀 예외 클래스입니다.
 * ErrorCode를 멤버로 가지며, 이를 통해 예외 상황을 체계적으로 관리합니다.
 */
@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
