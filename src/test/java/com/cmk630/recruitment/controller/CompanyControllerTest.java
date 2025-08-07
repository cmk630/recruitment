package com.cmk630.recruitment.controller;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cmk630.recruitment.dto.CompanyDto;
import com.cmk630.recruitment.entity.Application;
import com.cmk630.recruitment.entity.Company;
import com.cmk630.recruitment.entity.JobDescription;
import com.cmk630.recruitment.entity.JobSeeker;
import com.cmk630.recruitment.repository.ApplicationRepository;
import com.cmk630.recruitment.repository.CompanyRepository;
import com.cmk630.recruitment.repository.JobDescriptionRepository;
import com.cmk630.recruitment.repository.JobSeekerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("CompanyController 통합 테스트")
class CompanyControllerTest {

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

	@BeforeEach
	void setUp() {
		// 테스트에 공통적으로 사용될 기업과 채용공고를 미리 저장
		Company company = Company.builder()
			.name("테스트 기업")
			.companyRegistrationNumber("111-11-11111")
			.build();
		savedCompany = companyRepository.save(company);

		JobDescription jobDescription = JobDescription.builder()
			.title("테스트 채용공고")
			.company(savedCompany)
			.build();
		savedJobDescription = jobDescriptionRepository.save(jobDescription);
	}

	@Nested
	@DisplayName("기업 추가 API [POST /companies]")
	class CreateCompanyTest {

		@Test
		@DisplayName("성공: 유효한 정보로 기업을 추가한다")
		void createCompany_Success() throws Exception {
			// given
			CompanyDto.CreateCompanyRequest request = new CompanyDto.CreateCompanyRequest(
				"원티드랩", "299-86-00021");

			// when & then
			mockMvc.perform(post("/companies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 중복된 사업자 등록 번호로 추가할 수 없다")
		void createCompany_Fail_DuplicateRegistrationNumber() throws Exception {
			// given
			CompanyDto.CreateCompanyRequest request = new CompanyDto.CreateCompanyRequest(
				"다른 기업", savedCompany.getCompanyRegistrationNumber());

			// when & then
			mockMvc.perform(post("/companies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("BAD_REQUEST_BODY"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 사업자 등록 번호 형식이 올바르지 않으면 400 에러가 발생한다")
		void createCompany_Fail_InvalidFormat() throws Exception {
			// given
			CompanyDto.CreateCompanyRequest request = new CompanyDto.CreateCompanyRequest(
				"형식오류 기업", "1234567890");

			// when & then
			mockMvc.perform(post("/companies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("사업자 등록 번호 형식을(NNN-NN-NNNNN) 지켜주세요."))
				.andDo(print());
		}
	}


	@Nested
	@DisplayName("채용공고 등록 API [POST /companies/{company_id}/job-description]")
	class CreateJobDescriptionTest {

		@Test
		@DisplayName("성공: 존재하는 기업에 채용공고를 등록한다")
		void createJobDescription_Success() throws Exception {
			// given
			CompanyDto.JobDescriptionCreateRequest request = new CompanyDto.JobDescriptionCreateRequest("새로운 채용공고");

			// when & then
			mockMvc.perform(post("/companies/{company_id}/job-description", savedCompany.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 존재하지 않는 기업에는 채용공고를 등록할 수 없다")
		void createJobDescription_Fail_CompanyNotFound() throws Exception {
			// given
			long nonExistentCompanyId = -1L;
			CompanyDto.JobDescriptionCreateRequest request = new CompanyDto.JobDescriptionCreateRequest("실패할 공고");

			// when & then
			mockMvc.perform(post("/companies/{company_id}/job-description", nonExistentCompanyId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("COMPANY_NOT_FOUND"))
				.andDo(print());
		}
	}

	@Nested
	@DisplayName("채용공고 삭제 API [DELETE /companies/{company_id}/job-description/{job_description_id}]")
	class DeleteJobDescriptionTest {

		@Test
		@DisplayName("성공: 자신의 채용공고를 삭제한다")
		void deleteJobDescription_Success() throws Exception {
			// when & then
			mockMvc.perform(delete("/companies/{company_id}/job-description/{job_description_id}",
					savedCompany.getId(), savedJobDescription.getId()))
				.andExpect(status().isNoContent())
				.andDo(print());

			// 삭제되었는지 확인
			assertThat(jobDescriptionRepository.findById(savedJobDescription.getId())).isEmpty();
		}

		@Test
		@DisplayName("실패: 존재하지 않는 기업 ID로 요청하면 404 에러가 발생한다")
		void deleteJobDescription_Fail_CompanyNotFound() throws Exception {
			// given
			long nonExistentCompanyId = -1L;

			// when & then
			mockMvc.perform(delete("/companies/{company_id}/job-description/{job_description_id}",
					nonExistentCompanyId, savedJobDescription.getId()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("COMPANY_NOT_FOUND"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 존재하지 않는 채용공고 ID로 요청하면 404 에러가 발생한다")
		void deleteJobDescription_Fail_JobDescriptionNotFound() throws Exception {
			// given
			long nonExistentJobDescriptionId = -1L;

			// when & then
			mockMvc.perform(delete("/companies/{company_id}/job-description/{job_description_id}",
					savedCompany.getId(), nonExistentJobDescriptionId))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("JOB_DESCRIPTION_NOT_FOUND"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 다른 기업의 채용공고를 삭제하려 하면 403 에러가 발생한다")
		void deleteJobDescription_Fail_NoPermission() throws Exception {
			// given: 다른 기업 생성
			Company anotherCompany = companyRepository.save(Company.builder()
				.name("다른 기업")
				.companyRegistrationNumber("222-22-22222")
				.build());

			// when & then
			mockMvc.perform(delete("/companies/{company_id}/job-description/{job_description_id}",
					anotherCompany.getId(), savedJobDescription.getId()))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.code").value("NO_PERMISSION"))
				.andDo(print());
		}
	}

	@Nested
	@DisplayName("기업별 지원자 목록 조회 [GET /companies/{company_id}/applies]")
	class GetCompanyAppliesTest {

		@Test
		@DisplayName("성공: 지원자가 있는 경우 목록이 정상적으로 조회된다")
		void getCompanyApplies_Success_WithApplies() throws Exception {
			// given: 지원자 및 지원 이력 생성
			JobSeeker jobSeeker = jobSeekerRepository.save(JobSeeker.builder()
				.name("지원자").email("apply@test.com").password("pwd").build());
			applicationRepository.save(Application.builder()
				.jobSeeker(jobSeeker)
				.jobDescription(savedJobDescription)
				.build());

			// when & then
			mockMvc.perform(get("/companies/{company_id}/applies", savedCompany.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].jobDescriptionId").value(savedJobDescription.getId()))
				.andExpect(jsonPath("$[0].jobSeekers[0].jobSeekerId").value(jobSeeker.getId()))
				.andExpect(jsonPath("$[0].jobSeekers[0].jobSeekerName").value("지원자"))
				.andDo(print());
		}

		@Test
		@DisplayName("성공: 지원자가 없는 경우 빈 배열이 반환된다")
		void getCompanyApplies_Success_NoApplies() throws Exception {
			// when & then
			mockMvc.perform(get("/companies/{company_id}/applies", savedCompany.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].jobDescriptionId").value(savedJobDescription.getId()))
				.andExpect(jsonPath("$[0].jobSeekers").isEmpty()) // jobSeekers 배열이 비어있는지 확인
				.andDo(print());
		}

		@Test
		@DisplayName("실패: 존재하지 않는 기업 ID로 조회하면 404 에러가 발생한다")
		void getCompanyApplies_Fail_CompanyNotFound() throws Exception {
			// given
			long nonExistentCompanyId = -1L;
			// when & then
			mockMvc.perform(get("/companies/{company_id}/applies", nonExistentCompanyId))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("COMPANY_NOT_FOUND"))
				.andDo(print());
		}
	}
}
