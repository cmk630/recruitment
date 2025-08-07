package com.cmk630.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import com.cmk630.recruitment.dto.ApplicationResponseDto;
import com.cmk630.recruitment.dto.CompanyDto;
import com.cmk630.recruitment.entity.Company;

/**
 * Company 엔티티에 대한 데이터 접근을 처리하는 리포지토리입니다.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

	/**
	 * 주어진 사업자 등록 번호로 등록된 기업이 있는지 확인합니다.
	 * @param companyRegistrationNumber 사업자 등록 번호
	 * @return 존재 여부 (true/false)
	 */
	boolean existsByCompanyRegistrationNumber(String companyRegistrationNumber);

}
