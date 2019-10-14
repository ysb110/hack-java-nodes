package com.ycq.ycqcontroller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private String msg;
    private String code;
    private T data;
}
