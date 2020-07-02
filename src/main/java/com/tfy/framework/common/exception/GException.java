package com.tfy.framework.common.exception;

import lombok.Getter;

@Getter
public class GException extends RuntimeException {

    private long code = ApiErrorCode.FAILED.getCode();

    private String msg;

    public GException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public GException(IErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }
}
