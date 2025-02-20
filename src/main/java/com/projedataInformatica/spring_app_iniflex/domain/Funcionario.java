package com.projedataInformatica.spring_app_iniflex.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario extends Pessoa {



        private BigDecimal salario;
        private String funcao;

        public Funcionario(){
                super("", LocalDate.now());
                this.salario = BigDecimal.ZERO;
                this.funcao = "";
        }

        public Funcionario(String nome, LocalDate dataNascimento) {
                super(nome, dataNascimento);
        }
        // Getters e Setters

        // Getter e Setter para 'salario'
        public BigDecimal getSalario() {
                return salario;
        }

        public void setSalario(BigDecimal salario) {
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
