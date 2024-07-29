package com.sin131.automatos.simuladores;



import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.AFN;
import com.sin131.automatos.records.Estado;
import com.sin131.automatos.records.Transicao;

import java.util.List;

public class SimuladorDeAceitacao {

    public static boolean aceitaPalavra(AFN afn, String palavra) {
        return verificaEstado(afn.estadoInicial(), palavra, afn.conjuntoTransicoes(), afn.estadosFinais());
    }

    private static boolean verificaEstado(Estado estadoAtual, String palavra, List<Transicao> transicoes, List<Estado> estadosFinais) {
        if (palavra.isEmpty()) {
            return estadosFinais.contains(estadoAtual);
        }

        char simbolo = palavra.charAt(0);
        String restoPalavra = palavra.substring(1);

        for (Transicao transicao : transicoes) {
            if (transicao.estadoAtual().equals(estadoAtual) && transicao.simboloEntrada().equals(simbolo)) {
                if (verificaEstado(transicao.estadoDestino(), restoPalavra, transicoes, estadosFinais)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean aceitaPalavra(AFD afd, String palavra) {
        Estado estadoAtual = afd.estadoInicial();

        for (char simbolo : palavra.toCharArray()) {
            estadoAtual = proximoEstado(estadoAtual, simbolo, afd.conjuntoTransicoes());
            if (estadoAtual == null) {
                return false;
            }
        }
        return afd.estadosFinais().contains(estadoAtual);
    }

    private static Estado proximoEstado(Estado estadoAtual, char simbolo, List<Transicao> conjuntoTransicoes) {
        for (Transicao transicao : conjuntoTransicoes) {
            if (transicao.estadoAtual().equals(estadoAtual) && transicao.simboloEntrada().equals(simbolo)) {
                return transicao.estadoDestino();
            }
        }
        return null;
    }
}
