package com.cmk630.recruitment.dto;

import java.util.List;

/**
 * 기업별 서류지원자 목록 조회 API의 응답(Response) DTO 입니다.
 */
public record ApplicationResponseDto(Long jobDescriptionId,
									 String jobDescriptionTitle,
									 List<JobSeekerInfoDto> jobSeekers) {}
