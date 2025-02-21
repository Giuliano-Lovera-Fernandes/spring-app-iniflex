package com.projedataInformatica.spring_app_iniflex.adapters.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;

import org.springframework.stereotype.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class FuncionarioJsonReader {

    //Método para ler o arquivo JSON e deserializar para uma lista de Funcionarios
    public List<Funcionario> lerFuncionariosDoArquivo(String arquivoJson) throws IOException {
        //Verifica se o caminho do arquivo não é nulo ou vazio
        if (arquivoJson == null || arquivoJson.trim().isEmpty()) {
            throw new IllegalArgumentException("O caminho do arquivo não pode ser nulo ou vazio.");
        }

        //Criação de um ObjectMapper para manipulação dos dados
        ObjectMapper objectMapper = new ObjectMapper();

        //Registra o módulo JavaTimeModule para poder lidar com LocalDate
        objectMapper.registerModule(new JavaTimeModule());

        //Usando Path em vez de File
        Path path = Paths.get(arquivoJson);

        //Verifica se o arquivo existe antes de tentar lê-lo
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Arquivo não encontrado: " + arquivoJson);
        }

        //Lê o arquivo JSON e converte para uma lista de objetos Funcionario
        try {
            return objectMapper.readValue(path.toFile(), objectMapper.getTypeFactory().constructCollectionType(List.class, Funcionario.class));
        } catch (IOException e) {
            //Log ou tratamento adicional de erro
            throw new RuntimeException("Erro ao ler o arquivo JSON: " + arquivoJson, e);
        }
    }
}


