package com.study.bbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.bbs.dao.TopicDao;
import com.study.bbs.entity.Topic;
import com.study.bbs.service.TopicService;
import com.study.common.core.base.BaseServiceImpl;

@Service
public class TopicServiceImpl extends BaseServiceImpl<Topic> implements TopicService {

	@Autowired
	private TopicDao dao;
	
	@Override
	public int updateSee(Long id) {
		return dao.updateSee(id);
	}

	@Override
	public int updateReply(Long id) {
		return dao.updateReply(id);
	}

}
