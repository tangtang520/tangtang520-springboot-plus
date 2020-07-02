package com.tfy.framework.configuration;

import com.google.common.collect.Lists;
import com.tfy.framework.service.security.uid.UidInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private List<String> excludeUrlList = Lists.newArrayList();

    @PostConstruct
    private void init() {
        // todo 在这里写排序的url
        // excludeUrlList.add("xxxx");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new UidInterceptor())
                .excludePathPatterns("/static/*")
                .excludePathPatterns("/error")
                .addPathPatterns("/**");
    }
}
