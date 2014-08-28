package com.mtdev.una.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.mtdev.una.business.interfaces.MessagesManager;

@Component
public class MessagesManagerImpl implements MessagesManager {

	@Override
	public Object getMessages(String pLang) {
		
		Map<Object, Object> lMessages = new HashMap<Object, Object>();

		lMessages.putAll(getCommonMessages(pLang));
		lMessages.putAll(getUserSpecificMessages(pLang));
		
		return null;
	}

	protected Map<? extends Object, ? extends Object> getUserSpecificMessages(
			String pLang) {
		
		Map<Object, Object> lResult = new HashMap<Object, Object>();
		
		return lResult;
	}

	@PreAuthorize("@AccessTool.isAuthorized()")
	protected Map<? extends Object, ? extends Object> getCommonMessages(
			String pLang) {
		Map<Object, Object> lResult = new HashMap<Object, Object>();
		
		return lResult;
	}
	
	

}
