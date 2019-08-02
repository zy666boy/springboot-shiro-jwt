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
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
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
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken jwtToken) throws AuthenticationException{
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
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
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
