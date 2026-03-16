package com.lumi.backend.analyze.service;

import com.lumi.backend.analyze.client.AiClient;
import com.lumi.backend.analyze.dto.AnalyzeRequest;
import com.lumi.backend.analyze.dto.AnalyzeResponse;
import com.lumi.backend.analyze.exceptions.AnalyzeException;
import com.lumi.backend.analyze.exceptions.code.AnalyzeErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AnalyzeServiceImplTest {

    @Mock
    private AiClient aiClient;

    @InjectMocks
    private AnalyzeServiceImpl analyzeService;

    @Test
    void analyzeLog_정상요청_응답반환() {
        // given
        AnalyzeRequest request = AnalyzeRequest.builder()
                .log("java.lang.NullPointerException at com.lumi.Service.java:42")
                .build();

        AnalyzeResponse expected = AnalyzeResponse.builder()
                .summary("NullPointerException 발생")
                .errors(List.of("com.lumi.Service.java:42"))
                .suggestion("null 체크 추가 필요")
                .build();

        given(aiClient.analyzeLog(request.getLog())).willReturn(expected);

        // when
        AnalyzeResponse result = analyzeService.analyzeLog(request);

        // then
        assertThat(result.getSummary()).isEqualTo("NullPointerException 발생");
        assertThat(result.getErrors()).containsExactly("com.lumi.Service.java:42");
        assertThat(result.getSuggestion()).isEqualTo("null 체크 추가 필요");
    }

    @Test
    void analyzeLog_AiClient실패_예외발생() {
        // given
        AnalyzeRequest request = AnalyzeRequest.builder()
                .log("java.lang.NullPointerException at com.lumi.Service.java:42")
                .build();

        given(aiClient.analyzeLog(request.getLog()))
                .willThrow(new AnalyzeException(AnalyzeErrorCode.AI_SERVER_ERROR));

        // when & then
        assertThatThrownBy(() -> analyzeService.analyzeLog(request))
                .isInstanceOf(AnalyzeException.class)
                .hasMessage(AnalyzeErrorCode.AI_SERVER_ERROR.getMessage());
    }
}
