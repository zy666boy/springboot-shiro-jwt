package com.example.springbootshirojwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootshirojwt.model.SysRole;
import com.example.springbootshirojwt.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户名查用户详细信息
     * @param username
     * @return
     */
    @Select("SELECT * FROM sys_user where username=#{username}")
    public SysUser getUserByUsername(String username);
    /**
     * 获取全部用户
     * @return
     */
    IPage<Map<String,Object>> getUserAll(Page page,@Param("user")SysUser user);
}
