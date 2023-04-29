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

import com.study.club.entity.Machine;
import com.study.club.service.MachineService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/club/machine")
public class MachineController extends BaseController{
	
	private static String prefix = "club/machine";

	@Autowired
	private MachineService machineService;

	@GetMapping()
	public String machine() {
		return prefix + "/machine";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Machine machine) {
		startPage();
		List<Machine> list = machineService.list(machine);
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
	 * @param machine
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Machine machine) {
		return toAjax(machineService.add(machine));
	}

	/**
	 * 修改
	 * @param machineId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.addAttribute("machine", machineService.getById(id));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * 
	 * @param machine
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Machine machine) {
		return toAjax(machineService.updateRedis(machine));
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(machineService.deleteByIds(ids));
	}
	

	

}
