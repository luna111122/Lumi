package com.lumi.backend.analyze.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalyzeRequest {

    @NotBlank(message = "로그를 입력해주세요.")
    private final String log;
}
