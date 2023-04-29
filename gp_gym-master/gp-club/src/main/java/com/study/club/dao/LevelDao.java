package com.study.club.dao;

import com.study.club.entity.Level;
import com.study.club.entity.UserLevel;
import com.study.common.core.base.BaseDao;

public interface LevelDao extends BaseDao<Level> {
	
	UserLevel getByUserId(Long userId);
	
	int addUserLevel(UserLevel userLevel);
	
	int updateUserLevel(UserLevel userLevel);

}
