package com.mtdev.una.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.mtdev.una.business.interfaces.ModulesManager;
import com.mtdev.una.data.dao.ModuleDao;
import com.mtdev.una.model.Module;
import com.mtdev.una.security.AccessTool;

@Component
public class ModulesManagerImpl implements ModulesManager {

	@Autowired
	protected ModuleDao mModuleDao;
	
	protected String[] sDefaultRole = {"none"};
	
	@Autowired
	protected AccessTool mAccessTool;
	
	@Override
	@PreAuthorize("!@AccessTool.isAuthenticated()")
	public List<Module> getNoAuthModules() {
		List<Module> lModules = new ArrayList<Module>();
		
		lModules.addAll(mModuleDao.getModulesForRole(Arrays.asList(sDefaultRole)));

		return lModules;

	}

	@Override
	@PreAuthorize("@AccessTool.isAuthenticated()")
	public List<Module> getAuthOnlyModules() {
		List<Module> lModules = new ArrayList<Module>();
		lModules.addAll(mModuleDao.getModulesForRole(mAccessTool.getUserRoles()));

		return lModules;
	}

}
