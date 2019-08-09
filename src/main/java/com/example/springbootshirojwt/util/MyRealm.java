package com.example.springbootshirojwt.util;
import com.example.springbootshirojwt.dto.Permission;
import com.example.springbootshirojwt.model.SysRole;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.service.SysMenuService;
import com.example.springbootshirojwt.service.SysRoleService;
import com.example.springbootshirojwt.service.SysUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Spring Boot整合shiro后导致@Cacheable、@Transactional等注解失效的问题(在realm里自动注入的service的实例,
 * 会导致在容器中的整个serviceBean内部的@Cacheable、@Transactional等注解等注解失效，以至于若是在其他地方自动注入此service bean,
 * 内部的注解同样无效)
 * 解决：
 * 在realm自动注入service的地方，加上@Lazy注解,延迟自动注入。(@Lazy有两种用法一种是在@Component,@Bean等注解上使用，
 * 使此bean延迟加载进入spring容器，正常情况是spring容器启动时，就加载此bean,用了之后是当此bean在第一次调用的时候才会被加载进spring容器，
 * 作用主要是减少springIOC容器启动的加载时间。另一种就是用在自动注入注解上，使该bean被延迟注入)
 */
@Component
public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);
    @Autowired
    @Lazy
    private SysUserService userService;
    @Autowired
    @Lazy
    private SysRoleService roleService;
    @Autowired
    @Lazy
    private SysMenuService menuService;
    /**
     * 重写 Realm 的 supports() 方法是通过 JWT 进行登录判断的关键
     * 因为前文中创建了 JWTToken 用于替换 Shiro 原生 token
     * 所以必须在此方法中显式的进行替换，否则在进行判断时会一直失败
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
    /**
     * 判断用户是否已登录认证，若是并给予认证许可(认证的有效期为本次访问某接口的过程内，
     * 不像之前单独的shiro那样，认证一次只要不登出，在不超过过期时常内都一直有效,所以每访问一次需要认证的接口都需要需要重复认证),抛出的错误会被全局异常处理统一处理。
     */
    @Cacheable(value="authenticationInfo",key = "#jwtToken.credentials")//jwtToken.credentials调用的是getCredentials(),返回的是tokenString,见自定义类JWTToken
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken jwtToken) throws AuthenticationException{
        System.out.println("MyRealm.doGetAuthenticationInfo()");
        String tokenString = (String) jwtToken.getCredentials();
        // 解码获得username
        String username = JWTUtil.getUsername(tokenString);
        //判断token是否失效
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }
        //从数据库获取user实体
        SysUser user = userService.getUserByUsername(username);
        if (user == null) {
                throw new AuthenticationException("User didn't existed!");
        }
        //判断用户是否已登录认证
        if (!JWTUtil.verify(tokenString, username,user.getPassword())) {
                throw new AuthenticationException("Username or password error");
        }
        //构造函数参数为：SimpleAuthenticationInfo(Object principal,Object credentials,String realmName)，
        // 内部会获取前端传来的密码（登录时传入的JWT类型的jwtToken,jwtToken.getCredentials()），与数据库中存储的密码(此时为tokenString)进行比较。
        // 其实这是一个恒等式，因为到了此步已经能验证用户为已登录认证用户了
        return new SimpleAuthenticationInfo(tokenString, tokenString, "my_realm");
    }
    /**
     * 只有当需要检测用户权限的时候才会调用此方法
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("MyRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String username = JWTUtil.getUsername(principals.toString());
        SysUser user = userService.getUserByUsername(username);
        Set<Permission> permissions=new HashSet<>();
        List<SysRole> roles=roleService.getRolesByUid(user.getUid());
        for(SysRole role:roles){
            simpleAuthorizationInfo.addRole(role.getRole());
            permissions.addAll(menuService.getPermisssionByRoleId(role.getId()));
            for(Permission permission:permissions){
                simpleAuthorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return simpleAuthorizationInfo;
    }
}
