package com.mtdev.una.tools;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class CalendarMessageConverter implements HttpMessageConverter<Object> {

	@Override
	public boolean canRead(Class<?> pClazz, MediaType pMediaType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canWrite(Class<?> pClazz, MediaType pMediaType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object read(Class<? extends Object> pClazz,
			HttpInputMessage pInputMessage) throws IOException,
			HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(Object pT, MediaType pContentType,
			HttpOutputMessage pOutputMessage) throws IOException,
			HttpMessageNotWritableException {
		// TODO Auto-generated method stub
		
	}

}
