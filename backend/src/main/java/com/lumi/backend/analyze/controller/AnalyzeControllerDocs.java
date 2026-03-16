package com.lumi.backend.analyze.controller;

import com.lumi.backend.analyze.dto.AnalyzeRequest;
import com.lumi.backend.analyze.dto.AnalyzeResponse;
import com.lumi.backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Analyze", description = "로그 분석 API")
public interface AnalyzeControllerDocs {

    @Operation(summary = "로그 분석", description = "로그 텍스트를 입력받아 AI가 에러를 분석하고 해결 방법을 제안합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "분석 성공",
                    content = @Content(schema = @Schema(implementation = AnalyzeResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "로그 입력값 없음"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "AI 서버 오류"
            )
    })
    ApiResponse<AnalyzeResponse> analyzeLog(@Valid @RequestBody AnalyzeRequest request);
}
