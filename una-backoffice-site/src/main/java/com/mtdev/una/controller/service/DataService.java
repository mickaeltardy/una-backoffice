package com.mtdev.una.controller.service;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.business.interfaces.MessagesManager;
import com.mtdev.una.data.view.ResponseView;
import com.mtdev.una.data.view.Views;

@Controller
@RequestMapping("/data")
public class DataService {

	@Autowired
	protected MessagesManager mMessagesManager;

	@RequestMapping(value = "/messages", produces = "application/json")
	@ResponseView(Views.Internal.class)
	public @ResponseBody Object getMessages(
			@RequestParam(value = "lang", defaultValue = "fr") String pLang)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		return mMessagesManager.getMessages(pLang);

	}

}
