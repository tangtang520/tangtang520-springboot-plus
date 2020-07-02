package com.tfy.framework.common.exception;

public enum ApiErrorCode implements IErrorCode {
    /*成功*/
    SUCCESS(0, "成功"),
    /*失败*/
    FAILED(-1, "失败");

    private final long code;
    private final String msg;

    ApiErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
