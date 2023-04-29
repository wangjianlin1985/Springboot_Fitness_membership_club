package com.study.club.dao;

import com.study.club.entity.Card;
import com.study.common.core.base.BaseDao;

public interface CardDao extends BaseDao<Card> {
	
	int updateBalance(Card card);
	
	int updateStatus(Card card);
	
	int updateType(Card card);
	
	Card getByUserId(Long userId);
	
	int updateXuka(Card card);
	
	int updateChongzhi(Card card);
	
	int updateXiaofei(Card card);

}
