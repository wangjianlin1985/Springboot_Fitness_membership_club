package com.study.bbs.service;

import com.study.bbs.entity.Topic;
import com.study.common.core.base.BaseService;

public interface TopicService extends BaseService<Topic> {

	int updateSee(Long id);
	
	int updateReply(Long id);
	
}
