package com.study.bbs.entity;

import com.study.common.core.domain.BaseEntity;
import com.study.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子主题
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Topic extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	/**id*/
	private Long id;
	
	/**标题*/
	private String title;
	
	/**发表时间*/
	private String time;
	
	/**阅读数*/
	private Integer numSee;
	
	/**回复数*/
	private Integer numReply;
	
	/**内容*/
	private String content;
	
	/**发表人*/
	private SysUser user;
	
	/**角色*/
	private String roleName;
	
	/**所属板块*/
	private Section section;

}
