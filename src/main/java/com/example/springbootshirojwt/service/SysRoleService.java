package com.example.springbootshirojwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootshirojwt.model.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    /**
     * 根据用户id查询所拥有的角色
     * @param uId
     * @return
     */
   List<SysRole> getRolesByUid(String uId);
}
