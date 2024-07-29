package com.sin131.automatos.conversores;
import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.AFN;
import com.sin131.automatos.records.Estado;
import com.sin131.automatos.records.Transicao;

import java.util.*;

public class AfnParaAfdConversor {
    public static AFD converte(AFN afn) {
        Map<Set<Estado>, Estado> conjuntosMap = new HashMap<>();
        List<Transicao> novasTransicoes = new ArrayList<>();
        Queue<Set<Estado>> fila = new LinkedList<>();

        Set<Estado> estadoInicial = new HashSet<>();
        estadoInicial.add(afn.estadoInicial());
        fila.add(estadoInicial);

        Estado novoEstadoInicial = new Estado(conjuntoParaNome(estadoInicial));
        conjuntosMap.put(estadoInicial, novoEstadoInicial);

        while (!fila.isEmpty()) {
            Set<Estado> estadosAtuais = fila.poll();
            Estado estadoAfdAtual = conjuntosMap.get(estadosAtuais);

            for (Character simbolo : afn.alfabeto().caracteres()) {
                Set<Estado> estadosDestino = new HashSet<>();

                for (Estado estado : estadosAtuais) {
                    for (Transicao transicao : afn.conjuntoTransicoes()) {
                        if (transicao.estadoAtual().equals(estado) && transicao.simboloEntrada().equals(simbolo)) {
                            estadosDestino.add(transicao.estadoDestino());
                        }
                    }
                }

                if (!estadosDestino.isEmpty()) {
                    Estado estadoAfdDestino = conjuntosMap.get(estadosDestino);
                    if (estadoAfdDestino == null) {
                        estadoAfdDestino = new Estado(conjuntoParaNome(estadosDestino));
                        conjuntosMap.put(estadosDestino, estadoAfdDestino);
                        fila.add(estadosDestino);
                    }
                    novasTransicoes.add(new Transicao(estadoAfdAtual, simbolo, estadoAfdDestino));
                }
            }
        }

        List<Estado> novosEstados = new ArrayList<>(conjuntosMap.values());
        List<Estado> novosEstadosFinais = new ArrayList<>();
        for (Set<Estado> conjunto : conjuntosMap.keySet()) {
            for (Estado estado : conjunto) {
                if (afn.estadosFinais().contains(estado)) {
                    novosEstadosFinais.add(conjuntosMap.get(conjunto));
                    break;
                }
            }
        }

        return new AFD(novosEstados, afn.alfabeto(), novasTransicoes, novoEstadoInicial, novosEstadosFinais);
    }

    private static String conjuntoParaNome(Set<Estado> conjunto) {
        StringBuilder nome = new StringBuilder();
        for (Estado estado : conjunto) {
            nome.append(estado.nome());
        }
        return nome.toString();
    }
}
