package com.lumi.backend.global.apiPayload.code;

public interface BaseSuccessCode {

    String getCode();

    String getMessage();

    int getHttpStatus();
}
