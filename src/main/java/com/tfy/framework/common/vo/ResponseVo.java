package com.tfy.framework.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ResponseVo<T> implements Serializable {
    /*业务错误码*/
    private long code;

    /*数据*/
    private T data;

    /*错误信息*/
    private String msg;
}