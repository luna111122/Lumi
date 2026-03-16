package com.lumi.backend.analyze.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiAnalyzeRequest {

    private final String log;
}
