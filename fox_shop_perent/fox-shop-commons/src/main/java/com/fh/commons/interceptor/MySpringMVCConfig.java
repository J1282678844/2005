package com.fh.commons.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @ClassName MySpringMVCConfig
 * @Deacription TODO
 * @Author 阳
 * @Date 2020/12/1 19:10
 * @Version 1.0
 **/
@Configuration
public class MySpringMVCConfig extends WebMvcConfigurationSupport {

    @Resource
    private CrosHandlerInterceptor crosHandlerInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crosHandlerInterceptor).addPathPatterns("/**");
    }
}
