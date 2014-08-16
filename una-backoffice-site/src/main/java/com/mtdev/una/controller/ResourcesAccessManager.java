package com.mtdev.una.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@RequestMapping("/resources")
public class ResourcesAccessManager {

	@RequestMapping(value = "/{jsFile}.js", produces = "application/javascript")
	@ResponseBody
	@PreAuthorize("@AccessTool.isOk()")
	public String getJavascriptFile(@PathVariable("jsFile") String pJsFileName) {
		/*
		 * TODO Add reading requested file and output the file content
		 */

		return "alert('" + pJsFileName + "'	)";
	}

	@RequestMapping(value = "/{cssFile}.css", produces = "text/css")
	@ResponseBody
	@PreAuthorize("@AccessTool.isOk()")
	public String getCssFile(@PathVariable("cssFile") String pCssFileName) {
		return "body{background:#ff0000;}";
	}

}
