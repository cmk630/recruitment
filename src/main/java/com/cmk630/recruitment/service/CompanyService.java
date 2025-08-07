package com.cmk630.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cmk630.recruitment.dto.ApplicationResponseDto;
import com.cmk630.recruitment.dto.CompanyApplicationFlatDto;
import com.cmk630.recruitment.dto.CompanyDto;
import com.cmk630.recruitment.dto.JobSeekerInfoDto;
import com.cmk630.recruitment.entity.Company;
import com.cmk630.recruitment.entity.JobDescription;
import com.cmk630.recruitment.exception.BusinessException;
import com.cmk630.recruitment.exception.ErrorCode;
import com.cmk630.recruitment.repository.CompanyQueryRepository;
import com.cmk630.recruitment.repository.CompanyRepository;
import com.cmk630.recruitment.repository.JobDescriptionRepository;

/**
 * 기업 및 채용공고 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final CompanyQueryRepository companyQueryRepository;
	private final JobDescriptionRepository jobDescriptionRepository;

	/**
	 * 새로운 기업을 등록합니다.
	 * @param request 기업 생성 정보 DTO
	 */
	public void createCompany(CompanyDto.CreateCompanyRequest request) {
		// 사업자 등록 번호 중복 확인
		if (companyRepository.existsByCompanyRegistrationNumber(request.companyRegistrationNumber())) {
			throw new BusinessException(ErrorCode.DUPLICATE_COMPANY_REGISTRATION);
		}
		Company company = Company.builder()
			.name(request.name())
			.companyRegistrationNumber(request.companyRegistrationNumber())
			.build();
		companyRepository.save(company);
	}

	/**
	 * 기업을 삭제합니다.
	 * JPA Cascade 설정에 의해 해당 기업의 채용공고, 지원이력도 함께 삭제됩니다.
	 * @param companyId 삭제할 기업 ID
	 */
	public void deleteCompany(Long companyId) {
		companyRepository.deleteById(companyId);
	}

	/**
	 * 특정 기업의 채용공고를 등록합니다.
	 * @param companyId 기업 ID
	 * @param request 채용공고 생성 정보 DTO
	 */
	public void createJobDescription(Long companyId, CompanyDto.JobDescriptionCreateRequest request) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new BusinessException(ErrorCode.COMPANY_NOT_FOUND));

		JobDescription jobDescription = JobDescription.builder()
			.title(request.title())
			.company(company)
			.build();
		jobDescriptionRepository.save(jobDescription);
	}

	/**
	 * 채용공고를 삭제합니다.
	 * @param companyId 기업 ID
	 * @param jobDescriptionId 삭제할 채용공고 ID
	 */
	public void deleteJobDescription(Long companyId, Long jobDescriptionId) {
		// 1. 기업 존재 여부 확인
		if (!companyRepository.existsById(companyId)) {
			throw new BusinessException(ErrorCode.COMPANY_NOT_FOUND);
		}
		// 2. 채용공고 존재 여부 확인
		JobDescription jobDescription = jobDescriptionRepository.findById(jobDescriptionId)
			.orElseThrow(() -> new BusinessException(ErrorCode.JOB_DESCRIPTION_NOT_FOUND));

		// 3. 해당 기업에 속한 채용공고가 맞는지 권한 확인
		if (!jobDescription.getCompany().getId().equals(companyId)) {
			throw new BusinessException(ErrorCode.NO_PERMISSION);
		}

		// 4. 채용공고 삭제
		jobDescriptionRepository.delete(jobDescription);
	}

	/**
	 * 특정 기업의 채용공고별 지원자 목록을 조회합니다.
	 * @param companyId 조회할 기업 ID
	 * @return 지원자 목록 응답 DTO 리스트
	 */
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 성능 최적화
	public List<ApplicationResponseDto> getCompanyApplies(Long companyId) {
		// 기업 존재 여부 확인
		if (!companyRepository.existsById(companyId)) {
			throw new BusinessException(ErrorCode.COMPANY_NOT_FOUND);
		}
		// findApplicationsforCompanyId 메소드를 호출하여 플랫한 데이터 리스트를 받습니다.
		List<CompanyApplicationFlatDto> flatResults = companyQueryRepository.findApplicationsforCompanyId(companyId);

		// Java Stream을 사용하여 플랫한 데이터를 계층 구조로 변환합니다.
		Map<Long, List<JobSeekerInfoDto>> groupedByJobDescId = flatResults.stream()
			.filter(flatDto -> flatDto.jobSeekerId() != null) // 지원자가 없는 경우는 제외
			.collect(Collectors.groupingBy(
				CompanyApplicationFlatDto::jobDescriptionId,
				Collectors.mapping(
					flatDto -> new JobSeekerInfoDto(flatDto.jobSeekerId(), flatDto.jobSeekerName(),
						flatDto.jobSeekerEmail()),
					Collectors.toList()
				)
			));

		// 최종 응답 DTO 리스트를 생성합니다.
		return flatResults.stream()
			.map(flatDto -> new ApplicationResponseDto(
				flatDto.jobDescriptionId(),
				flatDto.jobDescriptionTitle(),
				// 위에서 그룹화한 지원자 목록을 가져옵니다. 지원자가 없으면 빈 리스트를 반환합니다.
				groupedByJobDescId.getOrDefault(flatDto.jobDescriptionId(), List.of())
			))
			.distinct() // 채용공고가 중복될 수 있으므로 distinct()로 제거합니다.
			.collect(Collectors.toList());
	}
}
