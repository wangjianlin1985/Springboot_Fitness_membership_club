package com.study.club.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.club.entity.Card;
import com.study.club.service.CardService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/club/card")
public class CardController extends BaseController{

	private static String prefix = "club/card/";
	
	@Autowired
	private CardService cardService;
	
	@GetMapping("/{userId}")
	public String card(@PathVariable Long userId,ModelMap mmap) {
		mmap.put("userId", userId);
		return prefix + "/card";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Card card) {
		startPage();
		List<Card> list = cardService.list(card);
		return getDataTable(list);
	}

	/**
	 * 新增
	 */
	@GetMapping("/add/{userId}")
	public String add(@PathVariable Long userId,ModelMap mmap) {
		mmap.put("userId", userId);
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param card
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Card card) {
		return toAjax(cardService.add(card));
	}

	/**
	 * 修改
	 * @param cardId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.addAttribute("card", cardService.getById(id));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * 
	 * @param card
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Card card) {
		return toAjax(cardService.updateRedis(card));
	}

	
	@PostMapping("/updateStatus")
	@ResponseBody
	public AjaxResult updateStatus(@Validated Card card) {
		return toAjax(cardService.updateStatus(card));
	}
	
	
	@PostMapping("/updateType")
	@ResponseBody
	public AjaxResult updateType(@Validated Card card) {
		return toAjax(cardService.updateType(card));
	}
	
	@PostMapping("/updateXuka")
	@ResponseBody
	public AjaxResult updateXuka(@Validated Card card) {
		return toAjax(cardService.updateXuka(card));
	}
	
	@PostMapping("/updateChongzhi")
	@ResponseBody
	public AjaxResult updateChongzhi(@Validated Card card) {
		return toAjax(cardService.updateChongzhi(card));
	}
	
}
