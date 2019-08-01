package com.example.springbootshirojwt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.springbootshirojwt.vo.MenuVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class Permission implements Serializable {
    private Integer id;
    private String name;
    private String permission;

    /**重写hashcode（一般重写equals也要重写hasgcode）,hashcode是方便查询的，代表存储在散列表中相对的存储位置，
     * equals相同，hashcode必相同，反过来不成立，一个存储位置可存多个，去查helpsource
     * @return
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * 重写相等方法，默认为比较地址相等则，改成menuId 相同则相同，方便在Set中不会有值相同的对象出现
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            Integer targetPermissionId = ((Permission) obj).getId();
            return id.equals(targetPermissionId);
        }
        return super.equals(obj);
    }
}
