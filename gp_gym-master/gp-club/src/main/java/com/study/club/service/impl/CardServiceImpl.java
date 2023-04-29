package com.study.club.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.club.dao.CardDao;
import com.study.club.entity.Card;
import com.study.club.service.CardService;
import com.study.common.core.base.BaseServiceImpl;

@Service
public class CardServiceImpl extends BaseServiceImpl<Card> implements CardService{
	
	@Autowired
	private CardDao dao;

	@Override
	public int updateBalance(Card card) {
		return dao.updateBalance(card);
	}

	@Override
	public int updateStatus(Card card) {
		return dao.updateStatus(card);
	}

	@Override
	public int updateType(Card card) {
		return dao.updateType(card);
	}

	@Override
	public Card getByUserId(Long userId) {
		return dao.getByUserId(userId);
	}

	@Override
	public int updateXuka(Card card) {
		return dao.updateXuka(card);
	}

	@Override
	public int updateChongzhi(Card card) {
		return dao.updateChongzhi(card);
	}

	@Override
	public int updateXiaofei(Card card) {
		return dao.updateXiaofei(card);
	}

}
