package com.tfy.framework.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tfy.framework.common.consts.GlobalConst;
import com.tfy.framework.common.exception.ApiErrorCode;
import com.tfy.framework.common.exception.IErrorCode;
import com.tfy.framework.common.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
        return vo;
    }

    public static Object ok(HttpServletRequest request, Object data) {
        ResponseVo<Object> vo = new ResponseVo<>();
        vo.setCode(ApiErrorCode.SUCCESS.getCode());
        vo.setData(data);
        return returnType(request, vo);
    }

    public static <T> ResponseVo<T> failed(String msg) {
        ResponseVo<T> vo = new ResponseVo<>();
        vo.setCode(ApiErrorCode.FAILED.getCode());
        vo.setMsg(msg);
        vo.setData(null);
        return vo;
    }

    public static <T> ResponseVo<T> failed(IErrorCode errorCode) {
        ResponseVo<T> vo = new ResponseVo<>();
        vo.setCode(errorCode.getCode());
        vo.setMsg(errorCode.getMsg());
        vo.setData(null);
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

    public static Object returnType(HttpServletRequest request, Object returnMsg) {
        String callback = request.getParameter("callback");

        if (StringUtils.isNotEmpty(callback)) {
            try {
                return callback + "(" + JSONObject.toJSONString(returnMsg) + ")";
            } catch (Exception e) {
                log.error("G.returnType:", e);
            }
        }
        return returnMsg;
    }

    public static String uuidExcludeStrip() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    public static void main(String[] args) {
    }
}
