package com.example.springbootshirojwt.controller;

import com.example.springbootshirojwt.service.TestTestService;
import com.example.springbootshirojwt.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestTestController {
    @Autowired
    private TestTestService testTestService;
    @GetMapping("/getAll")
    public R getAll(){
        return new R(testTestService.list());
    }
    @GetMapping("/getOne")
    public R getOne(){
        return new R(testTestService.getOne(1));
    }
}
