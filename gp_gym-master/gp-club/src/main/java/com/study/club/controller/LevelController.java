package com.study.club.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.club.entity.Level;
import com.study.club.service.LevelService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/club/level")
public class LevelController extends BaseController {

	private static String prefix = "club/level";

	@Autowired
	private LevelService levelService;

	@GetMapping()
	public String level() {
		return prefix + "/level";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Level level) {
		startPage();
		List<Level> list = levelService.list(level);
		return getDataTable(list);
	}

	/**
	 * 新增
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param level
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Level level) {
		return toAjax(levelService.add(level));
	}

	/**
	 * 修改
	 * @param levelId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.addAttribute("level", levelService.getById(id));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * 
	 * @param level
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Level level) {
		return toAjax(levelService.updateRedis(level));
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(levelService.deleteByIds(ids));
	}
	
	
}
