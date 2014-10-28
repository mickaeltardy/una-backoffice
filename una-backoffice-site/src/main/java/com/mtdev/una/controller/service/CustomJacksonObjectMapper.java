package com.mtdev.una.controller.service;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

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