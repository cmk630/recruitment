package com.cmk630.recruitment.dto;

// 조회된 회사 지원서의 데이터를 담는 역할을 합니다.
public record CompanyApplicationFlatDto (
	Long jobDescriptionId,
	String jobDescriptionTitle,
	Long jobSeekerId,
	String jobSeekerName,
	String jobSeekerEmail
) {}
