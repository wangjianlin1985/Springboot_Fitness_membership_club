package com.study.club.entity;

import com.study.common.core.domain.BaseEntity;
import com.study.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 训练详情
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Detail extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Record record;
	private Long recordId;
	
	private Project project;
	
	private String startTime;
	
	private String endTime;
	
	private Machine machine;
	
	private SysUser coach;
	
	private Integer money;

}
