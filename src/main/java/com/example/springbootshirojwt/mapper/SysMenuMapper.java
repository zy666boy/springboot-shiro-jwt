package com.example.springbootshirojwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootshirojwt.dto.Permission;
import com.example.springbootshirojwt.model.SysMenu;
import com.example.springbootshirojwt.vo.MenuVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据角色id，查询菜单选项集合
     * @param roleId
     * @return
     */
    List<SysMenu> getMenuByRoleId(Integer roleId);

    /**
     * 根据角色id获取权限集合
     * @param roleId
     * @return
     */
    @Select("select sm.id id,sm.name name,sm.permission permission from sys_role_menu srm left join sys_menu sm on srm.menu_id=sm.id where sm.permission!='' and srm.role_id=#{roleId}")
    List<Permission> getPermisssionByRoleId(Integer roleId);
}
