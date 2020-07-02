package com.tfy.framework.service.security.uid;

import com.tfy.framework.common.consts.BloomConst;
import com.tfy.framework.common.utils.ParamsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UidInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireUidFilter classAnn = handlerMethod.getMethod().getDeclaringClass().getDeclaredAnnotation(RequireUidFilter.class);
        if (ParamsUtils.isNotEmpty(classAnn)) {
            return this.doFilter(request);
        }
        RequireUidFilter methodAnn = handlerMethod.getMethod().getDeclaredAnnotation(RequireUidFilter.class);
        if (ParamsUtils.isNotEmpty(methodAnn)) {
            return this.doFilter(request);
        }
        return true;
    }

    private boolean doFilter(HttpServletRequest request) {
        String uid = request.getParameter("uid");
        if (ParamsUtils.isEmpty(uid)) {
            return false;
        }
        return BloomConst.dealIdBloomFilter.mightContain(Integer.valueOf(uid));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
