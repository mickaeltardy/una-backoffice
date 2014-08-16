package com.mtdev.una.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.data.dao.UserDao;
import com.mtdev.una.model.User;
import com.mtdev.una.security.SecurityConstants;
import com.mtdev.una.tools.Toolbox;

@Controller
@RequestMapping("/registration")
public class RegistrationService {

	@Autowired
	UserDao mUserDao;

	@RequestMapping(value = "/external", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("true")
	public @ResponseBody Map<String, String> registerNewExternalAccount(
			@RequestParam("username") String pUsername,
			@RequestParam("openid") String pOpenid,
			@RequestParam("origin") String pOrigin) {

		Map<String, String> lResponse = new HashMap<String, String>();

		if (!StringUtils.isEmpty(pUsername) && !StringUtils.isEmpty(pOpenid)
				&& !StringUtils.isEmpty(pOrigin)) {

			User lDbUser = mUserDao.getUserByUsername(pUsername);

			if (lDbUser == null) {
				lDbUser = new User(pUsername, null);

				lDbUser.addRole(SecurityConstants.Roles.user);
			}
			if (!this.doesOpenidExist(pOrigin, lDbUser)) {
				lDbUser.putOpenid(pOrigin, Toolbox.md5Spring(pOpenid));

				mUserDao.saveUser(lDbUser);
			}
		}

		return lResponse;

	}

	private boolean doesOpenidExist(String pOrigin, User pDbUser) {
		boolean lResult = false;
		if (pOrigin != null && pDbUser != null && pDbUser.getOpenids() != null) {
			pDbUser.getOpenids().containsKey(pOrigin);
		}

		return lResult;
	}

}
