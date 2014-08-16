package com.mtdev.una.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.data.view.ResponseView;
import com.mtdev.una.data.view.Views;
import com.mtdev.una.data.view.Views.Internal;

@Controller
public class DataServiceManager implements ServiceManager {

	private final ObjectMapper mObjectMaper = new ObjectMapper();

	@RequestMapping(value = "/containters", produces = "application/json")
	@ResponseView(Views.Internal.class)
	public @ResponseBody List<Container> getContainers(
			@RequestParam(value = "itemCnt", defaultValue = "0") Integer pItemCnt)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<Container> lResult = new ArrayList<Container>();
		for (int i = 1; i <= pItemCnt; i++) {
			lResult.add(new Container(i, "containerName_" + i, "data_" + i,
					null));
		}
		return lResult;

	}

	/*
	 * @RequestMapping(value = "/containters", produces = "application/json")
	 * 
	 * @ResponseBody public String getContainers(
	 * 
	 * @RequestParam(value = "itemCnt", defaultValue = "0") Integer pItemCnt)
	 * throws JsonGenerationException, JsonMappingException, IOException {
	 * List<Container> lResult = new ArrayList<Container>(); for (int i = 1; i
	 * <= pItemCnt; i++) { lResult.add(new Container(i, "containerName_" + i,
	 * "data_" + i, null)); } return getViewAwareJson(lResult,
	 * Views.Internal.class);
	 * 
	 * }
	 */

	private String getViewAwareJson(Object pData, Class<Internal> pView)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectWriter lObjectWriter = mObjectMaper.writerWithView(pView);
		return lObjectWriter.writeValueAsString(pData);
	}

	/*
	 * private String getViewAwareJson(){
	 * 
	 * }
	 */

	//
	// @RequestMapping(value = "/containters", produces = "application/json")
	// @ResponseBody
	// public List<Container> getContainers(@RequestParam(value = "itemCnt",
	// defaultValue="0") Integer pItemCnt){
	// List<Container> lResult = new ArrayList<Container>();
	// for(int i = 1; i <=pItemCnt; i++){
	// lResult.add(new Container(i, "containerName_"+i, "data_"+i, null));
	// }
	//
	// return lResult;
	// }

}
