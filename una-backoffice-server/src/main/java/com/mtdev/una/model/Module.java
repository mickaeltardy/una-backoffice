package com.mtdev.una.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "modules")
public class Module {
	
	@Id
	private String id;
	
	@Field
	private String code;
	
	@Field
	private String label;
	
	@Field
	private int order;
	
	@Field
	private List<String> roles;
	
	@Field
	private List<String> resources;
	
	public Module(){}

	public Module(String pCode, String pLabel) {
		super();
		code = pCode;
		label = pLabel;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String pCode) {
		code = pCode;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String pLabel) {
		label = pLabel;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> pRoles) {
		roles = pRoles;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> pResources) {
		resources = pResources;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int pOrder) {
		order = pOrder;
	}
	
	

}
