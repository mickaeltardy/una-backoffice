package com.mtdev.una.controller.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.security.AccessTool;
import com.mtdev.una.security.UserDetailsImpl;

@Controller
@RequestMapping(value = "/auth")
public class AuthService {

	@Autowired
	protected AccessTool mAccessTool;

	@RequestMapping(value = "/check")
	@PreAuthorize("@AccessTool.isAuthenticated()")
	public @ResponseBody Object isAuthenticated() {
		Map<Object, Object> lResponse = new HashMap<Object, Object>();
		lResponse.put("auth", "true");
		lResponse.put("user", mAccessTool.getUserDetails());

		if (mAccessTool.getUserDetails() != null) {
			try {
				lResponse.put("username", ((UserDetailsImpl) mAccessTool
						.getUserDetails()).getUsername());
			} catch (Exception lE) {

			}

		}

		return lResponse;
	}

}
