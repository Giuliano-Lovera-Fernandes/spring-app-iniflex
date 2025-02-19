package com.projedataInformatica.spring_app_iniflex.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    /*
    @Bean
    public FuncionarioService funcionarioService() {
        return new FuncionarioService();  // Retorna a implementação do serviço
    }
     */

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Registra o módulo para lidar com datas
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Desativa o uso de timestamp para datas
        return objectMapper;
    }
}
