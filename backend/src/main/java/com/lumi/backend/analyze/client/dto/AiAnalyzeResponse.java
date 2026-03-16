package com.lumi.backend.analyze.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AiAnalyzeResponse {

    private String summary;
    private List<String> errors;
    private String suggestion;
}
