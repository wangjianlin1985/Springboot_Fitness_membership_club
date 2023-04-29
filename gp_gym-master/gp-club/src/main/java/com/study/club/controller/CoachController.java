package com.study.club.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.club.constant.ClubConstant;
import com.study.common.config.Global;
import com.study.common.constant.PunctuationConstants;
import com.study.common.constant.UserConstants;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.common.exception.BusinessException;
import com.study.common.util.AESUtil;
import com.study.common.util.StringUtils;
import com.study.common.util.file.FileUploadUtils;
import com.study.framework.shiro.service.SysPasswordService;
import com.study.framework.util.ShiroUtils;
import com.study.system.entity.SysUser;
import com.study.system.service.SysUserService;

import cn.hutool.core.convert.Convert;

@Controller
@RequestMapping("/club/coach")
public class CoachController extends BaseController{
	
	private static String prefix = "club/coach";

	@Autowired
	private SysUserService userService;
	@Autowired
	private SysPasswordService passwordService;

	@GetMapping()
	public String user() {
		return prefix + "/coach";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysUser user) {
		user.setUserType(2);
		startPage();
		List<SysUser> list = userService.list(user);
		return getDataTable(list);
	}
	

	@GetMapping("show")
	public String show(ModelMap mmap) {
		SysUser user = new SysUser();
		user.setUserType(2);
		user.setStatus("0");
		List<SysUser> coachList = userService.list(user);
		mmap.addAttribute("coachList", coachList);
		return prefix + "/show";
	}
	
	
	/**
	 * 新增用户
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
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
		user.setUserType(2);
		user.setDeptId(ClubConstant.COACH_DEPT_ID);
		user.setRoleIds(new Long[]{ClubConstant.COACH_ROLE_ID});
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
		user.setUserType(2);
		user.setDeptId(ClubConstant.COACH_DEPT_ID);
		user.setRoleIds(new Long[]{ClubConstant.COACH_ROLE_ID});
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
	
	
	@PostMapping("/uploadCover")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String path = FileUploadUtils.upload(Global.getUploadPath(), file);
                Map<String, Object> map = new HashMap<>();
                map.put("path", path);
                return AjaxResult.success(map);
            }
            return error();
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
	
	
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(SysUser user) {
		return toAjax(userService.changeStatus(user));
	}

}
