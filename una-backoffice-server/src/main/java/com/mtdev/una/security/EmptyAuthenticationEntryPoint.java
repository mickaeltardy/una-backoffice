package com.mtdev.una.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class EmptyAuthenticationEntryPoint implements AuthenticationEntryPoint {

	public void commence(HttpServletRequest pRequest,
			HttpServletResponse pResponse, AuthenticationException pException)
			throws IOException, ServletException {

	}

}