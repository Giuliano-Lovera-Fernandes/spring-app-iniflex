package com.projedataInformatica.spring_app_iniflex.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Pessoa {
    // Atributos da classe Pessoa
    private String nome;

    @JsonFormat(pattern = "yyyy-MM-dd")
    //@JsonFormat(pattern = "dd-MM-YYYY")
    private LocalDate dataNascimento;

    // Construtor
    public Pessoa(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    // Métodos da classe Pessoa
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

    public void exibirInformacoes() {
        System.out.println("Nome: " + nome);
        System.out.println("Data de Nascimento: " + dataNascimento);
    }
}

