package com.lumi.backend.analyze.exceptions.code;

import com.lumi.backend.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalyzeSuccessCode implements BaseSuccessCode {

    ANALYZE_SUCCESS("ANALYZE200_1", "로그 분석이 완료되었습니다.", 200);

    private final String code;
    private final String message;
    private final int httpStatus;
}
