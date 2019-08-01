package com.example.springbootshirojwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootshirojwt.mapper.SysMenuMapper;
import com.example.springbootshirojwt.mapper.SysRoleMapper;
import com.example.springbootshirojwt.model.SysMenu;
import com.example.springbootshirojwt.model.SysRole;
import com.example.springbootshirojwt.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    /**
     * 根据用户id查询所拥有的角色
     * @param uId
     * @return
     */
    public List<SysRole> getRolesByUid(String uId){
        return sysRoleMapper.getRolesByUid(uId);
    }
}
