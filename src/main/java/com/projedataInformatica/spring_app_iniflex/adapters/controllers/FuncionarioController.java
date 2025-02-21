package com.projedataInformatica.spring_app_iniflex.adapters.controllers;

import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import com.projedataInformatica.spring_app_iniflex.domain.TituloRelatorio;
import com.projedataInformatica.spring_app_iniflex.domain.TituloRelatorioStyle;
import com.projedataInformatica.spring_app_iniflex.usecases.FuncionarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;


    @GetMapping("/")
    public String gerarRelatorioFuncionarios() {

        /*3.1 – Inserir todos os funcionários, na mesma ordem e informações da tabela acima.
        // Pensei nessa solução, criando um arquivo json com todos os dados da tabela e lendo o arquivo
         */
        List<Funcionario> funcionarios = funcionarioService.inserirFuncionarios();

        //3.2 – Remover o funcionário “João” da lista.
        String nomeFuncionario = "João";
        funcionarioService.removerFuncionarioNome(nomeFuncionario);
        List<Funcionario> funcionariosModificados = funcionarioService.ListarFuncionariosModificados();

        StringBuilder html = new StringBuilder();
        html.append(TituloRelatorio.RELATORIO_TITLE);
        html.append(TituloRelatorioStyle.ESTILO_CSS);

        html.append(TituloRelatorio.RELATORIO);
        html.append(String.format(TituloRelatorio.REMOCAO_FUNCIONARIO, nomeFuncionario));
        html.append(funcionarioService.imprimirInformacoesFuncionarios(funcionariosModificados));

        //3.3 – Imprimir todos os funcionários com todas suas informações, sendo que:
        //  - informação de data deve ser exibido no formato dd/mm/aaaa;
        //  - informação de valor numérico deve ser exibida no formatado com separador de milhar como ponto e decimal como vírgula.
        html.append(TituloRelatorio.LISTA_TODOS_FUNCIONARIOS);
        html.append(funcionarioService.imprimirInformacoesFuncionarios(funcionarios));

        //3.4 – Os funcionários receberam 10% de aumento de salário, atualizar a lista de funcionários com novo valor.
        BigDecimal aumentoPercentual = new BigDecimal("1.10");
        StringBuilder resultadoAumento = funcionarioService.aplicarAumentoSalarial(aumentoPercentual);
        html.append(TituloRelatorio.AUMENTO_SALARIAL);
        html.append(funcionarioService.imprimirInformacoesFuncionarios(funcionarios));
        html.append(resultadoAumento);

        //3.5 – Agrupar os funcionários por função em um MAP, sendo a chave a “função” e o valor a “lista de funcionários”.
        //3.6 – Imprimir os funcionários, agrupados por função.
        html.append(TituloRelatorio.AGRUPAMENTO_FUNCAO);
        Map<String, List<Funcionario>> funcionariosAgrupados= funcionarioService.agruparFuncionariosFuncao();
        html.append(funcionarioService.imprimirFuncionariosAgrupadosFuncao(funcionariosAgrupados));

        //3.8 – Imprimir os funcionários que fazem aniversário no mês 10 e 12.
        int primeiroMes = 10;
        int segundoMes = 12;
        //html.append("<h1>Lista de Funcionários (Funcionários Aniversariantes)</h1>");
        StringBuilder funcionariosAniversariantes = funcionarioService.imprimirFuncionariosAniversariantesMesesEspecificos(funcionarios, primeiroMes, segundoMes);
        html.append(funcionariosAniversariantes);

        //html.append("<h1>Funcionário de maior idade</h1>");
        //3.9 – Imprimir o funcionário com a maior idade, exibir os atributos: nome e idade.
        StringBuilder funcionarioMaisVelho = funcionarioService.imprimirFuncionarioMaiorIdade(funcionarios);
        html.append(funcionarioMaisVelho);

        //3.10 – Imprimir a lista de funcionários por ordem alfabética.
        html.append(TituloRelatorio.LISTA_ORDENADA);
        funcionarioService.ordenarFuncionariosNome();
        html.append(funcionarioService.imprimirInformacoesFuncionarios(funcionarios));

        //3.11 – Imprimir o total dos salários dos funcionários.
        String totalSalarios = funcionarioService.calcularTotalSalariosFuncionarios();
        html.append(TituloRelatorio.TOTAL_SALARIOS);
        html.append("<p>").append(totalSalarios).append("</p>");

        //3.12 – Imprimir quantos salários mínimos ganha cada funcionário, considerando que o salário mínimo é R$1212.00.
        html.append(TituloRelatorio.SALARIOS_MINIMOS);
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        StringBuilder listaQuantidadeSalarios = funcionarioService.imprimirFuncionariosSalariosMinimos(funcionarios, salarioMinimo);
        html.append(listaQuantidadeSalarios);

        //html.append("body { background-color: #D3D3D3; font-family: Arial, sans-serif; color: black; }"); // Cor de fundo cinza claro
        return html.toString();  // Retorna o HTML gerado
    }

}
