package com.cmk630.recruitment.controller;

import com.cmk630.recruitment.dto.ApplicationResponseDto;
import com.cmk630.recruitment.dto.CompanyDto;
import com.cmk630.recruitment.exception.ErrorResponse;
import com.cmk630.recruitment.service.CompanyService;
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
 * 기업 및 채용공고 관련 API를 처리하는 컨트롤러입니다.
 */
@Tag(name = "Companies", description = "기업 및 채용공고 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

	private final CompanyService companyService;

	@Operation(summary = "기업 추가", description = "새로운 기업 정보를 시스템에 등록합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "기업 등록 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (중복된 사업자 번호 또는 유효성 검사 실패)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping
	public ResponseEntity<Void> createCompany(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "생성할 기업의 이름과 사업자 등록 번호", required = true,
			content = @Content(schema = @Schema(implementation = CompanyDto.CreateCompanyRequest.class))
		)
		@RequestBody @Valid CompanyDto.CreateCompanyRequest request) {
		companyService.createCompany(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "기업 삭제", description = "특정 기업의 정보를 삭제합니다. 연관된 채용공고 및 지원이력도 함께 삭제됩니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "기업 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 기업",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@DeleteMapping("/{company_id}")
	public ResponseEntity<Void> deleteCompany(
		@Parameter(description = "삭제할 기업의 ID", required = true) @PathVariable("company_id") Long companyId) {
		companyService.deleteCompany(companyId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "채용공고 등록", description = "특정 기업에 새로운 채용공고를 등록합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "채용공고 등록 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 기업",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/{company_id}/job-description")
	public ResponseEntity<Void> createJobDescription(
		@Parameter(description = "채용공고를 등록할 기업의 ID", required = true) @PathVariable("company_id") Long companyId,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "생성할 채용공고의 제목", required = true,
			content = @Content(schema = @Schema(implementation = CompanyDto.JobDescriptionCreateRequest.class))
		)
		@RequestBody @Valid CompanyDto.JobDescriptionCreateRequest request) {
		companyService.createJobDescription(companyId, request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "채용공고 삭제", description = "특정 기업의 채용공고를 삭제합니다. 연관된 지원이력도 함께 삭제됩니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "채용공고 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음 (다른 회사의 채용공고 삭제 시도)",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 기업 또는 채용공고",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@DeleteMapping("/{company_id}/job-description/{job_description_id}")
	public ResponseEntity<Void> deleteJobDescription(
		@Parameter(description = "기업 ID", required = true) @PathVariable("company_id") Long companyId,
		@Parameter(description = "삭제할 채용공고의 ID", required = true) @PathVariable("job_description_id") Long jobDescriptionId) {
		companyService.deleteJobDescription(companyId, jobDescriptionId);
		return ResponseEntity.noContent().build();
	}

	// API 엔드포인트명을 applications 로 변경하는 것을 권장합니다.
	@Operation(summary = "기업별 지원자 목록 조회", description = "특정 기업의 모든 채용공고와 각 공고에 지원한 지원자 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 기업",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/{company_id}/applies")
	public ResponseEntity<List<ApplicationResponseDto>> getCompanyApplies(
		@Parameter(description = "지원자 목록을 조회할 기업의 ID", required = true) @PathVariable("company_id") Long companyId) {
		List<ApplicationResponseDto> applies = companyService.getCompanyApplies(companyId);
		return ResponseEntity.ok(applies);
	}
}
