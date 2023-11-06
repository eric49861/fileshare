package com.eric.fileshare.service;

import com.eric.fileshare.util.EmailUtil;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class EmailService implements IEmailService{

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public EmailService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    /*
    * 发送验证码
    * 1. 生成随机验证码
    * 2. 将验证码添加到缓存
    * 3. 通过邮箱将验证码发送给用户
    * */
    public void sendCode(String to) throws EmailException {
        String code = EmailUtil.randomCode();
        redisTemplate.opsForValue().set(to, code, Duration.ofMinutes(EmailUtil.TIME));
        EmailUtil.sendEmail(code, to);
    }

    public void sendLink(String link, String to) throws EmailException {
        EmailUtil.sendLink(link, to);
    }
}
