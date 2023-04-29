package com.study.bbs.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.bbs.entity.Reply;
import com.study.bbs.entity.Section;
import com.study.bbs.entity.Topic;
import com.study.bbs.service.ReplyService;
import com.study.bbs.service.SectionService;
import com.study.bbs.service.TopicService;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.util.DateUtils;
import com.study.common.util.StringUtils;
import com.study.system.entity.SysUser;
import com.study.system.service.SysUserService;

@Controller
@RequestMapping("/bbs/topic")
public class TopicController extends BaseController {
	
	private static String prefix = "bbs/topic";
	
	@Autowired
	private TopicService topicService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private SysUserService userService;
	
	/**
	 * BBS首页
	 * 如果不传sectionId就是默认第一个
	 * @param mmap
	 * @param request
	 * @return
	 */
	@GetMapping("index")
	public String section(ModelMap mmap,HttpServletRequest request) {
		List<Section> sectionList = sectionService.listAllRedis();
		
		Section section = sectionList.get(0);
		String sectionId = request.getParameter("sectionId");
		if (StringUtils.isNotEmpty(sectionId)) {
			section = sectionService.getById(Long.valueOf(sectionId));
		}
		SysUser banzhu = section.getUser();
		mmap.put("banzhu", banzhu);
		
		for(Section s: sectionList) {
			if (s.getId().equals(section.getId())) {
				s.setActive(true);
			}
		}
		mmap.addAttribute("sectionList", sectionList);
		
		Topic topic1 = new Topic();
		topic1.setSection(section);
		List<Topic> topicList = topicService.list(topic1);
		mmap.addAttribute("topicList", topicList);
		
		return prefix + "/topic";
	}
	
	
	@GetMapping("detail")
	public String detail(ModelMap mmap,HttpServletRequest request) {
		Long id = Long.valueOf(request.getParameter("id"));
		Topic topic = topicService.getById(id);
		mmap.addAttribute("topic", topic);
		Long louzhuId = topic.getUser().getUserId();
		
		Reply reply = new Reply();
		reply.setTopic(topic);
		List<Reply> replyList = replyService.list(reply);
		for(Reply r : replyList) {
			if (r.getUser().getUserId().equals(louzhuId)) {
				r.setLouzhu(true);
			}
		}
		mmap.addAttribute("replyList", replyList);
		mmap.addAttribute("replySize", replyList.size());
		
		// 此时top阅读数量加1
		topicService.updateSee(id);
		
		return prefix + "/detail";
	}
	
	
	@RequestMapping("newTopic")
	public String newTopic(ModelMap mmap) {
		List<Section> sectionList = sectionService.listAllRedis();
		mmap.addAttribute("sectionList", sectionList);
		return prefix + "/add";
	}
	
	
	@ResponseBody
	@RequestMapping("addTopic")
	@Transactional
	public AjaxResult addTopic(HttpServletRequest request) {
		String content = request.getParameter("content");
		Long sectionId = Long.valueOf(request.getParameter("sectionId"));
		Topic topic = new Topic();
		Section section = new Section();
		section.setId(sectionId);
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		topic.setTitle(request.getParameter("title"));
		topic.setRoleName(userService.getById(getUserId()).getRoles().get(0).getRoleName());
		topic.setTime(DateUtils.getTime());
		topic.setContent(content);
		topic.setSection(section);
		topic.setUser(user);
		int r = topicService.add(topic);
		return toAjax(r);
	}
	
	
	@ResponseBody
	@RequestMapping("addReply")
	@Transactional
	public AjaxResult addReply(HttpServletRequest request) {
		String content = request.getParameter("content");
		Long topicId = Long.valueOf(request.getParameter("topicId"));
		Topic topic = new Topic();
		topic.setId(topicId);
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		
		Reply reply = new Reply();
		reply.setTime(DateUtils.getTime());
		reply.setContent(content);
		reply.setTopic(topic);
		reply.setUser(user);
		int r = replyService.add(reply);
		if (r > 0) {
			topicService.updateReply(topicId);
		}
		return toAjax(r);
	}
	

}
