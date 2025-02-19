package com.projedataInformatica.spring_app_iniflex.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//Gera automaticamente os métodos e o construtor
@Getter
@Setter
@AllArgsConstructor
public class User {
    private String name;
    private String email;

    public String getName(){
        return name;
    }
}
