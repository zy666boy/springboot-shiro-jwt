package com.example.springbootshirojwt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootshirojwt.mapper.SysRoleMapper;
import com.example.springbootshirojwt.mapper.SysUserMapper;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.service.SysRoleService;
import com.example.springbootshirojwt.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    //为了能够统一缓存管理，使用SysRoleService，而不用SysRoleMapper
    @Autowired
    private SysRoleService sysRoleService;
    /**
     * 通过用户名查用户详细信息
     * @param username
     * @return
     */
    @Cacheable(value="UserInfo",keyGenerator = "keyGenerator")
    public SysUser getUserByUsername(String username){
        return sysUserMapper.getUserByUsername(username);
    }
    /**
     * 获取全部用户信息
     * @return
     */
    public IPage<Map<String,Object>> getUserInfoAll(Page page,SysUser user){
        IPage<Map<String,Object>> iPage=sysUserMapper.getUserAll(page,user);
        iPage.getRecords().forEach(map->{
            List<String> rolesStr=new ArrayList();
            sysRoleService.getRolesByUid((String)map.get("uid")).forEach(role->{
                rolesStr.add(role.getRole());
            });
            map.put("roles",rolesStr);
        });
        return iPage;
    }
}
