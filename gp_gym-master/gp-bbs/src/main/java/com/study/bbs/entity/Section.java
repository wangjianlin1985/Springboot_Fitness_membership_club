package com.study.bbs.entity;

import com.study.common.core.domain.BaseEntity;
import com.study.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 论坛板块
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Section extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private Integer sort;
	
	private SysUser user;

	/**当前样式激活，前端展示使用，没有数据对应*/
	private boolean active = false;
}
