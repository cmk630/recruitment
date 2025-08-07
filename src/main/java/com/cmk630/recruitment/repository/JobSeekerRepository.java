package com.cmk630.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.cmk630.recruitment.dto.ApplicationHistoryDto;
import com.cmk630.recruitment.dto.JobSeekerDto;
import com.cmk630.recruitment.entity.JobSeeker;

/**
 * JobSeeker 엔티티에 대한 데이터 접근을 처리하는 리포지토리입니다.
 */
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {

	/**
	 * 주어진 이메일로 등록된 구직자가 있는지 확인합니다.
	 * @param email 이메일 주소
	 * @return 존재 여부 (true/false)
	 */
	boolean existsByEmail(String email);

	/**
	 * 특정 구직자의 모든 지원 이력을 조회합니다.
	 * JPQL을 사용하여 필요한 정보만 DTO로 직접 프로젝션하여 성능을 최적화합니다.
	 * @param jobSeekerId 조회할 구직자 ID
	 * @return 지원 이력 정보가 담긴 DTO 리스트
	 */
	@Query("""
        SELECT new com.cmk630.recruitment.dto.ApplicationHistoryDto(
            c.id, c.name, c.companyRegistrationNumber, jd.id, jd.title
        )
        FROM Application a
        JOIN a.jobDescription jd
        JOIN jd.company c
        WHERE a.jobSeeker.id = :jobSeekerId
    """)
	List<ApplicationHistoryDto> findApplicationHistoriesByJobSeekerId(Long jobSeekerId);
}
