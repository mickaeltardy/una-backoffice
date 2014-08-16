package com.mtdev.una.controller;

import java.util.HashMap;
import java.util.Map;

public class SystemMessage {

	public Map<Object, Object> error;

	public SystemMessage(String pType, Object pCode, String pMessage) {
		super();
		error = new HashMap<Object, Object>();
		error.put("type", pType);
		error.put("code", pCode);
		error.put("message", pMessage);
	}

	public void addDetail(Object pKey, Object pValue) {
		if (error == null)
			error = new HashMap<Object, Object>();
		error.put(pKey, pValue);
	}

}
