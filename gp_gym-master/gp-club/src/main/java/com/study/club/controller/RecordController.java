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

import com.study.club.entity.Card;
import com.study.club.entity.Level;
import com.study.club.entity.Record;
import com.study.club.entity.UserLevel;
import com.study.club.service.CardService;
import com.study.club.service.LevelService;
import com.study.club.service.RecordService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.system.entity.SysUser;
import com.study.system.service.SysUserService;

@Controller
@RequestMapping("/club/record")
public class RecordController extends BaseController {

	private static String prefix = "club/record";
	@Autowired
	private RecordService recordService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private CardService cardService;
	@Autowired
	private LevelService levelService;
	
	@GetMapping()
	public String record() {
		return prefix + "/record";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Record record) {
		// 如果是会员看到的话，那么看到的是他自己的记录
		SysUser user = userService.getById(getUserId());
		if (1 == user.getUserType()) {
			record.setUser(user);
		}
		startPage();
		List<Record> list = recordService.list(record);
		return getDataTable(list);
	}

	/**
	 * 新增
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		SysUser user = new SysUser();
		user.setUserType(1);
		List<SysUser> userList = userService.list(user);
		mmap.addAttribute("userList", userList);
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param record
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Record record) {
		return toAjax(recordService.add(record));
	}

	/**
	 * 修改
	 * @param recordId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.addAttribute("record", recordService.getById(id));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * 
	 * @param record
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Record record) {
		return toAjax(recordService.updateRedis(record));
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(recordService.deleteByIds(ids));
	}
	
	
	@PostMapping("/koukuan")
	@ResponseBody
	@Transactional
	public AjaxResult koukuan(Long id) {
		Record record = recordService.getById(id);
		SysUser user = record.getUser();
		Card card = cardService.getByUserId(user.getUserId());
		if (card == null) {
			return error("没有可用卡");
		}
		if (card.getBalance() < record.getMoney()) {
			return error("卡余额不足");
		}
		Integer newMoney = card.getBalance() - record.getMoney();
		card.setBalance(newMoney);
		//更新卡余额
		int r1 = cardService.updateBalance(card);
		
		// 更改已付款状态
		record.setStatus(1);
		recordService.update(record);
		
		// 查询已付款的所有消费记录
		Record searchRecord = new Record();
		searchRecord.setUser(user);
		searchRecord.setStatus(1);
		List<Record> recordList = recordService.list(searchRecord);
		int totalMoney = 0;
		for(Record r : recordList) {
			totalMoney += (r.getMoney());
		}
		// 根据余额查询当前处于哪个会员等级
		List<Level> levelList = levelService.list(new Level());
		Level currentLevel = levelList.get(0);
		for(Level level : levelList) {
			if (totalMoney >= level.getStart() && totalMoney < level.getEnd()) {
				currentLevel = level;
				break;
			}
		}
		
		// 更新或者插入会员等级
		UserLevel userLevel = levelService.getByUserId(user.getUserId());
		if(userLevel == null) {
			userLevel = new UserLevel();
			userLevel.setUser(user);
			userLevel.setLevel(currentLevel);
			levelService.addUserLevel(userLevel);
		} else {
			userLevel.setLevel(currentLevel);
			levelService.updateUserLevel(userLevel);
		}
		
		return toAjax(r1);
	}
	
	public static void main(String[] args) {
		String t1 = "2021-10-11 10:00:00";
		String t2 = "2021-10-12 10:00:00";
		System.out.println(t1.compareTo(t2));
	}
	
}
