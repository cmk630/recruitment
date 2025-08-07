package com.cmk630.recruitment.repository;

import static com.cmk630.recruitment.entity.QApplication.*;
import static com.cmk630.recruitment.entity.QJobDescription.*;
import static com.cmk630.recruitment.entity.QJobSeeker.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmk630.recruitment.dto.ApplicationResponseDto;
import com.cmk630.recruitment.dto.CompanyApplicationFlatDto;
import com.cmk630.recruitment.dto.JobSeekerInfoDto;
import com.cmk630.recruitment.entity.Company;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * Company 처리하는 리포지토리입니다.
 */
@Repository
@RequiredArgsConstructor
public class CompanyQueryRepository {
	final JPAQueryFactory queryFactory;

	/**
	 * 특정 회사의 채용공고별 지원자 목록을 조회하는 메서드입니다.
	 * @param companyId 조회할 회사의 ID
	 * @return 계층 구조를 가진 응답 DTO 리스트
	 */
	public List<CompanyApplicationFlatDto> findApplicationsforCompanyId(Long companyId) {
		return queryFactory
			.select(
				// 1단계에서 만든 플랫 DTO의 생성자를 호출합니다.
				Projections.constructor(CompanyApplicationFlatDto.class,
					jobDescription.id,
					jobDescription.title,
					jobSeeker.id,
					jobSeeker.name,
					jobSeeker.email
				)
			)
			.from(jobDescription)
			.leftJoin(application).on(application.jobDescription.id.eq(jobDescription.id))
			.leftJoin(application.jobSeeker, jobSeeker)
			.where(jobDescription.company.id.eq(companyId))
			.fetch(); // transform 대신 fetch()를 사용합니다.
	}
}
