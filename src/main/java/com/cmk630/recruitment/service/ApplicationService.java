package com.cmk630.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmk630.recruitment.dto.ApplicationDto;
import com.cmk630.recruitment.entity.Application;
import com.cmk630.recruitment.entity.JobDescription;
import com.cmk630.recruitment.entity.JobSeeker;
import com.cmk630.recruitment.exception.BusinessException;
import com.cmk630.recruitment.exception.ErrorCode;
import com.cmk630.recruitment.repository.ApplicationRepository;
import com.cmk630.recruitment.repository.JobDescriptionRepository;
import com.cmk630.recruitment.repository.JobSeekerRepository;

/**
 * 지원 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@Transactional // 클래스 내 모든 public 메소드에 트랜잭션을 적용합니다.
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final JobSeekerRepository jobSeekerRepository;
	private final JobDescriptionRepository jobDescriptionRepository;

	/**
	 * 서류 지원을 생성합니다.
	 * @param request 지원 정보 DTO
	 */
	public void createApplication(ApplicationDto.Request request) {
		// 1. 구직자 존재 여부 확인
		JobSeeker jobSeeker = jobSeekerRepository.findById(request.userId())
			.orElseThrow(() -> new BusinessException(ErrorCode.JOB_SEEKER_NOT_FOUND));

		// 2. 채용공고 존재 여부 확인
		JobDescription jobDescription = jobDescriptionRepository.findById(request.jobDescriptionId())
			.orElseThrow(() -> new BusinessException(ErrorCode.JOB_DESCRIPTION_NOT_FOUND));

		// 3. 요청된 companyId와 실제 채용공고의 companyId가 일치하는지 확인 (무결성 검증)
		if (!jobDescription.getCompany().getId().equals(request.companyId())) {
			throw new BusinessException(ErrorCode.COMPANY_NOT_FOUND); // 혹은 NO_PERMISSION 등 더 적절한 에러 코드
		}

		// 4. 중복 지원 여부 확인
		if (applicationRepository.existsByJobSeekerIdAndJobDescriptionId(jobSeeker.getId(), jobDescription.getId())) {
			throw new BusinessException(ErrorCode.ALREADY_APPLY);
		}

		// 5. 지원 정보 생성 및 저장
		Application apply = Application.builder()
			.jobSeeker(jobSeeker)
			.jobDescription(jobDescription)
			.build();

		applicationRepository.save(apply);
	}
}
