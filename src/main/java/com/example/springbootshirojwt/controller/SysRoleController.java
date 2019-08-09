package com.example.springbootshirojwt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootshirojwt.model.SysRole;
import com.example.springbootshirojwt.service.SysRoleMenuService;
import com.example.springbootshirojwt.service.SysRoleService;
import com.example.springbootshirojwt.util.R;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @RequiresAuthentication
    @RequiresPermissions("role_view")
    @RequestMapping("/pageRole")
    public R pageRole(Page page){
        return new R(sysRoleService.page(page));
    }
    /**
     * 获取所有角色
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("role_view")
    @RequestMapping("/getRoleAll")
    public R getRoleAll(){
        return new R(sysRoleService.list());
    }

    /**
     * 新增一个角色
     * @param sysRole
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("role_add")
    @PostMapping
    public R saveOne(SysRole sysRole){
        sysRole.setCreateTime(LocalDateTime.now());
        sysRole.setDelFlag("0");
        if(sysRoleService.save(sysRole))return new R(200,"添加成功","");
        else return new R(200,"添加失败","");
    }
    /**
     *修改一个角色
     * @param sysRole
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("role_update")
    @PutMapping
    @CacheEvict(value="UserInfo",allEntries = true)
    public R putOne(SysRole sysRole){
        sysRole.setUpdateTime(LocalDateTime.now());
        if(sysRoleService.updateById(sysRole))
            return new R(200,"修改成功","");
        else
            return new R(200,"修改失败","");
    }
    /**
     * 删除一个角色
     * @param id
     * @return
     */
    @RequiresAuthentication
    @RequiresPermissions("role_del")
    @DeleteMapping("/{id}")
    public R deleteOne(@PathVariable("id") String id){
        if(sysRoleService.removeById(id))
            return new R(200,"删除成功","");
        else
            return new R(200,"删除失败","");
    }

    /**
     * 根据角色id,更新角色菜单权限
     * @param id
     * @param menuIds
     * @return
     */
    @PutMapping("/putRoleMenuByRoleId")
    @RequiresPermissions("role_update")
    @RequiresAuthentication
    public R putRoleMenuByRoleId(Integer id,String menuIds){
        sysRoleMenuService.putRoleMenuByRoleId(id,menuIds);
        return new R(200,"更新成功","");
    }
}
