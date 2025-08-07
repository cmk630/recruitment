package com.cmk630.recruitment.dto;

/*
	지원 이력 상세 정보 DTO 입니다.
*/
public record ApplicationHistoryDto(
	Long companyId,
	String companyName,
	String companyRegistrationNumber,
	Long jobDescriptionId,
	String jobDescriptionTitle
) {}
