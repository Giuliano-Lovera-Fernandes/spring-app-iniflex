package com.projedataInformatica.spring_app_iniflex.usecases;

import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface FuncionarioService {

    List<Funcionario> inserirFuncionarios();

    Optional<Funcionario> removerFuncionarioNome(String nome);

    List<Funcionario> ListarFuncionariosModificados();

    StringBuilder imprimirInformacoesFuncionarios(List<Funcionario> funcionarios);

    StringBuilder aplicarAumentoSalarial(BigDecimal percentualAumento);

    Map<String, List<Funcionario>> agruparFuncionariosFuncao();

    StringBuilder imprimirFuncionariosAgrupadosFuncao(Map<String, List<Funcionario>> funcionarios);

    StringBuilder imprimirFuncionariosAniversariantesMesesEspecificos(List<Funcionario> funcionarios, int primeiroMes, int segundoMes);

    StringBuilder imprimirFuncionarioMaiorIdade (List<Funcionario> funcionarios);

    void ordenarFuncionariosNome();

    String calcularTotalSalariosFuncionarios();

    StringBuilder imprimirFuncionariosSalariosMinimos(List<Funcionario> funcionarios, BigDecimal salarioMinimo);
}
