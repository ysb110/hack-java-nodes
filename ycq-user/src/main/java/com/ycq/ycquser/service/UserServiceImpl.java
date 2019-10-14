package com.ycq.ycquser.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.ycq.ycqcommonservicefacade.UserService;
import com.ycq.ycqcommonservicefacade.vo.LoginReq;
import com.ycq.ycqcommonservicefacade.vo.UserEntity;

@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {
    @Override
    public UserEntity login(LoginReq loginReq) {
        return new UserEntity("ycq","pwd",28);
    }
}
