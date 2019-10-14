package com.ycq.ycqcontroller.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class Index {
    @GetMapping(value="/")
    public String sayHello() {
        return "hello World";
    }
}
