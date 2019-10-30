package com.ycq.ycqcontroller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ycq.ycqcommonservicefacade.UserService;
import com.ycq.ycqcommonservicefacade.vo.LoginReq;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    @Reference(version="1.0.0")
    private UserService userService;

    @GetMapping(value = "/index")
    public String login(LoginReq loginReq, HttpServletRequest request) {
        userService.login(loginReq);
        //要访问的相对网址或绝对网址?参数名="+参数值
        return "redirect:/";
    }
}
