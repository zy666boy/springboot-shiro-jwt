package com.example.springbootshirojwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootshirojwt.model.SysRoleMenu;
import com.example.springbootshirojwt.util.R;

public interface SysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 根据角色id更新角色的菜单权限
     * @param id
     * @param menuIds
     * @return
     */
    boolean putRoleMenuByRoleId(Integer id, String menuIds);
}
