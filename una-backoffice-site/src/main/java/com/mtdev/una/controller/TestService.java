package com.mtdev.una.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestService implements ServiceManager {
	@RequestMapping("/data")
	@ResponseBody
	public Container randomPerson() {
		return new Container(10, "containerName", "data", null);
	}

}
