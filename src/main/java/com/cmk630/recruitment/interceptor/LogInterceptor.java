package com.cmk630.recruitment.interceptor;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 모든 API 요청과 응답을 로깅하기 위한 인터셉터입니다.
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

	private static final String LOG_ID = "logId";

	/**
	 * 컨트롤러가 호출되기 전에 실행됩니다.
	 * 요청 로그를 남기고, 요청 추적을 위한 고유 ID를 생성하여 request attribute에 저장합니다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();
		request.setAttribute(LOG_ID, uuid);
		log.info("REQUEST [{}][{}][{}]", uuid, request.getMethod(), requestURI);
		return true; // true를 반환해야 다음 단계로 진행됩니다.
	}

	/**
	 * 컨트롤러가 정상적으로 실행된 후, View가 렌더링되기 전에 실행됩니다.
	 * 응답 로그를 남깁니다.
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		String uuid = (String) request.getAttribute(LOG_ID);

		// 요청 본문 로깅 추가
		if (request instanceof ContentCachingRequestWrapper cachingRequest) {
			byte[] content = cachingRequest.getContentAsByteArray();
			if (content.length > 0) {
				log.info("Request Body [{}]: {}", uuid, new String(content, "UTF-8"));
			}
		}

		log.info("RESPONSE [{}][{}][{}] STATUS: {}", uuid, request.getMethod(), request.getRequestURI(), response.getStatus());
	}

	/**
	 * 요청 처리가 완료된 후 (View 렌더링 포함) 실행됩니다.
	 * 예외 발생 여부와 관계없이 항상 호출됩니다.
	 * 예외가 발생했다면 예외 로그를 남깁니다.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (ex != null) {
			log.error("EXCEPTION!!", ex);
		}
	}
}
