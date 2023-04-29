package com.study.club.entity;

import com.study.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Project extends BaseEntity{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private Integer money; 
	
	private String disease;  
	
}
