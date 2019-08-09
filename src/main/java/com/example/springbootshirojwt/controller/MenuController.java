package com.example.springbootshirojwt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootshirojwt.dto.MenuTree;
import com.example.springbootshirojwt.dto.Permission;
import com.example.springbootshirojwt.model.SysMenu;
import com.example.springbootshirojwt.model.SysRole;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.service.SysMenuService;
import com.example.springbootshirojwt.service.SysRoleService;
import com.example.springbootshirojwt.service.SysUserService;
import com.example.springbootshirojwt.util.JWTUtil;
import com.example.springbootshirojwt.util.R;
import com.example.springbootshirojwt.util.TreeUtil;
import com.example.springbootshirojwt.vo.MenuVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据登陆人角色，获取所拥有的菜单，并生成菜单树(供生成siderbar菜单使用)
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_view")
    @GetMapping("/getMenu")
    public R getMenu(){
        //用Set避免不同角色相同菜单造成的重复
        Set<MenuVO> all=new HashSet<>();
        Subject subject = SecurityUtils.getSubject();
        String username = JWTUtil.getUsername(subject.getPrincipals().toString());
        SysUser user = sysUserService.getUserByUsername(username);
        List<SysRole> roles=sysRoleService.getRolesByUid(user.getUid());
        roles.forEach(role->{
            sysMenuService.getMenuByRoleId(role.getId()).forEach(sysMenu ->{
                if(sysMenu.getType().equals("0"))
                all.add(new MenuVO(sysMenu));
            });
        });
        List <MenuTree> menuTreeList=all.stream().map(MenuTree::new).sorted(Comparator.comparingInt(MenuTree::getSort)).collect(Collectors.toList());
        return new R(TreeUtil.bulid(menuTreeList, -1));
    }
    /**
     * 根据角色id，获取所拥有的菜单id集合
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_view")
    @GetMapping("/getMenuByRoleId/{id}")
    public R getMenuIdByRoleId(@PathVariable("id") Integer id){
        return new R(sysMenuService.getMenuByRoleId(id).stream().map(SysMenu::getId));
    }

    /**
     * 获取全部菜单,并生成菜单树(菜单管理的接口)
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_view")
    @GetMapping("/getMenuAll")
    public R getMenuAll(){
        return new R(TreeUtil.bulidTree(sysMenuService.list(Wrappers.emptyWrapper()), -1));
    }
    /**
     * 根据id获取某个菜单信息
     * @param id
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_view")
    @GetMapping("/{id}")
    public R getMenuById(@PathVariable("id") Integer id){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("id",id);
        return new R(new MenuTree(new MenuVO(sysMenuService.getOne(queryWrapper))));
    }

    /**
     * 添加一个菜单
     * @param sysMenu
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_add")
    @PostMapping
    public R saveOne(SysMenu sysMenu){
        sysMenu.setDelFlag("0");
        if(sysMenuService.save(sysMenu))
        return new R(200,"添加成功","");
        else return new R(200,"添加失败","");//如果数据库操作出现错误，抛出的异常会被统一处理，但是在有些情况比如更新和删除等时，给sql语句的条件
                                                                //设一个不存在的情况，此时数据库不会抛出异常,但相关crud方法返回false，这就是这个eles存在的原因
                                                               //不能光只靠统一异常处理来解决不合法的数据库操作

    }

    /**
     * 根据id删除菜单
     * @param id
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_del")
    @DeleteMapping("/{id}")
    public R deleteOne(@PathVariable("id") Integer id){
        if(sysMenuService.removeById(id))
        return new R(200,"删除成功","");
        else return new R(200,"删除失败","");
    }

    /**
     * 根据id修改菜单
     * @param sysMenu
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_update")
    @PutMapping
    @CacheEvict(value="MenuInfo",allEntries = true)
    public R putOne(SysMenu sysMenu){
        if(sysMenuService.updateById(sysMenu))
        return new R(200,"修改成功","");
        else return new R(200,"修改失败","");
    }
    /**
     * 获取所有权限
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("menu_view")
    @GetMapping("/getPermissionAll")
    public R getPermissionAll(){
        List<Permission> permissions=new ArrayList<>();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.isNotNull("permission");
        List<SysMenu> sysMenus=sysMenuService.list(queryWrapper);
        sysMenus.forEach(menu->{
            Permission p=new Permission();
            p.setId(menu.getId());
            p.setName(menu.getName());
            p.setPermission(menu.getPermission());
            permissions.add(p);
        });
        return new R(permissions);
    }
}
