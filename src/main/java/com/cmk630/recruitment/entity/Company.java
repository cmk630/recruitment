package com.cmk630.recruitment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 기업 정보를 담는 엔티티 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기업 고유 ID

	@Column(nullable = false, length = 20)
	private String name; // 기업명

	@Column(nullable = false, unique = true, length = 12)
	private String companyRegistrationNumber; // 사업자 등록 번호 (고유값)

	// Company가 삭제될 때 연관된 JobDescription도 모두 삭제되도록 CascadeType.ALL과 orphanRemoval=true 설정
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JobDescription> jobDescriptions = new ArrayList<>();

	// // Company가 삭제될 때 연관된 Application도 모두 삭제되도록 CascadeType.ALL과 orphanRemoval=true 설정
	// @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, orphanRemoval = true)
	// private List<Application> applications;
	@Builder
	public Company(String name, String companyRegistrationNumber) {
		this.name = name;
		this.companyRegistrationNumber = companyRegistrationNumber;
	}
}
