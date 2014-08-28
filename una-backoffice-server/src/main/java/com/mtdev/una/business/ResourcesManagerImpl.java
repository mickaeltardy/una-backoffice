package com.mtdev.una.business;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.mtdev.una.business.interfaces.ResourcesManager;

@Component
public class ResourcesManagerImpl implements ResourcesManager {

	@Override
	public String getResourceFile(String pFileName) {
		String lResult = "";
		try {

			String lFileName = pFileName.replaceAll("-", "/");
			URL lURL = this.getClass().getResource("/" + lFileName);

			lResult = new String(Files.readAllBytes(Paths.get(lURL.getPath())));

		} catch (Exception lE) {

		}
		return lResult;
	}

}
