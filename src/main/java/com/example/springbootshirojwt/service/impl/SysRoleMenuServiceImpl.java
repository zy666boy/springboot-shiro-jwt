package com.example.springbootshirojwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootshirojwt.mapper.SysRoleMenuMapper;
import com.example.springbootshirojwt.model.SysRoleMenu;
import com.example.springbootshirojwt.service.SysRoleMenuService;
import com.example.springbootshirojwt.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * 根据角色id更新角色的菜单权限
     * @param id
     * @param menuIds
     * @return
     */
    @Transactional
    public boolean putRoleMenuByRoleId(Integer id, String menuIds){
        sysRoleMenuMapper.deleteById(id);
        if(menuIds==""||menuIds==null)return true;
        String[] mIds=menuIds.split(",");
        List<SysRoleMenu> sysRoleMenus=new ArrayList<>();
        for(String mId:mIds){
            SysRoleMenu sysRoleMenu=new SysRoleMenu();
            sysRoleMenu.setDelFlag("0");
            sysRoleMenu.setRoleId(id);
            sysRoleMenu.setMenuId(Integer.valueOf(mId));
            sysRoleMenu.setUpdateTime(LocalDateTime.now());
            sysRoleMenus.add(sysRoleMenu);
        }
       this.saveBatch(sysRoleMenus);
        return true;
    }
}
