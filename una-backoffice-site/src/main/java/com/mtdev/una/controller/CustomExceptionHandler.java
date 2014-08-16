package com.mtdev.una.controller;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public @ResponseBody SystemMessage handleAllException(Exception ex) {

		SystemMessage lError = new SystemMessage("error", ex.getClass()
				.getSimpleName(), ex.getMessage());

		return lError;

	}

}
