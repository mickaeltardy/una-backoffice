package com.mtdev.una.tools;

import org.springframework.util.DigestUtils;

public abstract class Toolbox {

	public static String md5Spring(String pInput) {
		if (pInput != null)
			return DigestUtils.md5DigestAsHex(pInput.getBytes());
		else
			return null;
	}

}
