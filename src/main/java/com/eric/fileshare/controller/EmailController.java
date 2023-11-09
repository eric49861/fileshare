package com.eric.fileshare.controller;

import com.eric.fileshare.service.IEmailService;
import com.eric.fileshare.util.Result;
import jakarta.validation.constraints.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class EmailController {
    private final Logger logger = Logger.getLogger(getClass());

    private IEmailService emailService;

    public EmailController(){}

    @Autowired
    public EmailController(IEmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping("/code")
    public Result<String> EmailCode(@RequestParam("email") String email) {
        try {
            emailService.sendCode(email);
        }catch (EmailException | ExecutionException e) {
            logger.error("验证码发送失败, 失败的原因: " + e.getMessage());
            return Result.fail("验证码获取失败，请稍后重试");
        }
        return Result.success("验证码已发送到您的邮箱，请注意查收");
    }
}
