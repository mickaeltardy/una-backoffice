package com.mtdev.una.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	
	@Override
	public void onLogoutSuccess(HttpServletRequest pRequest,
			HttpServletResponse pResponse, Authentication pAuthentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		pResponse.getWriter().println("{\"status\": \"logout\"}");
	}

	
}