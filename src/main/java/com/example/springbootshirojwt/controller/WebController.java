package com.example.springbootshirojwt.controller;

import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.service.SysUserService;
import com.example.springbootshirojwt.util.EncryptionPassword;
import com.example.springbootshirojwt.util.JWTUtil;
import com.example.springbootshirojwt.util.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class WebController {
    @Autowired
    private SysUserService userService;
    //仅返回token，并没有认证，与JWTFilter里的executeLogin相区别，那个是验证是否已经认证的操作，并再次给予认证
    @PostMapping("/login")
    public R login(String username,String password,HttpServletRequest request){
        SysUser user=userService.getUserByUsername(username);
        if(user==null)return new R(500,"用户不存在","");
        String ePassword=EncryptionPassword.encryptionPassword("MD5",password,user.getCredentialsSalt(),2).toString();//数据库中存入的密码是经过md5算法加盐(密码+用户名)获得
        if (user.getPassword().equals(ePassword)) {
            return new R(200, "登录成功", JWTUtil.sign(username, ePassword));
        }
        else{
            //抛出自己创建的异常类
            throw new UnauthorizedException("用户名或密码错误");
        }
    }
    //所有人都可以访问，但是用户与游客看到的内容不同
    @GetMapping("/article")
    public R article() {
            Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new R(200, "You are already logged in", null);
        } else {
            return new R(200, "You are guest", null);
        }
    }
    //登录认证后的用户才可以进行访问
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public R requireAuth() {
        return new R(200, "You are authenticated", null);
    }
    //拥有admin的角色用户才可以访问
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public R requireRole() {
        return new R(200, "You are visiting require_role", null);
    }
    //拥有view和edit权限的用户才可以访问
    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public R requirePermission() {
        return new R(200, "You are visiting permission require edit,view", null);
    }
    //未登录认证，以下多个401请求映射路径,以适应不同请求方式
    @GetMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R unauthorized(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new R(401, "Unauthorized", null);
    }
    @PostMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R unauthorized2(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new R(401, "Unauthorized", null);
    }
    @PutMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R unauthorized3(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new R(401, "Unauthorized", null);
    }
    @DeleteMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R unauthorized4(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new R(401, "Unauthorized", null);
    }
}
