package com.mtdev.una.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.mtdev.una.business.interfaces.ModulesManager;
import com.mtdev.una.model.Module;

@Component
public class ModulesManagerImpl implements ModulesManager {

	@Override
	@PreAuthorize("!@AccessTool.isAuthenticated()")
	public List<Module> getNoAuthModules() {
		List<Module> lModules = new ArrayList<Module>();

		Module lSignup = new Module("signup", "Sign up");
		lSignup.setOrder(20);
		lModules.add(lSignup);

		Module lLogin = new Module("login", "Sign in");
		lLogin.setOrder(10);
		lModules.add(lLogin);

		return lModules;

	}

	@Override
	@PreAuthorize("@AccessTool.isAuthenticated()")
	public List<Module> getAuthOnlyModules() {
		List<Module> lModules = new ArrayList<Module>();
		Module lReg = new Module("profile", "Ma fiche d'inscription");
		lReg.setOrder(40);
		lModules.add(lReg);
		return lModules;
	}

}
