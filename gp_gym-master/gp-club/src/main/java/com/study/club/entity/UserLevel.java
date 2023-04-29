package com.study.club.entity;

import com.study.system.entity.SysUser;

import lombok.Data;

@Data
public class UserLevel {
	
	private Long id;
	
	private Level level;
	
	private SysUser user;

}
