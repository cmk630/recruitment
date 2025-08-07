package com.cmk630.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Collections;

import com.cmk630.recruitment.dto.ApplicationHistoryDto;
import com.cmk630.recruitment.dto.JobSeekerDto;
import com.cmk630.recruitment.entity.JobSeeker;
import com.cmk630.recruitment.exception.BusinessException;
import com.cmk630.recruitment.exception.ErrorCode;
import com.cmk630.recruitment.repository.JobSeekerRepository;

/**
 * 구직자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class JobSeekerService {

	private final JobSeekerRepository jobSeekerRepository;
	/**
	 * 새로운 구직자를 등록합니다.
	 * @param request 구직자 생성 정보 DTO
	 */
	public void createJobSeeker(JobSeekerDto.CreateUserRequest request) {
		// 이메일 중복 확인
		if (jobSeekerRepository.existsByEmail(request.email())) {
			throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
		}
		// 실무에서는 비밀번호를 반드시 암호화해야 합니다. (e.g., Spring Security BCryptPasswordEncoder)
		JobSeeker jobSeeker = JobSeeker.builder()
			.name(request.name())
			.email(request.email())
			.password(request.password()) // 암호화 로직 필요
			.build();
		jobSeekerRepository.save(jobSeeker);
	}

	/**
	 * 모든 구직자 목록을 조회합니다.
	 * @return 구직자 정보 DTO 리스트
	 */
	@Transactional(readOnly = true)
	public List<JobSeekerDto.GetResponse> getAllJobSeekers() {
		return jobSeekerRepository.findAll().stream()
			.map(JobSeekerDto.GetResponse::from)
			.toList();
	}

	/**
	 * 구직자를 삭제합니다.
	 * JPA Cascade 설정에 의해 해당 구직자의 지원 이력도 함께 삭제됩니다.
	 * @param jobSeekerId 삭제할 구직자 ID
	 */
	public void deleteJobSeeker(Long jobSeekerId) {
		jobSeekerRepository.deleteById(jobSeekerId);
	}

	/**
	 * 특정 구직자의 지원 이력을 조회합니다.
	 * @param jobSeekerId 조회할 구직자 ID
	 * @return 지원 이력 응답 DTO
	 */
	@Transactional(readOnly = true)
	public JobSeekerDto.ApplicationHistoryResponse getApplicationHistory(Long jobSeekerId) {
		// 구직자 존재 여부 확인
		if (!jobSeekerRepository.existsById(jobSeekerId)) {
			throw new BusinessException(ErrorCode.JOB_SEEKER_NOT_FOUND);
		}

		List<ApplicationHistoryDto> histories = jobSeekerRepository.findApplicationHistoriesByJobSeekerId(jobSeekerId);

		// 지원 이력이 없는 경우 요구사항에 맞게 빈 리스트와 count 0을 반환
		if (histories == null || histories.isEmpty()) {
			return new JobSeekerDto.ApplicationHistoryResponse(0, Collections.emptyList());
		}

		return new JobSeekerDto.ApplicationHistoryResponse(histories.size(), histories);
	}
}
