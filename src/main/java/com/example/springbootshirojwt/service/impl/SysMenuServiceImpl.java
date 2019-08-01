package com.example.springbootshirojwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootshirojwt.dto.MenuTree;
import com.example.springbootshirojwt.dto.Permission;
import com.example.springbootshirojwt.mapper.SysMenuMapper;
import com.example.springbootshirojwt.mapper.SysRoleMapper;
import com.example.springbootshirojwt.model.SysMenu;
import com.example.springbootshirojwt.model.SysRole;
import com.example.springbootshirojwt.service.SysMenuService;
import com.example.springbootshirojwt.util.TreeUtil;
import com.example.springbootshirojwt.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    /**
     * 根据角色id获取权限集合
     * @param roleId
     * @return
     */
    public List<Permission> getPermisssionByRoleId(Integer roleId){
        return sysMenuMapper.getPermisssionByRoleId(roleId);
    }
    /**
     * 根据用户id获取菜单
     * @param id
     * @return
     */
    public List<SysMenu> getMenuByRoleId(Integer id){
       //sysMenuMapper.getMenuByRoleId(id).stream().map(MenuTree::new).sorted(Comparator.comparingInt(MenuTree::getSort)).collect(Collectors.toList());
        return sysMenuMapper.getMenuByRoleId(id);
    }
}
