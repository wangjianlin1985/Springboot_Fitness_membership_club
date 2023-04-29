package com.study.club.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.club.dao.LevelDao;
import com.study.club.entity.Level;
import com.study.club.entity.UserLevel;
import com.study.club.service.LevelService;
import com.study.common.core.base.BaseServiceImpl;

@Service
public class LevelServiceImpl extends BaseServiceImpl<Level> implements LevelService{

	@Autowired
	private LevelDao dao;
	
	@Override
	public UserLevel getByUserId(Long userId) {
		return dao.getByUserId(userId);
	}

	@Override
	public int addUserLevel(UserLevel userLevel) {
		return dao.addUserLevel(userLevel);
	}

	@Override
	public int updateUserLevel(UserLevel userLevel) {
		return dao.updateUserLevel(userLevel);
	}

}
