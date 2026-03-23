package com.lumi.backend.analyze.client;

import com.lumi.backend.analyze.client.dto.AiAnalyzeRequest;
import com.lumi.backend.analyze.client.dto.AiAnalyzeResponse;
import com.lumi.backend.analyze.dto.AnalyzeResponse;
import com.lumi.backend.analyze.exceptions.AnalyzeException;
import com.lumi.backend.analyze.exceptions.code.AnalyzeErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiClientImpl implements AiClient {

    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    @Override
    public AnalyzeResponse analyzeLog(String log) {
        AiAnalyzeRequest request = AiAnalyzeRequest.builder()
                .log(log)
                .build();

        AiAnalyzeResponse response = webClient.post()
                .uri(UriComponentsBuilder.fromHttpUrl(aiServerUrl).pathSegment("analyze").toUriString())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiAnalyzeResponse.class)
                .doOnError(e -> log.error("AI 서버 호출 실패: {}", e.getClass().getSimpleName()))
                .onErrorMap(e -> new AnalyzeException(AnalyzeErrorCode.AI_SERVER_ERROR))
                .block();

        if (response == null) {
            throw new AnalyzeException(AnalyzeErrorCode.AI_SERVER_ERROR);
        }

        return AnalyzeResponse.builder()
                .summary(response.getSummary())
                .errors(response.getErrors())
                .suggestion(response.getSuggestion())
                .build();
    }
}
