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
import com.study.club.entity.Machineio;
import com.study.club.service.MachineService;
import com.study.club.service.MachineioService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.common.util.DateUtils;

@Controller
@RequestMapping("/club/machine/io")
public class MachineioController extends BaseController {
	
private static String prefix = "club/machineio/";
	
	@Autowired
	private MachineioService machineioService;
	@Autowired
	private MachineService machineService;
	
	@GetMapping("/{machineId}")
	public String machine(@PathVariable Long machineId,ModelMap mmap) {
		mmap.put("machineId", machineId);
		return prefix + "/machineio";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Machineio machineio) {
		startPage();
		List<Machineio> list = machineioService.list(machineio);
		return getDataTable(list);
	}

	/**
	 * 新增
	 */
	@GetMapping("/add/{machineId}")
	public String add(@PathVariable Long machineId,ModelMap mmap) {
		mmap.put("machineId", machineId);
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param machineio
	 * //0进1出
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	@Transactional
	public AjaxResult addSave(@Validated Machineio machineio) {
		Machine machine = machineService.getById(machineio.getMachine().getId());
		Integer oldNum = machine.getNum();
		if (1 == machineio.getIo() && oldNum < machineio.getNum()) {
			return error("库存不足，操作失败");
		}
		int newNum = 0;
		if (1==machineio.getIo()) {
			newNum = oldNum - machineio.getNum();
		} else {
			newNum = oldNum + machineio.getNum();
		}
		machine.setNum(newNum);
		// 更新数量
		machineService.update(machine);
		
		machineio.setTime(DateUtils.getTime());
		// 插入数据
		int r = machineioService.add(machineio);
		return toAjax(r);
	}
	

}
