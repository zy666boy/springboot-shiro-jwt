package com.example.springbootshirojwt.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("sys_role_menu")
public class SysRoleMenu {
    private Integer roleId;
    private Integer menuId;
    //逻辑删除，0--正常 1--删除，调用mybatis的crud接口的删除方法时，改变的是此值，在使用查询方法时，sql默认也会带上这个条件(delFlag=0)
    @TableLogic(value="0",delval="1")
    //@TableField(value = "del_flag")
    private String delFlag;
    /**
     * 创建时间
     */
    //@TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    //@TableField(value = "update_time")
    private LocalDateTime updateTime;
}
