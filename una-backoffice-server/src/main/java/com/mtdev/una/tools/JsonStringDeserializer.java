package com.mtdev.una.tools;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class JsonStringDeserializer extends JsonDeserializer<Object> {

	@Override
	public Object deserialize(JsonParser pJp, DeserializationContext pCtxt)
			throws IOException, JsonProcessingException {

		SimpleDateFormat format = new SimpleDateFormat(Constants.dateFormat);
		String date = pJp.getText();
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return pJp.getValueAsString();
		}
	}

}