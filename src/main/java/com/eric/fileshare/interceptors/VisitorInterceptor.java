package com.eric.fileshare.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class VisitorInterceptor implements HandlerInterceptor {


    public final int MAX_SIZE = 50 * 1024 * 1024;// 50M
    /*
    * 游客文件上传的拦截器
    * 1. 检查文件的大小是否符合要求(spring6中暂时还不清楚怎么判断是否是MultipartHttpServletRequest)
    * 2. 获取游客的ip地址
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取游客的ip
        String remoteAddr = request.getRemoteAddr();
        request.setAttribute("ip", remoteAddr);
        System.out.println("remoteAddr = " + remoteAddr);
        return true;
    }
}
