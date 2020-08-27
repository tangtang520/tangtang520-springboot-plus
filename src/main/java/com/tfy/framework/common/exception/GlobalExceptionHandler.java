package com.tfy.framework.common.exception;

import com.tfy.framework.common.utils.G;
import com.tfy.framework.common.VO.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一错误处理器 只监听rest接口
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(GException.class)
    public ResponseVo<Object> handleBusiness(GException e) {
        String requestContent = request.getRequestURI() + "?" + request.getQueryString();
        log.error("exceptionHandle.GException url:{}, ", requestContent, e);
        return G.failed(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseVo<Object> handleSystem(Exception e) {
        String requestContent = request.getRequestURI() + "?" + request.getQueryString();
        log.error("exceptionHandle.Exception url:{}, ", requestContent, e);
        return G.failed(e.getMessage());
    }
}
