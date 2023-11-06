package com.eric.fileshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        System.out.println("this = " + this);
        return "pong";
    }

}
