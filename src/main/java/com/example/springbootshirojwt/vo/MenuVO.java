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
    //不知道为什么数据库字段带"_"的在映射到实体类的时候，不能映射！只有把实体类的_去掉并把其后小写变大写，数据库字段不用改变。
package com.example.springbootshirojwt.vo;
import com.example.springbootshirojwt.model.SysMenu;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author gaoyang
 * @since 2017-11-08
 */
@Data
public class MenuVO implements Serializable {

	private static final long serialVersionUID = 1L;
	public MenuVO(){

	}
	public MenuVO(SysMenu sysMenu){
		this.id=sysMenu.getId();
		this.name=sysMenu.getName();
		this.type=sysMenu.getType();
		this.permission=sysMenu.getPermission();
		this.parentId=sysMenu.getParentId();
		this.icon=sysMenu.getIcon();
		this.path=sysMenu.getPath();
		this.component=sysMenu.getComponent();
		this.sort=sysMenu.getSort();
		this.keepAlive=sysMenu.getKeepAlive();
		this.createTime=sysMenu.getCreateTime();
		this.updateTime=sysMenu.getUpdateTime();
		this.delFlag=sysMenu.getDelFlag();
	}
	/**
	 * 菜单ID
	 */
	private Integer id;
	/**
	 * 菜单名称
	 */
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
	private Integer parentId;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 一个路径
	 */
	private String path;
	/**
	 * VUE页面
	 */
	private String component;
	/**
	 * 排序值
	 */
	private Integer sort;
	/**
	 * 是否缓冲
	 */
	private String keepAlive;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 0--正常 1--删除
	 */
	private String delFlag;

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
		if (obj instanceof MenuVO) {
			Integer targetMenuId = ((MenuVO) obj).getId();
			return id.equals(targetMenuId);
		}
		return super.equals(obj);
	}
}
