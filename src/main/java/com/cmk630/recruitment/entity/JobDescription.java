package com.cmk630.recruitment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 채용공고 정보를 담는 엔티티 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class JobDescription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 채용공고 고유 ID

	@Column(nullable = false)
	private String title; // 채용공고 제목

	// 채용공고는 하나의 기업에 속합니다 (N:1 관계).
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	// 채용공고가 삭제될 때 연관된 지원서(Application)도 모두 삭제되도록 설정
	@OneToMany(mappedBy = "jobDescription", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Application> applications;

	@Builder
	public JobDescription(String title, Company company) {
		this.title = title;
		this.company = company;
	}
}
