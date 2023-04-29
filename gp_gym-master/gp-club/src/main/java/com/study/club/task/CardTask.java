package com.study.club.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.study.club.entity.Card;
import com.study.club.service.CardService;
import com.study.common.util.DateUtils;

/**
 * 自动将时间到期的卡的状态置为过期 定时任务
 */
@Component
public class CardTask {

	@Autowired
	private CardService cardService;
	
	@Scheduled(cron = "0 0 10 * * *")
    public void testTasks() {
        Card card = new Card();
        card.setStatus(0);
        List<Card> cardList = cardService.list(card);
        String date = DateUtils.getDate();
        for(Card c : cardList) {
        	if (c.getEndTime().compareTo(date) < 0) {
				c.setStatus(2);
				cardService.updateStatus(c);
			}
        }
        
    }
	
	
}
