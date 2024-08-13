package com.example.daobe.upload.exception;

import com.example.daobe.common.exception.BaseException;

public class UploadException extends BaseException {

    public UploadException(UploadExceptionType exceptionType) {
        super(exceptionType);
    }
}
