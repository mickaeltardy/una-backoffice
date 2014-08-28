package com.mtdev.una.controller.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.business.interfaces.ModulesManager;
import com.mtdev.una.model.Module;

@Controller
public class ModulesService {

	@Autowired
	protected ModulesManager mModulesManager;

	@RequestMapping("modules")
	public @ResponseBody List<Module> getModules() {
		List<Module> lModules = new ArrayList<Module>();
		try {
			lModules.addAll(mModulesManager.getNoAuthModules());
		} catch (Exception lE) {

		}

		try {
			lModules.addAll(mModulesManager.getAuthOnlyModules());
		} catch (Exception lE) {

		}

		return lModules;
	}

}
