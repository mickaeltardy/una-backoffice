package com.mtdev.una.controller.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.business.interfaces.ProfilesManager;
import com.mtdev.una.data.dao.ProfileDao;
import com.mtdev.una.data.view.ResponseView;
import com.mtdev.una.data.view.Views;
import com.mtdev.una.model.Profile;

@Controller
@RequestMapping("/supervisor")
public class SupervisorService {

	@Autowired
	ProfileDao mProfileDao;

	@Autowired
	ProfilesManager mProfilesManager;

	@RequestMapping(value = "/profiles", method = RequestMethod.GET)
	@ResponseView(Views.Public.class)
	@PreAuthorize("@AccessTool.isUserSupervisor()")
	public @ResponseBody List<Profile> getProfiles() {
		return mProfilesManager.getValidProfiles();
	}

	@RequestMapping(value = "/profiles/{username:.+}", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseView(Views.Public.class)
	@PreAuthorize("@AccessTool.isUserSupervisor()")
	public @ResponseBody Object updateProfile(
			@PathVariable("username") String pUsername,
			@RequestBody Map<Object, Object> pProfileInput) {
		return mProfilesManager.updateProfile(pUsername, pProfileInput);
	}

}
