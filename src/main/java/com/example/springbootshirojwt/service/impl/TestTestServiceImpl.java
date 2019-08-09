package com.example.springbootshirojwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootshirojwt.mapper.SysUserMapper;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.service.TestTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestTestServiceImpl  extends ServiceImpl<SysUserMapper, SysUser> implements TestTestService {
    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 通过用户名查用户详细信息
     * @param username
     * @return
     */
    //@Cacheable(value="cache1",keyGenerator = "keyGenerator")
    public SysUser getUserByUsername(String username){
        return sysUserMapper.getUserByUsername(username);
    }
}
