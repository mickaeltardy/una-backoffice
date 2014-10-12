package com.mtdev.una.data.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mtdev.una.model.Module;

@Component
public class ModuleDao {
	@Autowired
	MongoOperations mMongoOperations;

	public List<Module> getAllModules() {
		List<Module> lModules = mMongoOperations.findAll(Module.class);
		return lModules;
	}
	
	public List<Module> getModulesForRole(List<String> pRoles){
		Query searchUserQuery = new Query(Criteria.where("roles").in(
				pRoles));

		// find the saved user again.
		List<Module> lModules = mMongoOperations.find(searchUserQuery,
				Module.class);
		
		return lModules;
	}
}
