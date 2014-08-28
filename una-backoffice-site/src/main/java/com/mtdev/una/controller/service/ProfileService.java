package com.mtdev.una.controller.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.business.interfaces.ProfilesManager;
import com.mtdev.una.business.interfaces.UsersManager;
import com.mtdev.una.model.Profile;
import com.mtdev.una.security.SecurityToolbox;
import com.mtdev.una.tools.Toolbox;

@RequestMapping("profile")
@Controller
public class ProfileService {

	@Autowired
	protected ProfilesManager mProfilesManager;

	@Autowired
	protected UsersManager mUsersManager;

	@Autowired
	protected SecurityToolbox mSecurityToolbox;

	@RequestMapping(value = "/saveProfile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("true")
	public @ResponseBody Object registerNewApplicationAccount(
			@RequestBody Map<Object, Object> pProfileInput) {

		String lProfileUsername = (pProfileInput.containsKey("username")) ? (String) pProfileInput
				.get("username") : null;
		String lAuthUsername = mSecurityToolbox.getUsername();
		if ((mSecurityToolbox.isUserRoleOnly() && (lAuthUsername != null
				&& lProfileUsername != null && (mSecurityToolbox.getUsername()
				.compareTo(lProfileUsername)) == 0))
				|| mSecurityToolbox.isAdminRole()) {

			if (mProfilesManager.saveProfile(pProfileInput))
				return Toolbox.generateResult("status", "success");

		}
		if (lAuthUsername == null) {
			if (!mUsersManager.doesUserExist(lProfileUsername)) {

				if (mProfilesManager.saveProfileAndUser(pProfileInput)) {
					return Toolbox.generateResult("status", "success");
				}
			}
		}

		return Toolbox.generateResult("error", 
				"failed to save profile");
	}

	@RequestMapping(value = "/getProfile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("@AccessTool.isAuthenticated()")
	public @ResponseBody Object registerNewApplicationAccount(
			@RequestParam("username") String pUsername) {

		Profile lProfile = mProfilesManager.getProfileByUsername(pUsername);

		if (lProfile != null) {
			return Toolbox.generateResult("profile", lProfile);
		}

		return Toolbox.generateResult("error", new Error("No profile"));
	}

}
