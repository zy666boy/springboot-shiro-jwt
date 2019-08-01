/*
 *
 *      Copyright (c) 2018-2025, gaoyang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the hn3l.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: gaoyang (gaoyang@gmail.com)
 *
 */

package com.example.springbootshirojwt.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
				//不知道为什么数据库字段带"_"的在映射到实体类的时候，不能映射！只有把实体类的_去掉并把其后小写变大写，数据库字段不用改变。
/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author gaoyang
 * @since 2017-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends Model<SysMenu> {
	private static final long serialVersionUID = 1L;
	/**
	 * 菜单ID
	 */
	@NotNull(message = "菜单ID不能为空")
	@TableId
	private Integer id;
	/**
	 * 菜单名称
	 */
	@NotBlank(message = "菜单名称不能为空")
	private String name;
	/**
	 * 菜单类型
	 */
	private String type;
	/**
	 * 所需权限
	 */
	private String permission;
	/**
	 * 父菜单ID
	 */
	@NotNull(message = "菜单父ID不能为空")
	//@TableField(value = "parent_id")
	private Integer parentId;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * VUE页面
	 */
	private String component;
	/**
	 * 排序值
	 */
	private Integer sort;
	/**
	 * 路由缓冲
	 */
	//@TableField(value = "keep_alive")
	private String keepAlive;
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
	/**
	 * 逻辑删除，0--正常 1--删除，调用mybatis的crud接口的删除方法时，改变的是此值，在使用查询方法(mybatis的crud)时，sql默认也会带上这个条件(delFlag=0)
	 */
	//@TableField(value = "del_flag")
	@TableLogic(value="0",delval="1")
	private String delFlag;
	/**
	 * 前端URL
	 */
	private String path;
}
