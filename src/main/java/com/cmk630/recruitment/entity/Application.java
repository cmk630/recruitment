package com.cmk630.recruitment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 동일한 구직자가 동일한 채용공고에 중복 지원 방지
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"job_seeker_id", "job_description_id"})
})
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_seeker_id", nullable = false)
	private JobSeeker jobSeeker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_description_id", nullable = false)
	private JobDescription jobDescription;

	@Builder
	public Application(JobSeeker jobSeeker, JobDescription jobDescription) {
		this.jobSeeker = jobSeeker;
		this.jobDescription = jobDescription;
	}
}
