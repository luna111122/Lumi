package com.lumi.backend.analyze.exceptions.code;

import com.lumi.backend.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalyzeErrorCode implements BaseErrorCode {

    AI_SERVER_ERROR("ANALYZE500_1", "AI 서버 호출 중 오류가 발생했습니다.", 500),
    INVALID_LOG_FORMAT("ANALYZE400_1", "로그 형식이 올바르지 않습니다.", 400);

    private final String code;
    private final String message;
    private final int httpStatus;
}
