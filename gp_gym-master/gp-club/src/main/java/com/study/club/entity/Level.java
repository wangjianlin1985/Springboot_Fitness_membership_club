package com.study.club.entity;

import com.study.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员等级 
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Level extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private Integer sort;
	
	private Integer start;
	
	private Integer end;
	

}
