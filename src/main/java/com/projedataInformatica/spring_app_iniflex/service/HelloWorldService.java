package com.projedataInformatica.spring_app_iniflex.service;
//Classe que contém a regra de negócio

import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {
    public String helloWorld(String name){
        return "Hello World " + name;
    }
}
