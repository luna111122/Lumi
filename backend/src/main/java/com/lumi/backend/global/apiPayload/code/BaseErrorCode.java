package com.lumi.backend.global.apiPayload.code;

public interface BaseErrorCode {

    String getCode();

    String getMessage();

    int getHttpStatus();
}
