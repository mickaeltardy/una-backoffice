package com.mtdev.una.security;

import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;

import com.mtdev.una.tools.Constants.Security;
import com.mtdev.una.tools.Toolbox;

public class CustomPasswordEncoder extends PlaintextPasswordEncoder {

	@Override
	public boolean isPasswordValid(String pEncPass, String pRawPass,
			Object pSalt) {
		boolean lResult = false;
		if (pEncPass != null) {
			String[] lPassElts = pEncPass.split(Security.sPasswordsSeparator);

			for (String lPassElt : lPassElts) {
				if (Toolbox.md5Spring(pRawPass).compareTo(lPassElt) == 0) {
					lResult = true;
				}
			}
		}
		return lResult;

		// return super.isPasswordValid(pEncPass, pRawPass, pSalt);
	}

}
