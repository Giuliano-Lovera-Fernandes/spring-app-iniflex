package com.projedataInformatica.spring_app_iniflex.adapters;

import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import com.projedataInformatica.spring_app_iniflex.usecases.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/funcionarios")
    public String obterTodosFuncionarios() {
        return funcionarioService.obterTodosFuncionarios();
    }


}
