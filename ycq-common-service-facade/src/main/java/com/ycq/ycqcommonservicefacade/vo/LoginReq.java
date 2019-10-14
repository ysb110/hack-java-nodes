package com.ycq.ycqcommonservicefacade.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginReq implements Serializable {
    private String username;
    private String pwd;
    private Integer age;
}
