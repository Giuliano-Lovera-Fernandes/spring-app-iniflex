package com.projedataInformatica.spring_app_iniflex.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class FuncionarioJsonReader {

    // Método para ler o arquivo JSON e deserializar para uma lista de Funcionarios
    public String lerFuncionariosDoArquivo(String arquivoJson) throws IOException {
        // Criação de um ObjectMapper para manipulação dos dados
        ObjectMapper objectMapper = new ObjectMapper();

        // Registra o módulo JavaTimeModule para poder lidar com LocalDate
        objectMapper.registerModule(new JavaTimeModule());

        // Lê o arquivo JSON e converte para uma lista de objetos Funcionario
        List<Funcionario> funcionarios =  objectMapper.readValue(new File(arquivoJson), objectMapper.getTypeFactory().constructCollectionType(List.class, Funcionario.class));

        return funcionarios.get(7).getNome();
    }
}


