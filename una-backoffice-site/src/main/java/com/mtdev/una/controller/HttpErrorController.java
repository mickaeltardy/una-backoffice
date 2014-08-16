package com.mtdev.una.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HttpErrorController {

	@RequestMapping(value = "/errors/404")
	public @ResponseBody SystemMessage handle404() {
		return new SystemMessage("error", 404, "Not found");
	}

	@RequestMapping(value = "/errors/403")
	public @ResponseBody SystemMessage handle403() {
		return new SystemMessage("error", 403, "Not access");
	}

}