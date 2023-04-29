package com.study.club.service;

import com.study.club.entity.Level;
import com.study.club.entity.UserLevel;
import com.study.common.core.base.BaseService;

public interface LevelService extends BaseService<Level>{
	
	UserLevel getByUserId(Long userId);
	
	int addUserLevel(UserLevel userLevel);
	
	int updateUserLevel(UserLevel userLevel);

}
