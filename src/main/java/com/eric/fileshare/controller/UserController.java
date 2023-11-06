package com.eric.fileshare.controller;

import com.eric.fileshare.beans.User;
import com.eric.fileshare.dto.LoginDTO;
import com.eric.fileshare.dto.SignupDTO;
import com.eric.fileshare.exceptions.login.LoginException;
import com.eric.fileshare.exceptions.signup.SignupException;
import com.eric.fileshare.service.IUserService;
import com.eric.fileshare.util.JWTUtil;
import com.eric.fileshare.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private IUserService userService;
    public UserController(){}

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        User user = null;
        try {
            user = userService.doLogin(loginDTO);
        }catch(LoginException e) {
            return Result.fail(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), e.getMessage());
        }
        // 为该用户生成token
        String token = JWTUtil.getToken(user.getId(), user.getEmail());
        return Result.success(token);
    }

    @PostMapping("/signup")
    public Result<String> signup(@RequestBody SignupDTO signupDTO) {
        System.out.println(signupDTO.toString());
        try {
            userService.doSignup(signupDTO);
        } catch (SignupException e) {
            return Result.fail(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), e.getMessage());
        }
        return Result.success(signupDTO.getEmail());
    }

    @GetMapping("/spacesize")
    public Result<Long> getBalance(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        long balance = userService.getBalance(email);
        return Result.success(balance);
    }



//    该功能通过前端直接清除请求头中的token即可
//    @GetMapping("/logout")
//    public Result<LogoutDTO> Logout() {
//        return null;
//    }

}
