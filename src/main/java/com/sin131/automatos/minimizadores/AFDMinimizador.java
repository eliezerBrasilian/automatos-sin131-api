package com.sin131.automatos.minimizadores;

import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.Estado;
import com.sin131.automatos.records.Transicao;

import java.util.*;

import static com.sin131.automatos.records.Estado.*;
import static com.sin131.automatos.records.Transicao.*;

public class AFDMinimizador {

    public static AFD minimizar(AFD afd) {
        var estadosFinais = afd.estadosFinais();

        var estadosSimples = getEstadosSimples(afd,estadosFinais);

        var retornoListaDestinosFinaisValidos = getListadedestinos(estadosFinais);

        var retornoListaDestinosSimplesValidos = getListadedestinos(estadosSimples);

        var listaDeDestinosValidosUnida = getlistaDeDestinosValidos(retornoListaDestinosSimplesValidos,
                retornoListaDestinosFinaisValidos,estadosFinais,estadosSimples);

        var transicoesSimples =  getTransicoes(afd,estadosSimples);

        var transicoesFinais = getTransicoes(afd, estadosFinais);

        var novasTransicoesList = getNovasTransicoes(afd,transicoesFinais,listaDeDestinosValidosUnida);
        System.out.println("-------novasTransicoesList");
        System.out.println(novasTransicoesList);
        var novasTransicoesSimplesList = getNovasTransicoes(afd,transicoesSimples,listaDeDestinosValidosUnida);

        var transicoesRefinadas = new ArrayList<>(
                getNovasTransicoes(
                afd, new ArrayList<>(novasTransicoesList),
                listaDeDestinosValidosUnida));

        System.out.println("------transicoesRefinadas");
        System.out.println(transicoesRefinadas);

        var transicoesSimplesRefinadas = new ArrayList<>(getNovasTransicoes(
                afd, new ArrayList<>(novasTransicoesSimplesList),
                listaDeDestinosValidosUnida));

        System.out.println("------transicoesSimplesRefinadas");
        System.out.println(transicoesSimplesRefinadas);

        garanteTransicoesComTodosSimbolos(transicoesRefinadas, novasTransicoesList);
        garanteTransicoesComTodosSimbolos(transicoesSimplesRefinadas, novasTransicoesSimplesList);

        var novoAfdSemTransicoesMinimizadas = new AFD(
                afd.conjuntoEstados(),
                afd.alfabeto(),
                transicoesRefinadas,
                afd.estadoInicial(),
                afd.estadosFinais()
        );

        adicionandoTransicoesNaoMinimizadas(afd, novoAfdSemTransicoesMinimizadas.conjuntoTransicoes(), novoAfdSemTransicoesMinimizadas);

        System.out.println("Novo afd: " + novoAfdSemTransicoesMinimizadas);

        var estadosNaoMinimizados = getEstadosNaoMinimzados(novoAfdSemTransicoesMinimizadas.conjuntoTransicoes(),afd.conjuntoTransicoes());
        System.out.println("estadosNaoMinimizados");
        System.out.println(estadosNaoMinimizados);

        System.out.println("novoAfdSemTransicoesMinimizadas.conjuntoTransicoes()");
        System.out.println(novoAfdSemTransicoesMinimizadas.conjuntoTransicoes());

        var novoAfdComTransicoesMinimizadas = new AFD(
                afd.conjuntoEstados(),
                afd.alfabeto(),
                novasTransicoesList,
                afd.estadoInicial(),
                afd.estadosFinais()
        );

        System.out.println("novoAfdComTransicoesMinimizadas----------------");
        System.out.println(novoAfdComTransicoesMinimizadas);

        return novoAfdComTransicoesMinimizadas;
    }
}

