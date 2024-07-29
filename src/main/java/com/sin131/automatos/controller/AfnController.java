package com.sin131.automatos.controller;

import com.sin131.automatos.conversores.AfnParaAfdConversor;
import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.AFN;
import com.sin131.automatos.simuladores.SimuladorDeAceitacao;
import org.springframework.web.bind.annotation.*;

import static java.lang.StringTemplate.STR;

@RestController
@RequestMapping("automatos/api/afn")
public class AfnController {

    @PostMapping
    public AFD convertToAfd(@RequestBody AFN afn){
        return AfnParaAfdConversor.converte(afn);
    }

    private record PalavraRequest(AFN afn, String palavra){}
    @PostMapping("aceita-palavra")
    public Boolean aceitaPalavra(@RequestBody PalavraRequest data){

        return SimuladorDeAceitacao.aceitaPalavra(data.afn,data.palavra);
    }
}
