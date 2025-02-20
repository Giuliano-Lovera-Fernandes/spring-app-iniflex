package com.projedataInformatica.spring_app_iniflex.adapters.controllers;

import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import com.projedataInformatica.spring_app_iniflex.usecases.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;
/*
    @GetMapping("/funcionarios")
    public String obterTodosFuncionarios() {
        return funcionarioService.obterTodosFuncionarios();
    }
 */

    @GetMapping("/")
    public String inserirTodosFuncionarios() {

        //3.1 – Inserir todos os funcionários, na mesma ordem e informações da tabela acima.
        // Pensei nessa solução, criando um arquivo json com todos os dados da tabela e lendo o arquivo
        //List<Funcionario> funcionarios = funcionarioService.getFuncionariosModificados();
        List<Funcionario> funcionarios = funcionarioService.inserirTodosFuncionarios();

        //3.2 – Remover o funcionário “João” da lista.
        funcionarioService.removerFuncionarioPorNome("João");

        List<Funcionario> funcionariosModificados = funcionarioService.getFuncionariosModificados();
        Locale brasil = new Locale("pt", "BR");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(brasil);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        StringBuilder html = new StringBuilder();

        // Data formatada
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Formatação de valores monetários com separador de milhar e vírgula como separador decimal
        DecimalFormat salarioFormatter = new DecimalFormat("#,##0.00", symbols);

        html.append("<html><head><title>Lista de Funcionários</title></head><body>");
        html.append("<h1>Lista de Funcionários com a remoção de João</h1>");
        html.append(funcionarioService.imprimirFuncionarios(funcionariosModificados));
        /*
        html.append("<table><thead><tr><th>funcionarios</th><th>Data de Nascimento</th><th>Salário</th></tr></thead><tbody>");
        //html.append("<p>").append(funcionarios).append("</p>");

        for (Funcionario f : funcionariosModificados) {
            String dataFormatada = f.getDataNascimento().format(dataFormatter);

            // Formatar o salário com vírgula como separador decimal
            String salarioFormatado = salarioFormatter.format(f.getSalario());

            html.append("<tr><td>").append(f.getNome()).append("</td>");
            html.append("<td>").append(dataFormatada).append("</td>");
            html.append("<td>").append(salarioFormatado).append("</td></tr>");
        }


        html.append("</tbody></table></body></html>");
        */
        //funcionarios = funcionarioService.inserirTodosFuncionarios();


        // Imprime a lista após a remoção
        html.append("<h1>Lista de Funcionários (Após a remoção de João)</h1>");

        /*
        html.append("<table><thead><tr><th>Funcionários</th><th>Data de Nascimento</th><th>Salário</th></tr></thead><tbody>");

        // Itera sobre a lista de funcionários atualizada e monta as linhas da tabela
        for (Funcionario f : funcionarios) {
            html.append("<tr><td>").append(f.getNome()).append("</td>");
            html.append("<td>").append(f.getDataNascimento()).append("</td>");
            html.append("<td>").append(f.getSalario()).append("</td></tr>");
        }


        html.append("</tbody></table></body></html>");
         */
        html.append(funcionarioService.imprimirFuncionarios(funcionarios));
        funcionarioService.aplicarAumentoSalarial();
        html.append("<h1>Lista de Funcionários (Após aumento salarial)</h1>");
        html.append(funcionarioService.imprimirFuncionarios(funcionarios));
        html.append("<h1>Lista de Funcionários (Agrupar funcionários)</h1>");
        Map<String, List<Funcionario>> funcionariosAgrupados= funcionarioService.agruparPorFuncao();
        html.append(funcionarioService.imprimirFuncionariosAgrupados(funcionariosAgrupados));
        html.append("<h1>Lista de Funcionários (Funcionários Aniversariantes)</h1>");
        StringBuilder funcionariosAniversariantes = funcionarioService.imprimirFuncionariosAniversariantesMesesEspecificos(funcionarios);
        html.append(funcionariosAniversariantes);

        html.append("<h1>Lista de Funcionários (Funcionário mais velho)</h1>");
        StringBuilder funcionarioMaisVelho = funcionarioService.imprimirFuncionarioMaisVelho(funcionarios);
        html.append(funcionarioMaisVelho);

        html.append("<h1>Lista de Funcionários (Lista Ordenada Alfabeticamente)</h1>");
        funcionarioService.ordenarFuncionariosPorNome();
        html.append(funcionarioService.imprimirFuncionarios(funcionarios));

        String totalSalarios = funcionarioService.calcularTotalSalarios();
        html.append("<h2>Total dos Salários</h2>");
        html.append("<p>").append(totalSalarios).append("</p>");

        html.append("<h1>Lista de Funcionários (Lista com a quantidade de salários mínimos)</h1>");
        StringBuilder listaQuantidadeSalarios = funcionarioService.imprimirFuncionariosComSalariosMinimos(funcionarios);
        html.append(listaQuantidadeSalarios);

        return html.toString();  // Retorna o HTML gerado
    }

}
