package com.lumi.backend.global.apiPayload.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneralSuccessCode implements BaseSuccessCode {

    OK("COMMON200_1", "요청이 성공적으로 처리되었습니다.", 200),
    CREATED("COMMON201_1", "리소스가 성공적으로 생성되었습니다.", 201),
    ACCEPTED("COMMON202_1", "요청이 수락되었습니다.", 202),
    NO_CONTENT("COMMON204_1", "처리가 완료되었습니다.", 204);

    private final String code;
    private final String message;
    private final int httpStatus;
}
