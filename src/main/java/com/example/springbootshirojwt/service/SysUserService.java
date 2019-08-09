package com.example.springbootshirojwt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootshirojwt.model.SysUser;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    /**
     * 通过用户名查用户详细信息
     * @param username
     * @return
     */
    SysUser getUserByUsername(String username);
    /**
     * 获取全部用户信息
     * @return
     */
    IPage<Map<String,Object>> getUserInfoAll(Page page,SysUser user);
}
