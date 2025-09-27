package com.daowuyou.sessionservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daowuyou.sessionservice.domain.po.Dialogue;
import com.daowuyou.sessionservice.mapper.DialogueMapper;
import com.daowuyou.sessionservice.service.ISessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ObjectStorageServiceApplicationTests {


	@Autowired
	public ISessionService sessionService;
	@Autowired
	public DialogueMapper dialogueMapper;
	@Test
	void contextLoads() {
		Dialogue dialogue = dialogueMapper.selectById(1L);
		Page<Dialogue> doPage = dialogueMapper.selectPage(
				new Page<>(1, 20),
				new LambdaQueryWrapper<>(Dialogue.class)
						.select()
						.eq(Dialogue::getSessionId, 5)
						.orderByDesc(Dialogue::getCreateTime)
		);
		return;
	}

}
