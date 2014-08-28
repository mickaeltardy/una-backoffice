package com.mtdev.una.business.interfaces;

import java.util.List;

import com.mtdev.una.model.Module;

public interface ModulesManager {

	List<Module> getNoAuthModules();

	List<Module> getAuthOnlyModules();

}
