package com.mtdev.una.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.data.view.ResponseView;
import com.mtdev.una.data.view.Views;

@Controller
@RequestMapping("/welcome")
public class HelloController {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	// @PreAuthorize("@AccessTool.isOk()")
	public String printWelcome() {

		return "hello";

	}

	@RequestMapping(value = "/json", produces = "application/json")
	@ResponseBody
	@ResponseView(Views.Public.class)
	@PreAuthorize("@AccessTool.alwaysOk()")
	public Container getConteinerDataJson(
			@RequestParam(defaultValue = "10", required = false) Integer pNum) {
		return new Container(pNum, "containerName", "data", null);
	}

	@RequestMapping(value = "/js", produces = "application/javascript")
	@ResponseBody
	@PreAuthorize("@AccessTool.isOk()")
	public String getConteinerDataText() {
		return "alert('xxxx')";
	}

}
