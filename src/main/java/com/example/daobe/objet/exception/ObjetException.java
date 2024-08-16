package com.example.daobe.objet.exception;

import com.example.daobe.common.exception.BaseException;

public class ObjetException extends BaseException {

    public ObjetException(ObjetExceptionType exceptionType) {
        super(exceptionType);
    }
}
