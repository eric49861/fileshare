package com.eric.fileshare.config;

import com.eric.fileshare.interceptors.UploadInterceptor;
import com.eric.fileshare.interceptors.VisitorInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@Configuration
@EnableWebMvc
@ComponentScan("com.eric.fileshare")
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VisitorInterceptor()).addPathPatterns("/visitor/upload");

        registry.addInterceptor(new UploadInterceptor()).addPathPatterns("/upload", "/spacesize");
    }
}
