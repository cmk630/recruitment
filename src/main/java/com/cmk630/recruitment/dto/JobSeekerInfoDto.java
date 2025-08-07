package com.cmk630.recruitment.dto;
/*
	지원자 정보 DTO 입니다.
*/
public record JobSeekerInfoDto(
	Long jobSeekerId,
	String jobSeekerName,
	String email
) {}
