package com.eric.fileshare.interceptors;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eric.fileshare.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

public class UploadInterceptor implements HandlerInterceptor {


    /*
    * 检查请求头是否含有token
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        PrintWriter writer = null;
        if(token == null) {
            writer = response.getWriter();
            writer.write("用户身份验证失败");
            writer.flush();
            writer.close();
            return false;
        }
        String email = null;
        try {
            email = JWTUtil.parseToken(token);
        }catch(JWTVerificationException e) {
            writer = response.getWriter();
            writer.write("无效的token");
            writer.flush();
            return false;
        }finally {
            if(writer != null) {
                writer.close();
            }
        }
        request.setAttribute("email", email);
        return true;
    }
}
