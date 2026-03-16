package com.lumi.backend.analyze.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AnalyzeResponse {

    private final String summary;
    private final List<String> errors;
    private final String suggestion;
}
