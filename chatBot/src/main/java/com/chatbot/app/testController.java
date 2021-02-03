package com.chatbot.app;

import java.util.Map;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import result.BoxOfficeResult;
import result.test;

@RestController
public class testController {
	private static final Logger logger =  LoggerFactory.getLogger(testController.class);
	
	@RequestMapping(value="/test")
	public Map keyboard() {
		Map list = null;
		try {
			list = new BoxOfficeResult().getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
