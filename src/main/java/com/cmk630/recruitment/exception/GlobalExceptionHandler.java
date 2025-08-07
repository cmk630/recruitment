package com.cmk630.recruitment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 핸들러 클래스입니다.
 * @RestControllerAdvice 어노테이션을 통해 모든 @RestController에서 발생하는 예외를 감지합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 직접 정의한 BusinessException을 처리합니다.
	 * @param e 발생한 BusinessException
	 * @return ErrorResponse를 담은 ResponseEntity
	 */
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		ErrorCode errorCode = e.getErrorCode();
		ErrorResponse response = ErrorResponse.of(errorCode);
		log.warn("BusinessException: {}", response.getMessage());

		// ErrorCode에 따라 적절한 HTTP 상태 코드를 결정합니다.
		HttpStatus status = switch (errorCode) {
			case COMPANY_NOT_FOUND, JOB_DESCRIPTION_NOT_FOUND, JOB_SEEKER_NOT_FOUND, APPLICATION_NOT_FOUND ->
				HttpStatus.NOT_FOUND;
			case NO_PERMISSION -> HttpStatus.FORBIDDEN;
			default -> HttpStatus.BAD_REQUEST;
		};

		return new ResponseEntity<>(response, status);
	}

	/**
	 * @Valid 어노테이션을 통한 유효성 검증 실패 시 발생하는 예외를 처리합니다.
	 * @param e 발생한 MethodArgumentNotValidException
	 * @return ErrorResponse를 담은 ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		String defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_BODY.getCode(), defaultMessage);
		log.warn("MethodArgumentNotValidException: {}", defaultMessage);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 위에서 처리하지 못한 모든 예외를 처리합니다. (최후의 보루)
	 * @param e 발생한 Exception
	 * @return ErrorResponse를 담은 ResponseEntity (500 Internal Server Error)
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Unhandled Exception: ", e);
		ErrorResponse response = ErrorResponse.of("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
