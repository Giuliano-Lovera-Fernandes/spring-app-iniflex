package com.projedataInformatica.spring_app_iniflex.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Funcionario {
        private String nome;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dataNascimento;
        private double salario;
        private String funcao;

        // Getters e Setters
        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public LocalDate getDataNascimento() {
                return dataNascimento;
        }

        public void setDataNascimento(LocalDate dataNascimento) {
               this.dataNascimento = dataNascimento;
        }

        // Getter e Setter para 'salario'
        public double getSalario() {
                return salario;
        }

        public void setSalario(double salario) {
                this.salario = salario;
        }

        // Getter e Setter para 'funcao'
        public String getFuncao() {
                return funcao;
        }

        public void setFuncao(String funcao) {
                this.funcao = funcao;
        }        // Continuar com os outros getters e setters
}
