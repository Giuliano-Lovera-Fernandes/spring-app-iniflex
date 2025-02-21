package com.projedataInformatica.spring_app_iniflex.usecases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projedataInformatica.spring_app_iniflex.adapters.controllers.FuncionarioJsonReader;
import com.projedataInformatica.spring_app_iniflex.domain.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuncionarioImplementation implements FuncionarioService{

    @Autowired
    private FuncionarioJsonReader funcionarioJsonReader;

    private List<Funcionario> funcionarios = new ArrayList<>();
    private List<Funcionario> funcionariosModificados = new ArrayList<>();

    private static final String CAMINHO_ARQUIVO = "src/main/resources/funcionarios.json";


    @Override
    public List<Funcionario> inserirFuncionarios() {
        try {
            funcionarios = funcionarioJsonReader.lerFuncionariosDoArquivo(CAMINHO_ARQUIVO);
            funcionariosModificados = new ArrayList<>(funcionarios);
            return funcionarios;
        } catch (IOException e) {
            e.printStackTrace();

            return List.of();
        }
    }

    @Override
    public Optional<Funcionario> removerFuncionarioNome(String nome) {
        try {
            boolean funcionarioRemovido = funcionariosModificados.removeIf(f -> f.getNome().equalsIgnoreCase(nome));
            if (funcionarioRemovido) {
                //salvarFuncionariosNoArquivo(funcionariosModificados);

                return funcionarios.stream()
                        .filter(f -> f.getNome().equalsIgnoreCase(nome))
                        .findFirst();
            } else {
                System.out.println("Funcionário " + nome + " não encontrado!");
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao tentar remover o funcionário.");

            return Optional.empty();
        }
    }

    // Método auxiliar para salvar os funcionários no arquivo JSON, em caso de necessidade de persistência
    private void salvarFuncionariosNoArquivo(List<Funcionario> funcionarios) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.writeValue(new File(CAMINHO_ARQUIVO), funcionarios);
    }

    public List<Funcionario> ListarFuncionariosModificados() {

        return (funcionariosModificados != null && !funcionariosModificados.isEmpty())
                ? funcionariosModificados
                : new ArrayList<>();
    }

    @Override
    public StringBuilder imprimirInformacoesFuncionarios(List<Funcionario> funcionarios) {
        StringBuilder html = new StringBuilder();
        //Configuração de formatação de números e datas
        DecimalFormatSymbols simbolos = configurarFormatoNumero();
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat salarioFormatoDecimal = new DecimalFormat("#,##0.00", simbolos);
        //Cabeçalho da tabela HTML
        html.append("<table><thead><tr><th>Funcionários</th><th>Data de Nascimento</th><th>Salário</th><th>Função</th></tr></thead><tbody>");

        //Preenchendo as linhas da tabela
        for (Funcionario f : funcionarios) {
            html.append(criarLinhaFuncionario(f, dataFormato, salarioFormatoDecimal));
        }

        html.append("</tbody></table>");

        return html;
    }

    //Método privado para criar cada linha de funcionário da tabela de exibição
    private StringBuilder criarLinhaFuncionario(Funcionario f, DateTimeFormatter dataFormato, DecimalFormat salarioFormatoDecimal) {
        StringBuilder linha = new StringBuilder();

        //Formata as informações do funcionário
        String dataFormatada = f.getDataNascimento().format(dataFormato);
        String salarioFormatado = salarioFormatoDecimal.format(f.getSalario());

        //Adiciona os dados do funcionário na linha
        linha.append("<tr><td>").append(f.getNome()).append("</td>");
        linha.append("<td>").append(dataFormatada).append("</td>");
        linha.append("<td>").append(salarioFormatado).append("</td>");
        linha.append("<td>").append(f.getFuncao()).append("</td></tr>");

        return linha;
    }

    //Método privado para configurar o formato numérico
    private DecimalFormatSymbols configurarFormatoNumero() {
        //Configuração de idioma e região de acordo com a localidade
        Locale brasil = new Locale("pt", "BR");

        //Classe que fornece os símbolos usados para formatação e análise de números
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(brasil);
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');

        return simbolos;
    }

    //comentario
    @Override
    public StringBuilder aplicarAumentoSalarial(BigDecimal percentualAumento) {
        StringBuilder html = new StringBuilder();
        if (funcionarios == null || funcionarios.isEmpty()) {
            html.append("<h4>Não há funcionários para aplicar aumento salarial.</h4>");
            return html; // Retorna a mensagem em HTML
        }

        //Aplica um aumento de 10% no salário de cada funcionário
        for (Funcionario f : funcionarios) {
            BigDecimal salarioAtual = f.getSalario();
            BigDecimal novoSalario = salarioAtual.multiply(percentualAumento).setScale(2, RoundingMode.HALF_UP);;
            f.setSalario(novoSalario);
        }

        html.append("<h3>Aumento salarial aplicado com sucesso.</h3>");
        return html;
        //Se estiver mantendo uma lista modificada, atualize-a também
        //funcionariosModificados = new ArrayList<>(funcionarios);
    }

    @Override
    public Map<String, List<Funcionario>> agruparFuncionariosFuncao() {
        //Stream API do Java, maneira funcional e eficiente de manipular coleções de dados
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    @Override
    public StringBuilder imprimirFuncionariosAgrupadosFuncao(Map<String, List<Funcionario>> funcionariosPorFuncao) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Funcionários Agrupados por Função</title></head><body>");
        html.append("<h1>Funcionários Agrupados por Função</h1>");

        DecimalFormatSymbols simbolos = configurarFormatoNumero();
        DecimalFormat salarioFormatter = new DecimalFormat("R$ #,##0.00", simbolos);

        for (Map.Entry<String, List<Funcionario>> entry : funcionariosPorFuncao.entrySet()) {

            html.append("<h2>").append(entry.getKey()).append("</h2>"); //Exibe o nome da função (chave)
            html.append("<table><thead><tr><th>Funcionário</th><th>Data de Nascimento</th><th>Salário</th></tr></thead><tbody>");

            for (Funcionario f : entry.getValue()) {
                String salarioFormatado = salarioFormatter.format(f.getSalario());
                //Exibe cada funcionário na função
                html.append("<tr><td>").append(f.getNome()).append("</td>");
                html.append("<td>").append(f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>");
                html.append("<td>").append(salarioFormatado).append("</td></tr>");
            }

            html.append("</tbody></table>");
        }

        html.append("</body></html>");
        return html;
    }

    public StringBuilder imprimirFuncionariosAniversariantesMesesEspecificos(List<Funcionario> funcionarios, int primeiroMes, int segundoMes) {
        StringBuilder html = new StringBuilder();
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");  //Formatação de data
        DecimalFormatSymbols simbolos = configurarFormatoNumero();
        DecimalFormat salarioFormato = new DecimalFormat("#,##0.00", simbolos);

        String cabecalhoTabela = String.format("<h1>Funcionários Aniversariantes: Meses %d e %d</h1>", primeiroMes, segundoMes);
        html.append(cabecalhoTabela);
        html.append("<table><thead><tr><th>Funcionários</th><th>Data de Nascimento</th><th>Salário</th><th>Função</th></tr></thead><tbody>");
        boolean encontrouAniversariante = false;

        for (Funcionario f : funcionarios) {
            //Verificar se o mês de nascimento é outubro (10) ou dezembro (12)
            int mesNascimento = f.getDataNascimento().getMonthValue();  //Pega o mês da data de nascimento

            if (mesNascimento == primeiroMes || mesNascimento == segundoMes) {
                encontrouAniversariante = true;

                StringBuilder linhaFuncionario = criarLinhaFuncionario(f, dataFormato, salarioFormato);  // Cria linha formatada
                html.append(linhaFuncionario);
            }

            if(!encontrouAniversariante) {
                html.setLength(0); //Limpa o conteúdo da StringBuilder
                html.append(cabecalhoTabela);
                html.append("<tr><td colspan='4'>Nenhum aniversariante encontrado nos meses especificados.</td></tr>");
            }
        }

        html.append("</tbody></table>");

        return html;
    }

    public StringBuilder imprimirFuncionarioMaiorIdade(List<Funcionario> funcionarios) {
        StringBuilder html = new StringBuilder();
        Funcionario maisVelho = null;
        int maiorIdade = 0;

        for (Funcionario f : funcionarios) {
            LocalDate dataNascimento = f.getDataNascimento();
            int idade = Period.between(dataNascimento, LocalDate.now()).getYears(); // Calcula a idade

            if (idade > maiorIdade) {
                maiorIdade = idade;
                maisVelho = f; //Armazena o funcionário de maior idade
            }
        }

        html.append("<h1>Funcionário de maior idade</h1>");

        //Se encontrou o funcionário de maior idade, exibe os dados
        if (maisVelho != null) {
            html.append("<p>Funcionário com a maior idade:</p>");
            html.append("<p>Nome: ").append(maisVelho.getNome()).append("</p>");
            html.append("<p>Idade: ").append(maiorIdade).append(" anos</p>");
        } else {
            html.append("<p>Não há funcionários cadastrados.</p>");
        }

        return html;
    }

    @Override
    public void ordenarFuncionariosNome() {
        if (funcionarios != null && !funcionarios.isEmpty()) {
            funcionarios.sort(Comparator.comparing(Funcionario::getNome));
        } else {
            System.out.println("Não há funcionários para ordenar.");
        }
    }

    @Override
    public String calcularTotalSalariosFuncionarios() {
        //Calcula a soma dos salários de todos os funcionários
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Locale brasil = new Locale("pt", "BR");
        DecimalFormatSymbols simbolos = configurarFormatoNumero();

        DecimalFormat salarioFormatter = new DecimalFormat("#,##0.00", simbolos);

        return "<p>" + salarioFormatter.format(totalSalarios) + "</p>";  //Retorna o valor formatado como String
    }

    public StringBuilder imprimirFuncionariosSalariosMinimos(List<Funcionario> funcionarios, BigDecimal salarioMinimo) {
        StringBuilder html = new StringBuilder();
        Locale brasil = new Locale("pt", "BR");
        DecimalFormatSymbols symbols = configurarFormatoNumero();
        DecimalFormat salarioFormatter = new DecimalFormat("#,##0.00", symbols);

        html.append("<table><thead><tr><th>Funcionários</th><th>Data de Nascimento</th><th>Salário</th><th>Função</th><th>Salários Mínimos</th></tr></thead><tbody>");

        for (Funcionario f : funcionarios) {
            BigDecimal salariosMinimos = calcularSalariosMinimos(f.getSalario(), salarioMinimo);

            html.append(criarLinhaFuncionarioFuncao(f, salarioFormatter, salariosMinimos));
        }

        html.append("</tbody></table>");

        return html;
    }

    // Método privado para calcular os salários mínimos
    private BigDecimal calcularSalariosMinimos(BigDecimal salario, BigDecimal salarioMinimo) {
        return salario.divide(salarioMinimo, 2, RoundingMode.HALF_UP);
    }

    private StringBuilder criarLinhaFuncionarioFuncao(Funcionario f, DecimalFormat salarioFormatter, BigDecimal salariosMinimos) {
        StringBuilder linha = new StringBuilder();
        String dataFormatada = f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String salarioFormatado = salarioFormatter.format(f.getSalario());

        DecimalFormatSymbols simbolos = configurarFormatoNumero();
        DecimalFormat salarioMinimoFormatter = new DecimalFormat("#,##0.00", simbolos);
        String salarioMinimoFormatado = salarioMinimoFormatter.format(salariosMinimos);

        linha.append("<tr><td>").append(f.getNome()).append("</td>");
        linha.append("<td>").append(dataFormatada).append("</td>");
        linha.append("<td>").append(salarioFormatado).append("</td>");
        linha.append("<td>").append(f.getFuncao()).append("</td>");
        linha.append("<td>").append(salarioMinimoFormatado).append("</td></tr>");

        return linha;
    }
}
