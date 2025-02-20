package com.projedataInformatica.spring_app_iniflex.usecases;

import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface FuncionarioService {

    //void InserirFuncionarios(List<Funcionario> funcionarios);
    List<Funcionario> inserirTodosFuncionarios();

    Optional<Funcionario> removerFuncionarioPorNome(String nome);

    List<Funcionario> getFuncionariosModificados();

    StringBuilder imprimirFuncionarios(List<Funcionario> funcionarios);

    void aplicarAumentoSalarial();

    Map<String, List<Funcionario>> agruparPorFuncao();

    StringBuilder imprimirFuncionariosAgrupados(Map<String, List<Funcionario>> funcionarios);

    StringBuilder imprimirFuncionariosAniversariantesMesesEspecificos(List<Funcionario> funcionarios);

    StringBuilder imprimirFuncionarioMaisVelho(List<Funcionario> funcionarios);

    void ordenarFuncionariosPorNome();

    String calcularTotalSalarios();

    StringBuilder imprimirFuncionariosComSalariosMinimos(List<Funcionario> funcionarios);
}
