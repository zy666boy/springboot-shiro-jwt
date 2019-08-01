package com.example.springbootshirojwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.model.TestTest;

public interface TestTestService extends IService<TestTest> {
        TestTest getOne(Integer id);
}
