package com.example.springbootshirojwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.model.TestTest;

public interface TestTestService extends IService<SysUser>{
        /**
         * 通过用户名查用户详细信息
         * @param username
         * @return
         */
        SysUser getUserByUsername(String username);
}
