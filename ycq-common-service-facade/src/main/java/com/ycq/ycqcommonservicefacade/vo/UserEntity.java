package com.ycq.ycqcommonservicefacade.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserEntity implements Serializable {
    private String username;
    private String pwd;
    private Integer age;
}