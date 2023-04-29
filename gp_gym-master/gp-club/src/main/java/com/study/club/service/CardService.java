package com.study.club.service;

import com.study.club.entity.Card;
import com.study.common.core.base.BaseService;

public interface CardService extends BaseService<Card>{
	
	int updateBalance(Card card);
	
	int updateStatus(Card card);
	
	int updateType(Card card);
	
	Card getByUserId(Long userId);

	int updateXuka(Card card);
	
	int updateChongzhi(Card card);
	
	int updateXiaofei(Card card);

}
