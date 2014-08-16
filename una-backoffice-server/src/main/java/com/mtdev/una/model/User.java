package com.mtdev.una.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {

	@Id
	private String id;

	@Field
	String username;

	@Field
	String password;

	@Field
	List<String> roles;

	@Field
	Map<String, String> openids;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void addRole(String pRole) {
		if (this.roles == null)
			this.roles = new ArrayList<String>();
		if (pRole != null)
			this.roles.add(pRole);
	}

	public Map<String, String> getOpenids() {
		return openids;
	}

	public void setOpenids(Map<String, String> pOpenids) {
		openids = pOpenids;
	}

	public void putOpenid(String pKey, String pValue) {
		if (this.openids == null)
			this.openids = new HashMap<String, String>();

		this.openids.put(pKey, pValue);
	}
}