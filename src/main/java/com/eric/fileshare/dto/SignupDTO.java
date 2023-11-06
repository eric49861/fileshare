package com.eric.fileshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @JsonProperty(value = "email")
    private String email;


    @NotBlank(message = "密码不能为空")
    @Max(value = 16, message = "密码长度不能超过16")
    @Min(value = 8, message = "密码不能少于8位")
    @JsonProperty(value = "password")
    private String password;

    @Min(value = 6, message = "验证码长度为6")
    @Max(value = 6, message = "验证码长度为6")
    @JsonProperty(value = "verifyCode")
    private String verifyCode;
}
