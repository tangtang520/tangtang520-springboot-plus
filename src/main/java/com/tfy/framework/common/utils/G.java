package com.tfy.framework.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tfy.framework.common.consts.GlobalConst;
import com.tfy.framework.common.exception.ApiErrorCode;
import com.tfy.framework.common.exception.IErrorCode;
import com.tfy.framework.common.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * 公共的方法放在这里
 */
@Slf4j
public class G {

    /*正确的返回结果*/
    public static <T> ResponseVo<T> ok(T data) {
        ResponseVo<T> vo = new ResponseVo<>();
        vo.setCode(ApiErrorCode.SUCCESS.getCode());
        vo.setData(data);
        vo.setSerialNumber(MDC.get(GlobalConst.SERIAL_NUMBER_KEY));
        return vo;
    }

    public static <T> ResponseVo<T> failed(String msg) {
        return failed(ApiErrorCode.FAILED.getCode(), msg);
    }

    public static <T> ResponseVo<T> failed(IErrorCode errorCode) {
        return failed(errorCode.getCode(), errorCode.getMsg());
    }


    public static <T> ResponseVo<T> failed(long code, String msg) {
        ResponseVo<T> vo = new ResponseVo<>();
        vo.setCode(code);
        vo.setMsg(msg);
        vo.setData(null);
        vo.setSerialNumber(MDC.get(GlobalConst.SERIAL_NUMBER_KEY));
        return vo;
    }


    public static <T> T parseObject(String text, Class<T> cls) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return JSON.parseObject(text, cls);
        } catch (Exception ex) {
        }
        return null;
    }

    public static <T> List<T> parseArray(String text, Class<T> cls) {
        try {
            return JSON.parseArray(text, cls);
        } catch (Exception ex) {
        }
        return null;
    }

    public static <T> T convertType(Object obj, Class<T> cls) {
        return parseObject(JSONObject.toJSONString(obj), cls);
    }


    /**
     * sql count int to bool
     */
    public static boolean retBool(int count) {
        return count > GlobalConst.ZERO;
    }

    public static String uuidExcludeStrip() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    public static void main(String[] args) {
    }
}
