package com.cmk630.recruitment.controller;

import com.cmk630.recruitment.dto.ApplicationDto;
import com.cmk630.recruitment.entity.Application;
import com.cmk630.recruitment.entity.Company;
import com.cmk630.recruitment.entity.JobDescription;
import com.cmk630.recruitment.entity.JobSeeker;
import com.cmk630.recruitment.repository.ApplicationRepository;
import com.cmk630.recruitment.repository.CompanyRepository;
import com.cmk630.recruitment.repository.JobDescriptionRepository;
import com.cmk630.recruitment.repository.JobSeekerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("ApplicationController 통합 테스트")
class ApplicationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;

	@Autowired
	private JobSeekerRepository jobSeekerRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	private Company savedCompany;
	private JobDescription savedJobDescription;
	private JobSeeker savedJobSeeker;

	@BeforeEach
	void setUp() {
		// 테스트에 공통적으로 사용될 데이터 미리 저장
		savedCompany = companyRepository.save(Company.builder()
			.name("테스트 기업")
			.companyRegistrationNumber("111-11-11111")
			.build());

		savedJobDescription = jobDescriptionRepository.save(JobDescription.builder()
			.title("테스트 채용공고")
			.company(savedCompany)
			.build());

		savedJobSeeker = jobSeekerRepository.save(JobSeeker.builder()
			.name("홍길동")
			.email("apply@test.com")
			.password("Password123!")
			.build());
	}

	@Nested
	@DisplayName("서류 지원하기 API [POST /applies]")
	class CreateApplyTest {

		@Test
		@DisplayName("성공: 유효한 정보로 서류 지원에 성공한다")
		void createApply_Success() throws Exception {
			// given
			ApplicationDto.Request request = new ApplicationDto.Request(
				savedJobSeeker.getId(),
				savedCompany.getId(),
				savedJobDescription.getId()
			);

			// when & then
			mockMvc.perform(post("/applies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 동일한 채용공고에 중복 지원할 수 없다")
		void createApply_Fail_AlreadyApplied() throws Exception {
			// given: 먼저 한 번 지원
			applicationRepository.save(Application.builder()
				.jobSeeker(savedJobSeeker)
				.jobDescription(savedJobDescription)
				.build());

			ApplicationDto.Request request = new ApplicationDto.Request(
				savedJobSeeker.getId(),
				savedCompany.getId(),
				savedJobDescription.getId()
			);

			// when & then
			mockMvc.perform(post("/applies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("ALREADY_APPLY"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 존재하지 않는 구직자 ID로 지원할 수 없다")
		void createApply_Fail_JobSeekerNotFound() throws Exception {
			// given
			long nonExistentJobSeekerId = -1L;
			ApplicationDto.Request request = new ApplicationDto.Request(
				nonExistentJobSeekerId,
				savedCompany.getId(),
				savedJobDescription.getId()
			);

			// when & then
			mockMvc.perform(post("/applies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("JOB_SEEKER_NOT_FOUND"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 존재하지 않는 채용공고 ID로 지원할 수 없다")
		void createApply_Fail_JobDescriptionNotFound() throws Exception {
			// given
			long nonExistentJobDescriptionId = -1L;
			ApplicationDto.Request request = new ApplicationDto.Request(
				savedJobSeeker.getId(),
				savedCompany.getId(),
				nonExistentJobDescriptionId
			);

			// when & then
			mockMvc.perform(post("/applies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("JOB_DESCRIPTION_NOT_FOUND"))
				.andDo(print());
		}
	}
}
