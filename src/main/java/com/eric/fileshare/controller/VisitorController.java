package com.eric.fileshare.controller;

import com.eric.fileshare.service.IVisitorService;
import com.eric.fileshare.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    private IVisitorService visitorService;

    @Autowired
    public VisitorController(IVisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/spacesize")
    public Result<Long> getBalance(HttpServletRequest request) {
        String ip = (String) request.getAttribute("ip");
        long balance = visitorService.getBalance(ip);
        return Result.success(balance);
    }

}
