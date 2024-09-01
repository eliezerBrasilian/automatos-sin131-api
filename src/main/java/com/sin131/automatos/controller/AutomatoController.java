package com.sin131.automatos.controller;

import com.sin131.automatos.conversores.AfnParaAfdConversor;
import com.sin131.automatos.minimizadores.AFDMinimizador;
import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.AFN;
import com.sin131.automatos.simuladores.SimuladorDeAceitacao;
import org.springframework.web.bind.annotation.*;

import static java.lang.StringTemplate.STR;

@RestController
@RequestMapping("automatos/api")
public class AutomatoController {

    @PostMapping("convert-afn")
    public AFD sendAfn(@RequestBody AFN afn){
        var x=  AfnParaAfdConversor.converte(afn);

        System.out.println(x);

        return x;
    }

    private record PalavraRequest(AFN automato, String palavra){}
    @PostMapping("accept-word")
    public Boolean aceitaPalalavraEmAfn(@RequestBody PalavraRequest data){

       return SimuladorDeAceitacao.aceitaPalavra(data.automato,data.palavra);

    }

    @PostMapping("minimize")
    public AFD minimizar(@RequestBody AFD afd){


        return AFDMinimizador.minimizar(afd);
    }

    private record EquivalenciaRequestDto(AFN afn, AFD afd, String palavra){};
    @PostMapping("test-equivalency")
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
