package com.projedataInformatica.spring_app_iniflex.controller;

//para que o spring identifique que ela é um controller e realize os eventos necessários


import com.projedataInformatica.spring_app_iniflex.domain.User;
import com.projedataInformatica.spring_app_iniflex.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//Combina o @Controller (pode também renderizar uma página html) e a notação de @ResponseBody. A api statefull mantém o estado de cada cliente no servidor (reconhece o cliente a cada requisição), já na stateless a cada requisição eu recebo todas as informações que eu preciso para fazer aquela funcionalidade que o cliente está pedindo, em muitos casos um token
@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {
/*
    private HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService){
        this.helloWorldService = helloWorldService;
    }
 */
    //Indica ao spring que essa dependência é autoinjetável
    @Autowired
    private HelloWorldService helloWorldService;

    @GetMapping
    public String helloWorld(){
        return helloWorldService.helloWorld("Giuliano");
    }

    @PostMapping("/{id}")
    public String helloWorldPost(@PathVariable("id") String id, @RequestParam(value="filter", defaultValue="nenhum") String filter, @RequestBody User body){
        //return "Hello World " + body.getName() + id;
        return "Hello World " + filter;
    }
}
