package com.cmk630.recruitment.controller;

import com.cmk630.recruitment.dto.JobSeekerDto;
import com.cmk630.recruitment.exception.ErrorResponse;
import com.cmk630.recruitment.service.JobSeekerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 구직자 관련 API를 처리하는 컨트롤러입니다.
 */
@Tag(name = "Job-Seekers", description = "구직자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/job-seekers")
public class JobSeekerController {

	private final JobSeekerService jobSeekerService;

	@Operation(summary = "구직자 회원가입", description = "새로운 구직자 정보를 시스템에 등록합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (중복 이메일 또는 유효성 검사 실패)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping
	public ResponseEntity<Void> createJobSeeker(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "생성할 구직자의 이름, 이메일, 비밀번호", required = true,
			content = @Content(schema = @Schema(implementation = JobSeekerDto.CreateUserRequest.class))
		)
		@RequestBody @Valid JobSeekerDto.CreateUserRequest request) {

		jobSeekerService.createJobSeeker(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@Operation(summary = "구직자 전체 목록 조회", description = "시스템에 등록된 모든 구직자 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping
	public ResponseEntity<List<JobSeekerDto.GetResponse>> getAllJobSeekers() {

		List<JobSeekerDto.GetResponse> jobSeekers = jobSeekerService.getAllJobSeekers();
		return ResponseEntity.ok(jobSeekers);

	}

	@Operation(summary = "구직자 삭제", description = "특정 구직자의 정보를 삭제합니다. 연관된 지원 이력도 함께 삭제됩니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "삭제 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 구직자",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@DeleteMapping("/{job_seeker_id}")
	public ResponseEntity<Void> deleteJobSeeker(
		@Parameter(description = "삭제할 구직자의 ID", required = true) @PathVariable("job_seeker_id") Long jobSeekerId) {

		jobSeekerService.deleteJobSeeker(jobSeekerId);
		return ResponseEntity.noContent().build();

	}

	@Operation(summary = "특정 구직자의 지원 이력 조회", description = "특정 구직자가 지원한 모든 채용공고의 이력을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 구직자",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/{job_seeker_id}/applies")
	public ResponseEntity<JobSeekerDto.ApplicationHistoryResponse> getApplyHistory(
		@Parameter(description = "지원 이력을 조회할 구직자의 ID", required = true) @PathVariable("job_seeker_id") Long jobSeekerId) {

		JobSeekerDto.ApplicationHistoryResponse history = jobSeekerService.getApplicationHistory(jobSeekerId);
		return ResponseEntity.ok(history);

	}
}
