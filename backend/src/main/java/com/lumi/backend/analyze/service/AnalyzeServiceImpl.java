package com.lumi.backend.analyze.service;

import com.lumi.backend.analyze.client.AiClient;
import com.lumi.backend.analyze.dto.AnalyzeRequest;
import com.lumi.backend.analyze.dto.AnalyzeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzeServiceImpl implements AnalyzeService {

    private final AiClient aiClient;

    @Override
    public AnalyzeResponse analyzeLog(AnalyzeRequest request) {
        log.info("로그 분석 요청");
        return aiClient.analyzeLog(request.getLog());
    }
}
