package com.lumi.backend.analyze.client;

import com.lumi.backend.analyze.dto.AnalyzeResponse;

public interface AiClient {

    AnalyzeResponse analyzeLog(String log);
}
