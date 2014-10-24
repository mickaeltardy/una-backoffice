package com.mtdev.una.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.business.interfaces.ResourcesManager;

@Component
@RequestMapping("/resources")
public class ResourcesService {
	
	@Autowired
	protected ResourcesManager mResourcesManager;

	@RequestMapping(value = "/{jsFile}.js", produces = "application/javascript;charset=utf-8")
	@ResponseBody
	public String getJavascriptFile(@PathVariable("jsFile") String pJsFileName) {
		/*
		 * TODO Add reading requested file and output the file content
		 */

		return mResourcesManager.getResourceFile("/js/"+pJsFileName+".js");
	}

	@RequestMapping(value = "/{cssFile}.css", produces = "text/css")
	@ResponseBody
	public String getCssFile(@PathVariable("cssFile") String pCssFileName) {
		return mResourcesManager.getResourceFile("/css/"+pCssFileName+".css");
	}


	@RequestMapping(value = "/{htmlFile}.html", produces = "text/html;charset=utf-8")
	@ResponseBody
	public String getHtmlFile(@PathVariable("htmlFile") String pHtmlFileName) {
		return mResourcesManager.getResourceFile("/html/"+pHtmlFileName+".html");
	}

	@RequestMapping(value = "/{datasourceFile}.datasource", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String getDatasourceFile(@PathVariable("datasourceFile") String pDatasourceFileName) {
		return mResourcesManager.getResourceFile("/datasource/"+pDatasourceFileName+".json");
	}

}
