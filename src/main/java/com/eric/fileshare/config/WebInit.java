package com.eric.fileshare.config;
//该类用于代替web.xml

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    private final Long MAX_SIZE = 50 * 1024 * 1024 * 1024L;
    private final int FILE_THRESH_SIZE = (int) (MAX_SIZE / 2);

    // 获取根配置文件，即spring的配置，可以用于整合
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    // SpringMVC的配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    // 配置servlet的映射
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement("", MAX_SIZE, MAX_SIZE, FILE_THRESH_SIZE));
    }
}
