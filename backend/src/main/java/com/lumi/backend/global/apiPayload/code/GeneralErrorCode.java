package com.lumi.backend.global.apiPayload.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {

    BAD_REQUEST("COMMON400_1", "잘못된 요청입니다.", 400),
    UNAUTHORIZED("COMMON401_1", "인증이 필요합니다.", 401),
    FORBIDDEN("COMMON403_1", "접근 권한이 없습니다.", 403),
    NOT_FOUND("COMMON404_1", "요청한 리소스를 찾을 수 없습니다.", 404),
    INTERNAL_SERVER_ERROR("COMMON500_1", "서버 내부 오류가 발생했습니다.", 500);

    private final String code;
    private final String message;
    private final int httpStatus;
}
