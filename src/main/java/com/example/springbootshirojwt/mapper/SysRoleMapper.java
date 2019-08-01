package com.example.springbootshirojwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootshirojwt.model.SysRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户id查询所拥有的角色
     * @param uId
     * @return
     */
    @Select("SELECT * FROM sys_user_role sur left join sys_role sr on  sur.role_id=sr.id where uid=#{uId} and sur.del_flag='0'")
    public List<SysRole> getRolesByUid(String uId);
}
