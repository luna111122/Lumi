package com.lumi.backend.fixture;

import com.lumi.backend.analyze.dto.AnalyzeRequest;
import com.lumi.backend.analyze.dto.AnalyzeResponse;

import java.util.List;

public class AnalyzeFixture {

    public static final String SAMPLE_LOG = "java.lang.NullPointerException at com.lumi.Service.java:42";

    public static AnalyzeRequest createRequest() {
        return AnalyzeRequest.builder()
                .log(SAMPLE_LOG)
                .build();
    }

    public static AnalyzeResponse createResponse() {
        return AnalyzeResponse.builder()
                .summary("NullPointerException 발생")
                .errors(List.of("com.lumi.Service.java:42"))
                .suggestion("null 체크 추가 필요")
                .build();
    }
}
