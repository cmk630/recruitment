package com.cmk630.recruitment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 구직자 정보를 담는 엔티티 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class JobSeeker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 구직자 고유 ID

	@Column(nullable = false, length = 16)
	private String name; // 구직자 이름

	@Column(nullable = false, unique = true)
	private String email; // 이메일 (고유값)

	@Column(nullable = false)
	private String password; // 비밀번호

	// 구직자가 삭제될 때 연관된 지원서(Application)도 모두 삭제되도록 설정
	@OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Application> applies;
	@Builder
	public JobSeeker(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
}
