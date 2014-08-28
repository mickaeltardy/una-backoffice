package com.mtdev.una.tools;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.DigestUtils;

public abstract class Toolbox {

	public static Map<Object, Object> generateResult(Object... pInput) {
		if (pInput != null && pInput.length % 2 == 0) {
			Map<Object, Object> lOutput = new HashMap<Object, Object>();

			for (int i = 0; i < pInput.length; i += 2) {
				lOutput.put(pInput[i], pInput[i + 1]);
			}
			return lOutput;
		}
		return null;
	}

	public static String md5Spring(String pInput) {
		if (pInput != null)
			return DigestUtils.md5DigestAsHex(pInput.getBytes());
		else
			return null;
	}

	public static String generateRandomString(int pLength) {

		String lBase = "ABCDEFGHIJKLMNOPQRSTUVWQYZabcdefghijklmnopqrstuvwxyz1234567890";
		int lBaseLenght = lBase.length() - 1;
		int lIdx = 0;
		StringBuilder lGenString = new StringBuilder();
		for (int i = 0; i < pLength; i++) {
			lIdx = new Long(Math.round((Math.random() * lBaseLenght)))
					.intValue();
			lGenString.append(lBase.substring(lIdx, lIdx + 1));

		}

		return lGenString.toString();

	}

	public static boolean mapContainsKeys(Map<Object, Object> pData, Object... pKeys) {

		if (pData != null && pData.size() > 0 && pKeys != null
				&& pKeys.length > 0) {
			boolean lResult = true;
			for (Object pKey : pKeys) {
				if (!pData.containsKey(pKey)) {
					lResult = false;
					break;
				}
			}

			return lResult;
		}

		return false;
	}
}
