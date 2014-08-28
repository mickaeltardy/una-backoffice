package com.mtdev.una.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "users")
public class User {

	@Id
	private String id;

	@Field
	int status;

	@Field
	String username;

	@Field
	String password;

	@Field
	List<String> roles;

	@Field
	Map<String, String> openids;
	
	@Field
	Date insertDate;
	

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int pStatus) {
		status = pStatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String pUsername) {
		username = pUsername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pPassword) {
		password = pPassword;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date pInsertDate) {
		insertDate = pInsertDate;
	}

	
	
}