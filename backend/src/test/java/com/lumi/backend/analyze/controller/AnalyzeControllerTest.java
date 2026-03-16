package com.lumi.backend.analyze.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumi.backend.analyze.dto.AnalyzeRequest;
import com.lumi.backend.analyze.dto.AnalyzeResponse;
import com.lumi.backend.analyze.service.AnalyzeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyzeController.class)
class AnalyzeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnalyzeService analyzeService;

    @Test
    void analyzeLog_정상요청_200반환() throws Exception {
        // given
        AnalyzeRequest request = AnalyzeRequest.builder()
                .log("java.lang.NullPointerException at com.lumi.Service.java:42")
                .build();

        AnalyzeResponse response = AnalyzeResponse.builder()
                .summary("NullPointerException 발생")
                .errors(List.of("com.lumi.Service.java:42"))
                .suggestion("null 체크 추가 필요")
                .build();

        given(analyzeService.analyzeLog(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.summary").value("NullPointerException 발생"))
                .andExpect(jsonPath("$.result.errors[0]").value("com.lumi.Service.java:42"))
                .andExpect(jsonPath("$.result.suggestion").value("null 체크 추가 필요"));
    }

    @Test
    void analyzeLog_빈로그_400반환() throws Exception {
        // given
        AnalyzeRequest request = AnalyzeRequest.builder()
                .log("")
                .build();

        // when & then
        mockMvc.perform(post("/api/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400_1"))
                .andExpect(jsonPath("$.result.log[0]").value("로그를 입력해주세요."));
    }

    @Test
    void analyzeLog_null로그_400반환() throws Exception {
        // given
        String requestBody = "{\"log\": null}";

        // when & then
        mockMvc.perform(post("/api/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400_1"));
    }
}
