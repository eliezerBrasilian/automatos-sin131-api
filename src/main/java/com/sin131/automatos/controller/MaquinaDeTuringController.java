package com.sin131.automatos.controller;

import com.sin131.automatos.records.MaquinaDeTuringPar;
import com.sin131.automatos.records.MaquinaDeTuringPalindromo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("maquina-de-turing/api")
public class MaquinaDeTuringController {

    @PostMapping("numero-eh-par/{numero}")
    public boolean ehPar(@PathVariable String numero){
        var maquina = new MaquinaDeTuringPar(numero);

        return maquina.processar();
    }

    @PostMapping("palindromo/{numero}")
    public boolean ehPalindromo(@PathVariable String numero){
        var maquina = new MaquinaDeTuringPalindromo(numero);
        return maquina.processar();
    }
}
