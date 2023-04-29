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

import com.study.club.entity.Detail;
import com.study.club.entity.Machine;
import com.study.club.entity.Project;
import com.study.club.entity.Record;
import com.study.club.service.DetailService;
import com.study.club.service.MachineService;
import com.study.club.service.ProjectService;
import com.study.club.service.RecordService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.system.entity.SysUser;
import com.study.system.service.SysUserService;

import cn.hutool.core.convert.Convert;

@Controller
@RequestMapping("/club/record/detail/")
public class DetailController extends BaseController{
	
	private static String prefix = "club/detail/";
	
	@Autowired
	private DetailService detailService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private MachineService machineService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private RecordService recordService;
	
	
	@GetMapping("/{recordId}")
	public String detail(@PathVariable Long recordId,ModelMap mmap) {
		mmap.put("recordId", recordId);
		return prefix + "/detail";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Detail detail) {
		List<Detail> list = detailService.list(detail);
		return getDataTable(list);
	}

	/**
	 * 新增
	 */
	@GetMapping("/add/{recordId}")
	public String add(@PathVariable Long recordId,ModelMap mmap) {
		mmap.put("recordId", recordId);
		mmap.put("projectList", projectService.list(new Project()));
		mmap.put("machineList", machineService.list(new Machine()));
		SysUser user = new SysUser();user.setUserType(2);user.setStatus("0");
		List<SysUser> userList = userService.list(user);
		mmap.put("coachList", userList);
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param detail
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Detail detail) {
		int r = detailService.add(detail);
		// 重新计算总金额
		countMoney(detail.getRecord().getId());
		return toAjax(r);
	}

	
	@PostMapping("/delete")
	@ResponseBody
	public AjaxResult addSave(String ids) {
		int r = detailService.deleteByIds(ids);
		Long recordId = detailService.getById(Convert.toLongArray(ids)[0]).getRecordId();
		// 重新计算总金额
		countMoney(recordId);
		return toAjax(r);
	}

		
	private void countMoney(Long recordId) {
		Detail d = new Detail();
		d.setRecordId(recordId);
		List<Detail> list = detailService.list(d);
		int money = 0;
		for(Detail detail : list) {
			money += detail.getMoney();
		}
		Record record = new Record();
		record.setId(recordId);
		record.setMoney(money);
		recordService.update(record);
	}

	

}
