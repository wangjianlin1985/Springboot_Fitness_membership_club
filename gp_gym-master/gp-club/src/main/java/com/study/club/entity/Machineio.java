package com.study.club.entity;

import com.study.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Machineio extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Integer num;
	
	private String time;
	
	private String type;
	
	private Integer io; //0进1出
	
	private Machine machine;
	
	private Long machineId;
	
}
