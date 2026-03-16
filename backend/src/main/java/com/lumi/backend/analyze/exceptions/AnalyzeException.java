package com.lumi.backend.analyze.exceptions;

import com.lumi.backend.global.apiPayload.code.BaseErrorCode;
import com.lumi.backend.global.apiPayload.exception.GeneralException;

public class AnalyzeException extends GeneralException {

    public AnalyzeException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
