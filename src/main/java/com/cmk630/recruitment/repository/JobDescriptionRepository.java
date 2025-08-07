package com.cmk630.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.cmk630.recruitment.entity.JobDescription;

/**
 * JobDescription 엔티티에 대한 데이터 접근을 처리하는 리포지토리입니다.
 */
public interface JobDescriptionRepository extends JpaRepository<JobDescription, Long> {

	/**
	 * 채용공고 ID와 기업 ID로 채용공고를 조회합니다.
	 * 특정 기업에 속한 채용공고인지 검증할 때 사용됩니다.
	 * @param jobDescriptionId 채용공고 ID
	 * @param companyId 기업 ID
	 * @return Optional<JobDescription> 채용공고 정보
	 */
	Optional<JobDescription> findByIdAndCompanyId(Long jobDescriptionId, Long companyId);
}
