package com.study.bbs.entity;

import com.study.common.core.domain.BaseEntity;
import com.study.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子回复
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Reply extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**id*/
	private Long id;
	
	/**发表时间*/
	private String time;
	
	/**内容*/
	private String content;
	
	/**发表人*/
	private SysUser user;
	
	/**角色*/
	private String roleName;
	
	/**所属板块*/
	private Topic topic;
	
	private boolean louzhu = false;
	
}
