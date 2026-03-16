package com.lumi.backend.analyze.dto;

import java.util.List;

public record AnalyzeResponse(
        String summary,
        List<String> errors,
        String suggestion
) {
}
