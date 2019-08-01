package com.example.springbootshirojwt.model;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser implements Serializable {
    @TableId(type=IdType.UUID)
    private String uid;
    private String username;//帐号
    private String name;//名称（昵称或者真实姓名，不同系统不同定义）
    private String password; //密码;
    private String salt;//加密密码的盐
    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
     //逻辑删除，0--正常 1--删除，调用mybatis的crud接口的删除方法时，改变的是此值，在使用查询方法时，sql默认也会带上这个条件(delFlag=0)
     @TableLogic(value="0",delval="1")
     //@TableField(value = "del_flag")
     private String delFlag;
    /**
     * 创建时间
     */
   // @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    //@TableField(value = "update_time")
    private LocalDateTime updateTime;
    //开始更新时间
    @TableField(exist = false)
    private String startCreateTime;
    //结束更新时间
    @TableField(exist = false)
    private String endCreateTime;
    //密码盐 ，重新对盐重新进行了定义，用户名+salt，这样就更加不容易被破解
    public String getCredentialsSalt(){
        return this.username+this.salt;
    }
}
