package com.study.bbs.dao;

import com.study.bbs.entity.Topic;
import com.study.common.core.base.BaseDao;

public interface TopicDao extends BaseDao<Topic>{

	int updateSee(Long id);
	
	int updateReply(Long id);
	
}
