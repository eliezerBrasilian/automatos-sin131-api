package com.sin131.automatos.controller;

import com.sin131.automatos.minimizadores.AFDMinimizador;
import com.sin131.automatos.records.AFD;
import com.sin131.automatos.simuladores.SimuladorDeAceitacao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("automatos/api/afd")
public class AfdController {

    @PostMapping("minimizar")
    public AFD minimizar(@RequestBody AFD afd){
        return AFDMinimizador.minimizar(afd);
    }

    private record PalavraRequest(AFD afd, String palavra){}
    @PostMapping("aceita-palavra")
    public Boolean aceitaPalavra(@RequestBody PalavraRequest data){
        return SimuladorDeAceitacao.aceitaPalavra(data.afd,data.palavra);
    }
}
