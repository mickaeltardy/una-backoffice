package com.mtdev.una.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

	@RequestMapping(value = "/isOk")
	@PreAuthorize("true")
	public @ResponseBody String isAuthorized() {
		return "Seems so";
	}

}
