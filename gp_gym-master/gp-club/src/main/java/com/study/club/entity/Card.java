package com.study.club.entity;

import com.study.common.core.domain.BaseEntity;
import com.study.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员卡 
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Card extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String code; // 卡号
	
	private Integer balance;  // 余额
	
	private Integer status;  // 0正常 1禁用 2过期 3退卡
	
	private String startTime; // 办卡时间
	
	private String endTime;   // 结束时间 ，次卡也是1年
	
	private Integer type; // 1年卡，2季卡，3月卡，4次卡
	
	private SysUser user;
	private Long userId;
}
