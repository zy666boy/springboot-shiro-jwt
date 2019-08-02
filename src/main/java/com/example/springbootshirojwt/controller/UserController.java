package com.example.springbootshirojwt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootshirojwt.dto.Permission;
import com.example.springbootshirojwt.dto.UserInfo;
import com.example.springbootshirojwt.model.SysRole;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.model.SysUserRole;
import com.example.springbootshirojwt.service.*;
import com.example.springbootshirojwt.util.EncryptionPassword;
import com.example.springbootshirojwt.util.JWTUtil;
import com.example.springbootshirojwt.util.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取当前登录用户的所有信息
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("user_view")
    @GetMapping("/getUserInfo")
    public R getUserInfo(){
        Subject subject = SecurityUtils.getSubject();
        String username = JWTUtil.getUsername(subject.getPrincipals().toString());
        SysUser user = sysUserService.getUserByUsername(username);
        UserInfo userInfo=new UserInfo();
        List<SysRole> roles=sysRoleService.getRolesByUid(user.getUid());
        List<String>rolesStr=new ArrayList<>();
        roles.forEach(role->rolesStr.add(role.getRole()));
        List<Permission> permissions=new ArrayList<>();
       roles.forEach(role->permissions.addAll(sysMenuService.getPermisssionByRoleId(role.getId())));
        List<String>permissionsStr=new ArrayList<>();
        permissions.forEach(permission->permissionsStr.add(permission.getPermission()));
        Map<String,Object> map=new HashMap<>();
        userInfo.setName(user.getName());
        userInfo.setUserName(user.getUsername());
        map.put("userInfo",userInfo);
        map.put("roles",rolesStr);
        map.put("permission",permissionsStr);
        return new R(map);
    }

    /**
     * 获取所有用户的信息
     * @param page
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("user_view")
    @GetMapping("/getUserInfoAll")
    public R getUserInfoAll(Page page,SysUser user){
        return new R(sysUserService.getUserInfoAll(page,user));
    }

    /**
     * 添加一个用户
     * @param sysUser
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("user_add")
    @PostMapping
    public R saveOne(SysUser sysUser){
        sysUser.setDelFlag("0");
        String salt=new StringBuffer(sysUser.getUsername()).reverse().toString();
        sysUser.setSalt(salt);
        String ePassword=EncryptionPassword.encryptionPassword("MD5",sysUser.getPassword(),sysUser.getPassword(),2).toString();
        sysUser.setPassword(ePassword);
        sysUser.setCreateTime(LocalDateTime.now());
        if(sysUserService.save(sysUser))
        return new R(200,"添加成功","");
        else return new R(200,"添加失败","");
    }

    /**
     * 根据id修改用户信息,但是这里有个缺陷就是因为给密码加密时的盐是根据用户名+用户名的反转，所以一旦修改了用户名或者密码都要进行重新修改数据库里的加密密码
     * 因为采用的加密算法是md5不可逆，所以修改用户名时，必须提供一个任意的密码。或者前端设置不能修改用户名。
     * @param sysUser
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("user_update")
    @PutMapping
    public R putOne(SysUser sysUser){
        sysUser.setUpdateTime(LocalDateTime.now());
        String oldUsername=sysUserService.getById(sysUser.getUid()).getUsername();
        String password=sysUser.getPassword();
        if(password!=null&&!password.equals("")||!oldUsername.equals(sysUser.getUsername())){
            String salt=new StringBuffer(sysUser.getUsername()).reverse().toString();
            sysUser.setSalt(salt);
            String ePassword=EncryptionPassword.encryptionPassword("MD5",sysUser.getPassword(),sysUser.getPassword(),2).toString();
            sysUser.setPassword(ePassword);
        }
        if(sysUserService.updateById(sysUser))
        return new R(200,"修改成功","");
        else return new R(200,"修改失败","");
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("user_del")
    @DeleteMapping("/{id}")
    public R deleteOne(@PathVariable("id") String id){
        if(sysUserService.removeById(id))
        return new R(200,"删除成功","");
        else return new R(200,"删除失败","");
    }
    @RequiresAuthentication
    @RequiresPermissions("user_view")
    @GetMapping("/getAllUser")
    public R getAllUser(){
        return new R(sysUserService.list());
    }
    @RequiresAuthentication
    @RequiresPermissions("user_update")
    @PutMapping("/putUserRole")
    @Transactional
    public R updateUserRole(String uid,String roleIds){
        QueryWrapper<SysUserRole> wrapper=new QueryWrapper<>();
        wrapper.eq("uid",uid);
        sysUserRoleService.remove(wrapper);
        String[] roleArr=roleIds.substring(1).split(",");
        for(String roleId :roleArr){
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setUid(uid);
            sysUserRole.setRoleId(Integer.parseInt(roleId));
            sysUserRole.setDelFlag("0");
            sysUserRole.setUpdateTime(LocalDateTime.now());
            sysUserRoleService.save(sysUserRole);
        }
        SysUser sysUser=new SysUser();
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setUid(uid);
        sysUserService.updateById(sysUser);
        return new R(200,"修改成功","");
    }
}
