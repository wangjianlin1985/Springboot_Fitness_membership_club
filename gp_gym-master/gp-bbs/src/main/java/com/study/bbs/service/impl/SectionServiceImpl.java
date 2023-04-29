package com.study.bbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.bbs.constant.BBSRedisKey;
import com.study.bbs.entity.Section;
import com.study.bbs.service.SectionService;
import com.study.common.core.base.BaseServiceImpl;

@Service
public class SectionServiceImpl extends BaseServiceImpl<Section> implements SectionService{

	@Override
	public List<Section> listAllRedis() {
		return super.listRedis(new Section(), BBSRedisKey.SECTION_ALL);
	}
	
}
