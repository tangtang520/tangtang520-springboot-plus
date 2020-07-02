package com.tfy.framework.common.exception;

import com.tfy.framework.common.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一错误处理器 只监听rest接口
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GException.class)
    public ResponseVo<Object> handleBusiness(GException e) {
        ResponseVo<Object> vo = new ResponseVo<>();
        vo.setCode(e.getCode());
        vo.setMsg(e.getMsg());
        vo.setData(null);
        return vo;
    }

    @ExceptionHandler(Exception.class)
    public ResponseVo<Object> handleSystem(Exception e) {
        log.error("global.exception.system.e", e);
        ResponseVo<Object> vo = new ResponseVo<>();
        vo.setCode(ApiErrorCode.FAILED.getCode());
        vo.setMsg(e.getMessage());
        vo.setData(null);
        return vo;
    }
}
