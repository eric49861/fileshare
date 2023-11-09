package com.eric.fileshare.service;

import com.eric.fileshare.exceptions.upload.UploadException;
import com.eric.fileshare.util.EmailUtil;
import com.google.common.cache.LoadingCache;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class EmailService implements IEmailService{

    private static LoadingCache<String, String> codeCache;

    @Autowired
    public EmailService(@Qualifier("codeCache") LoadingCache<String, String> codeCache) {
        this.codeCache = codeCache;
    }
    /*
    * 发送验证码
    * 1. 生成随机验证码
    * 2. 将验证码添加到本地缓存
    * 3. 通过邮箱将验证码发送给用户
    * */
    public void sendCode(String to) throws EmailException, ExecutionException {
        String code = EmailUtil.randomCode();
        codeCache.put(to, code);
        System.out.println("[to = " + to  + " , value = " + codeCache.get(to) + "]");
        EmailUtil.sendEmail(code, to);
    }

    public void sendLink(String link, String to) throws EmailException {
        EmailUtil.sendLink(link, to);
    }

    public void checkCode(String code, String email) throws UploadException, ExecutionException {
        String s = codeCache.get(email);
        if(s == null) {
            throw new UploadException("验证码已失效");
        }else if (!code.equals(s)) {
            throw new UploadException("验证码不正确");
        }
    }
}
