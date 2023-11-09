package com.eric.fileshare.interceptors;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

public class TrafficInterceptor implements HandlerInterceptor {

    private final Logger logger = Logger.getLogger(getClass());

    private RateLimiter rateLimiter;

    public TrafficInterceptor(double qps) {
        this.rateLimiter = RateLimiter.create(qps);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(rateLimiter.tryAcquire()) {
            logger.debug("拦截到[" + request.getRequestURI() + "]的请求");
            return true;
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF8");
        PrintWriter writer = null;
        try {
            logger.debug("[IP = " + request.getRemoteAddr() + "] 对" + request.getRequestURI() + "频繁发起请求");
            writer = response.getWriter();
            writer.write("请求过于频繁, 请稍后重试");
            writer.flush();
        }catch (IOException e) {
            logger.error("获取Writer失败, 失败的原因: " + e.getMessage());
        }finally {
            if (writer != null) {
                writer.close();
            }
        }
        return false;
    }
}
