//package com.jiawa.train.common.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//@Configuration
//public class JacksonConfig {
//    @Bean
//    public ObjectMapper jacksonObjcetMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper build = builder.createXmlMapper(false).build();
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        build.registerModule(simpleModule);
//        return build;
//    }
//}
