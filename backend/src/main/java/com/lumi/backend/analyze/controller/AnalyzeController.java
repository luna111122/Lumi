package com.lumi.backend.analyze.controller;

import com.lumi.backend.analyze.dto.AnalyzeRequest;
import com.lumi.backend.analyze.dto.AnalyzeResponse;
import com.lumi.backend.analyze.exceptions.code.AnalyzeSuccessCode;
import com.lumi.backend.analyze.service.AnalyzeService;
import com.lumi.backend.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analyze")
@RequiredArgsConstructor
public class AnalyzeController implements AnalyzeControllerDocs {

    private final AnalyzeService analyzeService;

    @Override
    @PostMapping
    public ApiResponse<AnalyzeResponse> analyzeLog(@Valid @RequestBody AnalyzeRequest request) {
        AnalyzeResponse response = analyzeService.analyzeLog(request);
        return ApiResponse.onSuccess(AnalyzeSuccessCode.ANALYZE_SUCCESS, response);
    }
}
