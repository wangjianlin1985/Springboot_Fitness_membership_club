package com.study.bbs.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.bbs.constant.BBSConstant;
import com.study.bbs.constant.BBSRedisKey;
import com.study.bbs.entity.Section;
import com.study.bbs.service.SectionService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.system.dao.SysUserRoleDao;
import com.study.system.entity.SysUser;
import com.study.system.entity.SysUserRole;
import com.study.system.service.SysUserService;

import cn.hutool.core.convert.Convert;

@Controller
@RequestMapping("/bbs/section")
public class SectionController extends BaseController{
	
	private static String prefix = "bbs/section";

	@Autowired
	private SysUserService userService;;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SysUserRoleDao userRoleDao;
	
	@GetMapping()
	public String section(ModelMap mmap) {
		SysUser user = new SysUser();
		user.setUserType(0);
	    List<SysUser> userList = userService.list(user);
	    mmap.addAttribute("userList", userList);
		return prefix + "/section";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Section section) {
		startPage();
		List<Section> list = sectionService.list(section);
		return getDataTable(list);
	}
	

	/**
	 * 新增信息
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		SysUser user = new SysUser();
		user.setUserType(0);
	    List<SysUser> userList = userService.list(user);
	    mmap.addAttribute("userList", userList);
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param section
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Section section) {
		int r = sectionService.addRedis(section, BBSRedisKey.SECTION_ALL);
		if (r > 0) {
			updateBanzhuRole();
		}
		return toAjax(r);
	}

	/**
	 * 修改
	 * @param sectionId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		SysUser user = new SysUser();
		user.setUserType(0);
	    List<SysUser> userList = userService.list(user);
	    mmap.addAttribute("userList", userList);
		mmap.addAttribute("section", sectionService.getById(id));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * 
	 * @param section
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Section section) {
		int r = sectionService.updateRedis(section, BBSRedisKey.SECTION_ALL);
		if (r > 0) {
			updateBanzhuRole();
		}
		return toAjax(r);
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(sectionService.deleteBatch(Convert.toLongArray(ids), getLoginName(),BBSRedisKey.SECTION_ALL));
	}
	
	/**
	 * 更新版主角色，
	 * 在设置或取消某板块的版主之后，
	 * 自动给他添加或取消版主的角色
	 */
	private void updateBanzhuRole() {
		// 查询当前有多少人是版主，并去重
		List<Section> sectionList = sectionService.list(new Section());
		Set<Long> userIdSet = new HashSet<>();
		for(Section section : sectionList) {
			if (section.getUser() != null && section.getUser().getUserId() != null) {
				userIdSet.add(section.getUser().getUserId());
			}
		}
		// 删除旧的
		userRoleDao.deleteAuthUsers(BBSConstant.BANZHU_ROLE_ID);
		// 插入新的
		List<SysUserRole> urList = new ArrayList<>();
		for(Long userId: userIdSet) {
			SysUserRole ur = new SysUserRole();
			ur.setRoleId(BBSConstant.BANZHU_ROLE_ID);
			ur.setUserId(userId);
			urList.add(ur);
		}
		if (urList.size() > 0) {
			userRoleDao.addBatch(urList);
		}
	}

}
