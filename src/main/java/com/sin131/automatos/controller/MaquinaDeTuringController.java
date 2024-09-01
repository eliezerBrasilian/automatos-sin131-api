package com.sin131.automatos.controller;

import com.sin131.automatos.records.MaquinaDeTuringPar;
import com.sin131.automatos.records.MaquinaDeTuringIncremento;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("maquina-de-turing/api")
public class MaquinaDeTuringController {

    @PostMapping("numero-eh-par/{numero}")
    public boolean ehPar(@PathVariable String numero){
        var maquina = new MaquinaDeTuringPar(numero);

        return maquina.processar();
    }

    @PostMapping("valorIncrementado/{numero}")
    public String valorIncrementado(@PathVariable String numero){
       var maquina = new MaquinaDeTuringIncremento(numero);
       return maquina.processar();
    }
}
