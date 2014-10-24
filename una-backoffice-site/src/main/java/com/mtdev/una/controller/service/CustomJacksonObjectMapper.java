package com.mtdev.una.controller.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtdev.una.tools.JsonDateSerializer;

@Component("customJacksonMapper")
public class CustomJacksonObjectMapper extends ObjectMapper {
/*
    public CustomJacksonObjectMapper(){
        CustomSerializerFactory factory = new CustomSerializerFactory();
        factory.addSpecificMapping(Date.class, new JsonDateSerializer());
        
        this.setSerializerFactory(factory);
    }
*/
}