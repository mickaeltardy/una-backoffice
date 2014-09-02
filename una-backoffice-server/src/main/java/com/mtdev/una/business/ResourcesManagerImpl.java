package com.mtdev.una.business;

import org.springframework.stereotype.Component;

import com.mtdev.una.business.interfaces.ResourcesManager;
import com.mtdev.una.tools.FileTools;

@Component
public class ResourcesManagerImpl implements ResourcesManager {

	@Override
	public String getResourceFile(String pFileName) {
		String lResult = "";
		try {

			String lFileName = pFileName.replaceAll("-", "/");

			lResult = FileTools.readFile("/" + lFileName);

		} catch (Exception lE) {

		}
		return lResult;
	}

}
