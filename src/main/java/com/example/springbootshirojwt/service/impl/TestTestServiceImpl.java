package com.example.springbootshirojwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootshirojwt.mapper.SysUserMapper;
import com.example.springbootshirojwt.mapper.TestTestMapper;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.model.TestTest;
import com.example.springbootshirojwt.service.TestTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestTestServiceImpl extends ServiceImpl<TestTestMapper, TestTest> implements TestTestService {
    @Autowired
    private TestTestMapper testTestMapper;
    @Override
    public TestTest getOne(Integer id) {
        return testTestMapper.getOne(id);
    }
}
