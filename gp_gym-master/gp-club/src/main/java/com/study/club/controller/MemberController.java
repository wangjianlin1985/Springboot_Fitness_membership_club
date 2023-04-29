package com.study.club.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.club.constant.ClubConstant;
import com.study.club.entity.Card;
import com.study.club.entity.UserLevel;
import com.study.club.service.CardService;
import com.study.club.service.LevelService;
import com.study.common.constant.PunctuationConstants;
import com.study.common.constant.UserConstants;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.common.exception.BusinessException;
import com.study.common.util.AESUtil;
import com.study.common.util.DateUtils;
import com.study.common.util.StringUtils;
import com.study.framework.shiro.service.SysPasswordService;
import com.study.framework.util.ShiroUtils;
import com.study.system.entity.SysDictData;
import com.study.system.entity.SysUser;
import com.study.system.service.SysConfigService;
import com.study.system.service.SysDictDataService;
import com.study.system.service.SysUserService;

import cn.hutool.core.convert.Convert;

@Controller
@RequestMapping("/club/member")
public class MemberController extends BaseController{
	
	private static String prefix = "club/member";

	@Autowired
	private SysUserService userService;
	@Autowired
	private SysConfigService configService;
	@Autowired
	private SysPasswordService passwordService;
	@Autowired
	private SysDictDataService sysDictDataService;
	@Autowired
	private LevelService levelService;
	@Autowired
	private CardService cardService;

	@GetMapping()
	public String user() {
		return prefix + "/member";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysUser user) {
		user.setUserType(1);
		startPage();
		List<SysUser> list = userService.list(user);
		for(SysUser u : list) {
			UserLevel userLevel = levelService.getByUserId(u.getUserId());
			if (userLevel == null) {
				u.setLevelName("暂未消费");
			} else {
				u.setLevelName(userLevel.getLevel().getName());
			}
		}
		return getDataTable(list);
	}
	
	
	@GetMapping("money")
	public String money() {
		return prefix + "/member_money";
	}
	
	@PostMapping("/moneyList")
	@ResponseBody
	public TableDataInfo moneyList() {
		Integer balanceRemind = Convert.toInt(configService.getConfigByKey("money_remind"), 7);
		// 所有会员
		SysUser user = new SysUser();
		user.setUserType(1);
		List<SysUser> oldList = userService.list(user);
		// 所有卡，过滤掉非正常的卡
		List<Card> cardList = cardService.list(new Card());
		Map<Long, Integer> map = new HashMap<>();
		for(Card card : cardList) {
			if (card.getStatus() == 0) {
				map.put(card.getUserId(), card.getBalance());
			}
		}
		List<SysUser> list = new ArrayList<>();
		for(SysUser u : oldList) {
			Integer balance = map.get(u.getUserId());
			if (balance == null) {
				continue;
			}
			if (balance <= balanceRemind) {
				u.setBalance(balance);
				list.add(u);
			}
		}
		return getDataTable(list);
	}
	
	
	
	@GetMapping("birth")
	public String birth() {
		return prefix + "/member_birth";
	}
	
	
	
	
	
	@PostMapping("/brithList")
	@ResponseBody
	public TableDataInfo brithList(SysUser user) {
		List<SysUser> list = new ArrayList<>();
		Integer days = Convert.toInt(configService.getConfigByKey("birth_remind_day"), 7);
		user.setUserType(1);
		List<SysUser> oldList = userService.list(user);
		String year = DateUtils.getDate().substring(0,4);
		Long now = System.currentTimeMillis();             // 今天
		Long remind = now + days * 24L * 3600 * 1000l;     // 几天后
		for(SysUser u : oldList) {
			String birth = u.getBirth();
			if (StringUtils.isEmpty(birth)) {
				continue;
			}
			String date = year + birth.substring(4);
			Long time = DateUtils.parseDate(date).getTime();
			if (now <= time && time <= remind) {
				UserLevel userLevel = levelService.getByUserId(u.getUserId());
				if (userLevel == null) {
					u.setLevelName("");
				} else {
					u.setLevelName(userLevel.getLevel().getName());
				}
				list.add(u);
			}
		}
		return getDataTable(list);
	}

	/**
	 * 新增用户
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		SysDictData sdd = new SysDictData();
		sdd.setDictType("diseaseList");
		List<SysDictData> diseaseList = sysDictDataService.listByType(sdd);
		mmap.put("diseaseList", diseaseList);
		return prefix + "/add";
	}

	/**
	 * 新增保存用户
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysUser user) {
		if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user))) {
			return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
		} else if (UserConstants.USER_IDCARDNO_NOT_UNIQUE.equals(userService.checkIdcardnoUnique(user))) {
            return error("新增用户'" + user.getLoginName() + "'失败，身份证号已存在");
        } else if(UserConstants.USER_CODE_NOT_UNIQUE.equals(userService.checkUsercodeUnique(user))) {
            return error("新增用户'" + user.getLoginName() + "'失败，学工号已存在");
        }
		if (StringUtils.isEmpty(user.getPassword())) {
			user.setPassword(ClubConstant.DEFAULT_PASSWORD);
		}
		user.setUserType(1);
		user.setDeptId(ClubConstant.MEMBER_DEPT_ID);
		user.setRoleIds(new Long[]{ClubConstant.MEMBER_ROLE_ID});
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		user.setOldpwd(AESUtil.encrypt(user.getPassword()));
		user.setCreateBy(ShiroUtils.getLoginName());
		
		return toAjax(userService.add(user));
	}

	/**
	 * 修改用户
	 */
	@GetMapping("/edit/{userId}")
	public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.getById(userId));
		SysDictData sdd = new SysDictData();
		sdd.setDictType("diseaseList");
		List<SysDictData> diseaseList = sysDictDataService.listByType(sdd);
		mmap.put("diseaseList", diseaseList);
		return prefix + "/edit";
	}

	/**
	 * 修改保存用户
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysUser user) {
		if (StringUtils.isNotNull(user.getUserId()) && SysUser.isAdmin(user.getUserId())) {
			return error("不允许修改超级管理员用户");
		} else if (UserConstants.USER_IDCARDNO_NOT_UNIQUE.equals(userService.checkIdcardnoUnique(user))) {
            return error("修改用户'" + user.getLoginName() + "'失败，身份证号已存在");
        } else if(UserConstants.USER_CODE_NOT_UNIQUE.equals(userService.checkUsercodeUnique(user))) {
            return error("修改用户'" + user.getLoginName() + "'失败，学工号已存在");
        }
		user.setUserType(1);
		user.setDeptId(ClubConstant.MEMBER_DEPT_ID);
		user.setRoleIds(new Long[]{ClubConstant.MEMBER_ROLE_ID});
		user.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(userService.update(user));
	}


	@PostMapping("/resetPwd")
	@ResponseBody
	public AjaxResult resetPwdSave(SysUser user) {
		user.setSalt(ShiroUtils.randomSalt());
		user.setOldpwd(AESUtil.encrypt(user.getPassword()));
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		if (userService.resetUserPwd(user) > 0) {
			if (ShiroUtils.getUserId().equals(user.getUserId())) {
				ShiroUtils.setSysUser(userService.getById(user.getUserId()));
			}
			return success();
		}
		return error();
	}

	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		try {
			for(String id : ids.split(PunctuationConstants.COMMA)) {
				if(SysUser.isAdmin(Long.valueOf(id))) {
					 throw new BusinessException("不允许删除超级管理员用户");
				}
			}
			return toAjax(userService.deleteBatch(Convert.toLongArray(ids), ShiroUtils.getLoginName()));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

}
