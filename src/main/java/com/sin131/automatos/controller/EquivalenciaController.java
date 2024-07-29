package com.sin131.automatos.controller;

import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.AFN;
import com.sin131.automatos.simuladores.SimuladorDeAceitacao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("automatos/api/equivalencia")
public class EquivalenciaController {

    private record EquivalenciaRequestDto(AFN afn, AFD afd, String palavra){};

    @PostMapping
    public Boolean testarEquivalencia(@RequestBody EquivalenciaRequestDto data){
        var aceitaAFD = SimuladorDeAceitacao.aceitaPalavra(
                data.afd,data.palavra);

        System.out.println("palavra aceita na AFD: " + aceitaAFD);

        var aceitaAFN = SimuladorDeAceitacao.aceitaPalavra(
                data.afn,data.palavra);

        System.out.println("palavra aceita na AFN: " + aceitaAFN);

        return (aceitaAFD && aceitaAFN) || (!aceitaAFD && !aceitaAFN);

    }
}
