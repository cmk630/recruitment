package com.cmk630.recruitment.filter;

import java.io.IOException;

import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 들어오는 모든 요청을 ContentCachingRequestWrapper로 감싸서
 * 요청의 Body를 여러 번 읽을 수 있도록 캐싱하는 필터입니다.
 */
public class CachingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		// HttpServletRequest를 ContentCachingRequestWrapper로 래핑합니다.
		ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);

		// 다음 필터 또는 서블릿으로 래핑된 요청(wrappingRequest)을 전달합니다.
		// 이 시점 이후부터는 컨트롤러나 인터셉터에서 request.getInputStream()을 여러 번 호출해도 괜찮습니다.
		chain.doFilter(wrappingRequest, response);
	}

	// init()와 destroy() 메소드는 이 경우 특별한 로직이 필요 없으므로 비워둡니다.
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}
