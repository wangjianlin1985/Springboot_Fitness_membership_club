package com.study.club.entity;


import com.study.common.core.domain.BaseEntity;
import com.study.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 训练记录 
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Record extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private SysUser user;
	
	private String startTime;
	
	private String endTime;
	
	private Integer money;
	
	private String content;
	
	private Integer status;
	
}
