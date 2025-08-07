package com.cmk630.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import com.cmk630.recruitment.dto.JobSeekerDto;
import com.cmk630.recruitment.entity.Application;

/**
 * Application 엔티티에 대한 데이터 접근을 처리하는 리포지토리입니다.
 * Spring Data JPA를 사용하여 기본적인 CRUD 및 쿼리 메소드를 제공합니다.
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	/**
	 * 특정 구직자가 특정 채용공고에 지원했는지 여부를 확인합니다.
	 * @param jobSeekerId 구직자 ID
	 * @param jobDescriptionId 채용공고 ID
	 * @return 지원 존재 여부 (true/false)
	 */
	boolean existsByJobSeekerIdAndJobDescriptionId(Long jobSeekerId, Long jobDescriptionId);

	/**
	 * 특정 구직자와 채용공고 ID로 지원 정보를 조회합니다.
	 * @param jobSeekerId 구직자 ID
	 * @param jobDescriptionId 채용공고 ID
	 * @return Optional<Apply> 지원 정보
	 */
	Optional<Application> findByJobSeekerIdAndJobDescriptionId(Long jobSeekerId, Long jobDescriptionId);

}
