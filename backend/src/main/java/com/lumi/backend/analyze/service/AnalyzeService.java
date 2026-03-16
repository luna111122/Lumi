package com.lumi.backend.analyze.service;

import com.lumi.backend.analyze.dto.AnalyzeRequest;
import com.lumi.backend.analyze.dto.AnalyzeResponse;

public interface AnalyzeService {

    AnalyzeResponse analyzeLog(AnalyzeRequest request);
}
