package com.cmk630.recruitment.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cmk630.recruitment.dto.JobSeekerDto;
import com.cmk630.recruitment.entity.JobSeeker;
import com.cmk630.recruitment.repository.JobSeekerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JobSeekerController에 대한 통합 테스트 클래스입니다.
 */
@SpringBootTest // Spring Boot 애플리케이션 컨텍스트를 로드하여 테스트
@AutoConfigureMockMvc // MockMvc를 자동 설정하여 컨트롤러 테스트를 위함
@Transactional // 각 테스트 후 롤백을 위함
class JobSeekerControllerTest {

	@Autowired
	private MockMvc mockMvc; // HTTP 요청을 시뮬레이션하는 객체

	@Autowired
	private ObjectMapper objectMapper; // Java 객체와 JSON 간의 변환을 위함

	@Autowired
	private JobSeekerRepository jobSeekerRepository;

	@Test
	@DisplayName("성공: 구직자 추가 API")
	void createJobSeeker_Success() throws Exception {
		// given: 테스트를 위한 데이터 준비
		JobSeekerDto.CreateUserRequest request = new JobSeekerDto.CreateUserRequest(
			"원티드01", "ai@wantedlab.com", "1q2w3e4r!");

		// when & then: 실제 테스트 실행 및 결과 검증
		mockMvc.perform(post("/job-seekers") // POST /job-seekers 요청
				.contentType(MediaType.APPLICATION_JSON) // 요청 본문의 타입은 JSON
				.content(objectMapper.writeValueAsString(request))) // 요청 본문에 DTO를 JSON 문자열로 변환하여 삽입
			.andExpect(status().isCreated()) // 응답 상태 코드가 201 CREATED 인지 검증
			.andDo(print()); // 요청/응답 전체 내용 출력
	}

	@Test
	@DisplayName("실패: 구직자 추가 - 중복된 이메일")
	void createJobSeeker_Fail_DuplicateEmail() throws Exception {
		// given: 이미 같은 이메일의 사용자가 존재하는 상황을 만듦
		jobSeekerRepository.save(JobSeeker.builder()
			.name("기존유저")
			.email("ai@wantedlab.com")
			.password("Password123!")
			.build());

		JobSeekerDto.CreateUserRequest request = new JobSeekerDto.CreateUserRequest(
			"새로운유저", "ai@wantedlab.com", "NewPassword123!");

		// when & then
		mockMvc.perform(post("/job-seekers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest()) // 응답 상태 코드가 400 BAD REQUEST 인지 검증
			.andExpect(jsonPath("$.code").value("BAD_REQUEST_BODY")) // 응답 JSON의 code 필드 값 검증
			.andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다.")) // 응답 JSON의 message 필드 값 검증
			.andDo(print());
	}

	@Test
	@DisplayName("실패: 구직자 추가 - 비밀번호 형식 오류")
	void createJobSeeker_Fail_InvalidPassword() throws Exception {
		// given
		JobSeekerDto.CreateUserRequest request = new JobSeekerDto.CreateUserRequest(
			"원티드01", "ai@wantedlab.com", "password"); // 형식에 맞지 않는 비밀번호

		// when & then
		mockMvc.perform(post("/job-seekers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("BAD_REQUEST_BODY"))
			.andExpect(jsonPath("$.message").value("비밀번호는 8~16자, 영문 대소문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다."))
			.andDo(print());
	}
}
