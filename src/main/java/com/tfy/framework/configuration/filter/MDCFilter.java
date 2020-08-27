package com.tfy.framework.configuration.filter;

import com.tfy.framework.common.consts.GlobalConst;
import com.tfy.framework.common.utils.G;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(value = 1)
@WebFilter("/*")
public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        MDC.put(GlobalConst.SERIAL_NUMBER_KEY, G.uuidExcludeStrip());
        super.doFilter(request, response, chain);
        MDC.clear();
    }
}