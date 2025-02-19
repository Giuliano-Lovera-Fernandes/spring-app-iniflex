package com.projedataInformatica.spring_app_iniflex.usecases;

import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FuncionarioService {
    String obterTodosFuncionarios();
    Funcionario obterFuncionarioPorNome(String nome);
}
