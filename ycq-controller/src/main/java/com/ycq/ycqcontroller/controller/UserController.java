package com.ycq.ycqcontroller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ycq.ycqcommonservicefacade.UserService;
import com.ycq.ycqcommonservicefacade.vo.LoginReq;
import com.ycq.ycqcommonservicefacade.vo.UserEntity;
import com.ycq.ycqcontroller.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference(version="1.0.0")
    private UserService userService;

    @GetMapping(value="login")
    public Result login(LoginReq loginReq, HttpServletRequest request) {
        UserEntity entity = userService.login(loginReq);
        Result result = new Result("","200", entity);
        return result;
    }
}
