package com.eric.fileshare.config;

import com.eric.fileshare.interceptors.TrafficInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.eric.fileshare")
public class WebConfig implements WebMvcConfigurer {

    /*
    * 注册流量拦截器
    * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TrafficInterceptor(5.0 / 60D)).addPathPatterns("/upload");
        registry.addInterceptor(new TrafficInterceptor(1.0 / 60D)).addPathPatterns("/code");
        registry.addInterceptor(new TrafficInterceptor(5.0 / 60D)).addPathPatterns("/download");
    }
}
