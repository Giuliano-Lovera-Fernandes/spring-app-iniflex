package com.projedataInformatica.spring_app_iniflex.configuration;

import org.springframework.context.annotation.Configuration;

//Será utilizada para quando precisarmos de dependências que não pertencem ao spring
@Configuration
public class HelloConfiguration {
    //@Bean criar instâncias de classe que não podem ser gerenciadas (necessário um contexto específico) pelo spring
}

/* Transport interface
 Car -> implementação da classe Transport
@Bean - instâncias geradas são do escopo singleton
public Transport MyService(){
    return new Car();
}
*/