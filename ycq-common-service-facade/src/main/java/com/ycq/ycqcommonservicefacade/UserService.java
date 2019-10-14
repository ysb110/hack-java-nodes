package com.ycq.ycqcommonservicefacade;

import com.ycq.ycqcommonservicefacade.vo.LoginReq;
import com.ycq.ycqcommonservicefacade.vo.UserEntity;

public interface UserService {
    public UserEntity login(LoginReq loginReq);
}