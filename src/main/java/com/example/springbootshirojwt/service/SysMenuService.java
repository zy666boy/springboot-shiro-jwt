package com.example.springbootshirojwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootshirojwt.dto.MenuTree;
import com.example.springbootshirojwt.dto.Permission;
import com.example.springbootshirojwt.model.SysMenu;
import com.example.springbootshirojwt.vo.MenuVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    /**
     * 根据角色id获取权限集合
     * @param roleId
     * @return
     */
    List<Permission> getPermisssionByRoleId(Integer roleId);

    /**
     * 根据角色id获取菜单
     * @param id
     * @return
     */
    List<SysMenu> getMenuByRoleId(Integer id);
}
