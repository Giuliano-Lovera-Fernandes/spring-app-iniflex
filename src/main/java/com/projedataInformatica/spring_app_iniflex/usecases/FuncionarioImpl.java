package com.projedataInformatica.spring_app_iniflex.usecases;

import com.projedataInformatica.spring_app_iniflex.adapters.FuncionarioJsonReader;
import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FuncionarioImpl implements FuncionarioService{

    @Autowired
    private FuncionarioJsonReader funcionarioJsonReader;

    @Override
    public String obterTodosFuncionarios() {
        try {
            // Chama o método lerFuncionariosDoArquivo para ler o arquivo JSON e retornar a lista
            String caminhoArquivo = "src/main/resources/funcionarios.json"; // Localizado diretamente na pasta resources

            return funcionarioJsonReader.lerFuncionariosDoArquivo(caminhoArquivo);
        } catch (IOException e) {
            // Caso aconteça algum erro ao ler o arquivo
            e.printStackTrace();
            //return List.of();
            //Retorna uma lista vazia em caso de erro
            return "";
        }
    }

    @Override
    public Funcionario obterFuncionarioPorNome(String nome) {
        return null;
    }
}
