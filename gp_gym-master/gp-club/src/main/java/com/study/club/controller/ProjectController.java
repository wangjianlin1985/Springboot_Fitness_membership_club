package com.study.club.controller;

import java.util.ArrayList;
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

import com.study.club.entity.Project;
import com.study.club.service.ProjectService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.common.util.StringUtils;
import com.study.system.entity.SysDictData;
import com.study.system.service.SysDictDataService;
import com.study.system.service.SysUserService;

import cn.hutool.core.convert.Convert;

@Controller
@RequestMapping("/club/project")
public class ProjectController extends BaseController {
	
	private static String prefix = "club/project";
	
	@Autowired
	private SysDictDataService sysDictDataService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SysUserService userService;
	
	
	@GetMapping("recommend")
	public String recommend() {
		return prefix + "/recommend";
	}
	
	@RequestMapping("recommendList")
	@ResponseBody
	public TableDataInfo recommendList() {
		List<Project> list = projectService.list(new Project());
		String disease = userService.getById(getUserId()).getDisease();
		// 我没有疾病 直接全部返回
		if (StringUtils.isEmpty(disease)) {
			return getDataTable(list);
		} else {
			List<Project> newList = new ArrayList<>();
			// 我的病array
			String[] mys = Convert.toStrArray(disease);
			for(Project project : list) {
				String d = project.getDisease();
				// 这个项目没有病的要求，直接添加
				if (StringUtils.isEmpty(d)) {
					newList.add(project);
				} else {
					// 这个项目不能参加的病，我正好有。
					boolean flag = false;
					for(String my: mys) {
						if (d.contains(my)) {
							flag = true;break;
						}
					}
					if (!flag) {
						newList.add(project);
					}
				}
			}
			return getDataTable(newList);
		}
	}
	
	

	@GetMapping()
	public String project() {
		return prefix + "/project";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Project project) {
		startPage();
		List<Project> list = projectService.list(project);
		return getDataTable(list);
	}

	/**
	 * 新增
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
	 * 新增保存
	 * @param project
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Project project) {
		return toAjax(projectService.add(project));
	}

	/**
	 * 修改
	 * @param projectId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.addAttribute("project", projectService.getById(id));
		SysDictData sdd = new SysDictData();
		sdd.setDictType("diseaseList");
		List<SysDictData> diseaseList = sysDictDataService.listByType(sdd);
		mmap.put("diseaseList", diseaseList);
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * 
	 * @param project
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Project project) {
		return toAjax(projectService.updateRedis(project));
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(projectService.deleteByIds(ids));
	}
	

}
