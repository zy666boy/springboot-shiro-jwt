package com.example.springbootshirojwt.controller;

import com.example.springbootshirojwt.mapper.SysUserMapper;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.model.TestTest;
import com.example.springbootshirojwt.service.SysUserService;
import com.example.springbootshirojwt.service.TestTestService;
import com.example.springbootshirojwt.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestTestController {
    @Autowired
    private TestTestService testTestService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserService sysUser2Service;
    @Autowired
    private RedisUtils redisUtils;
    @GetMapping("/getOne")
    public SysUser getOne(){
        return userService.getUserByUsername("admin");
    }
    @Cacheable(value="cache2",key = "targetClass.getName()+'.'+methodName")
    @GetMapping("/getOne2")
    public TestTest getOne2(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        TestTest testTest=new TestTest();
        testTest.setTestName("lyk");
        testTest.setTest_id(1);
        return testTest;
    }
    @GetMapping("/getOne3")
    public SysUser getOne3(){
        return sysUserService.getUserByUsername("admin");
    }
    @GetMapping("/delCache")
    @CacheEvict(value="UserInfo",key="'com.example.springbootshirojwt.service.impl.SysUserServiceImpl:getUserByUsername:'+#sysUser.username")
    public String deleteCache(SysUser sysUser){
        Object b=redisUtils.get("deleteCache");
        System.out.println(b);
       return "123";
    }
}
