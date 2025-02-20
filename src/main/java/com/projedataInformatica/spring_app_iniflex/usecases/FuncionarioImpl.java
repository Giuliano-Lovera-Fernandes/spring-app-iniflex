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
public class FuncionarioImpl implements FuncionarioService{

    @Autowired
    private FuncionarioJsonReader funcionarioJsonReader;

    // Lista interna para armazenar os funcionários
    private List<Funcionario> funcionarios = new ArrayList<>();
    private List<Funcionario> funcionariosModificados = new ArrayList<>();

    // Caminho fixo do arquivo
    private static final String CAMINHO_ARQUIVO = "src/main/resources/funcionarios.json";


    @Override
    public List<Funcionario> inserirTodosFuncionarios() {
        try {
            // Chama o método lerFuncionariosDoArquivo para ler o arquivo JSON e retornar a lista
            //String caminhoArquivo = "src/main/resources/funcionarios.json"; // Localizado diretamente na pasta resources

            funcionarios = funcionarioJsonReader.lerFuncionariosDoArquivo(CAMINHO_ARQUIVO);
            funcionariosModificados = new ArrayList<>(funcionarios);
            return funcionarios;
        } catch (IOException e) {
            // Caso aconteça algum erro ao ler o arquivo
            e.printStackTrace();
            return List.of();
            //Retorna uma lista vazia em caso de erro
            //return "";
        }
    }

    //@Override
    //public void InserirTodosFuncionarios(List<Funcionario> funcionarios){
        //String caminhoArquivo = "src/main/resources/funcionarios.json";
    //}

    @Override
    public Optional<Funcionario> removerFuncionarioPorNome(String nome) {
        //nome = "João";
        try {
            // Obtém todos os funcionários
            //List<Funcionario> funcionarios = inserirTodosFuncionarios();
            //funcionariosModificados = new ArrayList<>(funcionarios);
            // Usando removeIf para remover o primeiro funcionário que corresponde ao nome
            boolean funcionarioRemovido = funcionariosModificados.removeIf(f -> f.getNome().equalsIgnoreCase(nome));

            if (funcionarioRemovido) {
                // Retorna o funcionário removido usando stream

                //salvarFuncionariosNoArquivo(funcionariosModificados);

                return funcionarios.stream()
                        .filter(f -> f.getNome().equalsIgnoreCase(nome))
                        .findFirst();
            } else {
                // Caso o funcionário não seja encontrado
                System.out.println("Funcionário " + nome + " não encontrado!");
                return Optional.empty();
            }

        } catch (Exception e) {
            // Trata qualquer erro que possa ocorrer
            e.printStackTrace();
            System.out.println("Erro ao tentar remover o funcionário.");
            return Optional.empty();
        }
    }

    // Método auxiliar para salvar os funcionários no arquivo JSON
    private void salvarFuncionariosNoArquivo(List<Funcionario> funcionarios) throws IOException {
        // Usando ObjectMapper para escrever a lista no arquivo
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Se necessário para LocalDate

        // Salva a lista de funcionários no arquivo
        objectMapper.writeValue(new File(CAMINHO_ARQUIVO), funcionarios);
    }

    public List<Funcionario> getFuncionariosModificados() {
        return funcionariosModificados;
    }

    @Override
    public StringBuilder imprimirFuncionarios(List<Funcionario> funcionarios) {
        StringBuilder html = new StringBuilder();

        // Configuração de Locale para o Brasil
        Locale brasil = new Locale("pt", "BR");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(brasil);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        // Formatação de data
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Formatação do salário
        DecimalFormat salarioFormatter = new DecimalFormat("#,##0.00", symbols);

        // Tabela HTML
        html.append("<table><thead><tr><th>Funcionários</th><th>Data de Nascimento</th><th>Salário</th><th>Função</th></tr></thead><tbody>");

        // Itera sobre os funcionários e formata as informações
        for (Funcionario f : funcionarios) {
            String dataFormatada = f.getDataNascimento().format(dataFormatter);  // Formata a data
            String salarioFormatado = salarioFormatter.format(f.getSalario());    // Formata o salário

            html.append("<tr><td>").append(f.getNome()).append("</td>");
            html.append("<td>").append(dataFormatada).append("</td>");
            html.append("<td>").append(salarioFormatado).append("</td>");
            html.append("<td>").append(f.getFuncao()).append("</td></tr>");
        }

        html.append("</tbody></table>");

        return html;
    }

    @Override
    public void aplicarAumentoSalarial() {
        // Aplica um aumento de 10% no salário de cada funcionário
        for (Funcionario f : funcionarios) {
            BigDecimal salarioAtual = f.getSalario();  // Salário atual como BigDecimal
            BigDecimal aumento = new BigDecimal("1.10");  // Aumento de 10%
            BigDecimal novoSalario = salarioAtual.multiply(aumento).setScale(2, RoundingMode.HALF_UP);;  // Aplica o aumento
            f.setSalario(novoSalario);  // Atualiza o salário do funcionário

        }

        // Se estiver mantendo uma lista modificada, atualize-a também
        funcionariosModificados = new ArrayList<>(funcionarios);

        // Atualiza os funcionários no arquivo ou banco de dados
        // Aqui você pode adicionar a lógica para salvar as alterações, caso necessário
    }

    @Override
    public Map<String, List<Funcionario>> agruparPorFuncao() {
        // Agrupa os funcionários por função usando Streams
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    @Override
    public StringBuilder imprimirFuncionariosAgrupados(Map<String, List<Funcionario>> funcionariosPorFuncao) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><title>Funcionários Agrupados por Função</title></head><body>");
        sb.append("<h1>Funcionários Agrupados por Função</h1>");

        for (Map.Entry<String, List<Funcionario>> entry : funcionariosPorFuncao.entrySet()) {
            sb.append("<h2>").append(entry.getKey()).append("</h2>"); // Exibe o nome da função (chave)
            sb.append("<table><thead><tr><th>Funcionário</th><th>Data de Nascimento</th><th>Salário</th></tr></thead><tbody>");

            for (Funcionario f : entry.getValue()) {
                // Exibe cada funcionário na função
                sb.append("<tr><td>").append(f.getNome()).append("</td>");
                sb.append("<td>").append(f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>");
                sb.append("<td>").append(f.getSalario()).append("</td></tr>");
            }

            sb.append("</tbody></table>");
        }

        sb.append("</body></html>");
        return sb;
    }


    public StringBuilder imprimirFuncionariosAniversariantesMesesEspecificos(List<Funcionario> funcionarios) {
        StringBuilder html = new StringBuilder();
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");  // Formatação de data

        html.append("<table><thead><tr><th>Funcionários</th><th>Data de Nascimento</th><th>Salário</th><th>Função</th></tr></thead><tbody>");

        for (Funcionario f : funcionarios) {
            // Verificar se o mês de nascimento é outubro (10) ou dezembro (12)
            int mesNascimento = f.getDataNascimento().getMonthValue();  // Pega o mês da data de nascimento

            if (mesNascimento == 10 || mesNascimento == 12) {
                String dataFormatada = f.getDataNascimento().format(dataFormatter);  // Formata a data
                //String salarioFormatado = salarioFormatter.format(f.getSalario());    // Formata o salário

                html.append("<tr><td>").append(f.getNome()).append("</td>");
                html.append("<td>").append(f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>");
                html.append("<td>").append(f.getSalario()).append("</td>");
                html.append("<td>").append(f.getFuncao()).append("</td></tr>");
            }
        }

        html.append("</tbody></table>");
        return html;
    }

    public StringBuilder imprimirFuncionarioMaisVelho(List<Funcionario> funcionarios) {
        StringBuilder html = new StringBuilder();
        Funcionario maisVelho = null;
        int maiorIdade = 0;

        for (Funcionario f : funcionarios) {
            // Calculando a idade do funcionário
            LocalDate dataNascimento = f.getDataNascimento();
            int idade = Period.between(dataNascimento, LocalDate.now()).getYears(); // Calcula a idade

            // Comparando se a idade do funcionário é a maior
            if (idade > maiorIdade) {
                maiorIdade = idade;
                maisVelho = f; // Armazena o funcionário mais velho
            }
        }

        // Se encontrou o funcionário mais velho, exibe os dados
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
    public void ordenarFuncionariosPorNome() {
        funcionarios.sort(Comparator.comparing(Funcionario::getNome));
    }



    @Override
    public String calcularTotalSalarios() {
        // Calcula a soma dos salários de todos os funcionários
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)   // Extrai o salário de cada funcionário
                .reduce(BigDecimal.ZERO, BigDecimal::add);  // Soma todos os salários

        // Agora, formatamos o totalSalarios para uma string com vírgula como separador decimal e ponto como separador de milhar
        Locale brasil = new Locale("pt", "BR");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(brasil);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat salarioFormatter = new DecimalFormat("#,##0.00", symbols);
        return salarioFormatter.format(totalSalarios);  // Retorna o valor formatado como String
    }

    public StringBuilder imprimirFuncionariosComSalariosMinimos(List<Funcionario> funcionarios) {
        BigDecimal salarioMinimo = new BigDecimal("1212.00"); // Valor do salário mínimo
        DecimalFormat salarioFormatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US)); // Formatação do salário

        StringBuilder html = new StringBuilder();

        html.append("<table><thead><tr><th>Funcionários</th><th>Data de Nascimento</th><th>Salário</th><th>Função</th><th>Salários Mínimos</th></tr></thead><tbody>");

        for (Funcionario f : funcionarios) {
            String dataFormatada = f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));  // Formata a data
            String salarioFormatado = salarioFormatter.format(f.getSalario());    // Formata o salário
            BigDecimal salariosMinimos = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP); // Calcula a quantidade de salários mínimos

            // Adiciona os dados de cada funcionário na tabela
            html.append("<tr><td>").append(f.getNome()).append("</td>");
            html.append("<td>").append(dataFormatada).append("</td>");
            html.append("<td>").append(salarioFormatado).append("</td>");
            html.append("<td>").append(f.getFuncao()).append("</td>");
            html.append("<td>").append(salariosMinimos).append("</td></tr>");
        }

        html.append("</tbody></table>");
        return html;
    }


}
