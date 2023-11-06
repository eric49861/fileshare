package com.eric.fileshare.controller;

import com.eric.fileshare.service.IEmailService;
import com.eric.fileshare.util.Result;
import jakarta.validation.constraints.Email;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private IEmailService emailService;

    public EmailController(){}

    @Autowired
    public EmailController(IEmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping("/code")
    public Result<String> EmailCode(@RequestParam @Email String email) {
        try {
            emailService.sendCode(email);
        }catch (EmailException e) {
            // todo： 打印日志
            return Result.fail("验证码获取失败，请稍后重试");
        }
        return Result.success("验证码已发送到您的邮箱，请注意查收");
    }
}
