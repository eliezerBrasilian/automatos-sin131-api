package com.sin131.automatos.minimizadores;

import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.Estado;
import com.sin131.automatos.records.Transicao;

import java.util.*;

public class AFDMinimizador {
    public static AFD minimizar(AFD afd) {
        // Passo 1: Inicializar partições P e W
        Set<Set<Estado>> P = new HashSet<>();
        Set<Set<Estado>> W = new HashSet<>();

        // Dividir estados finais e não finais
        Set<Estado> estadosFinais = new HashSet<>(afd.estadosFinais());
        Set<Estado> estadosNaoFinais = new HashSet<>(afd.conjuntoEstados());
        estadosNaoFinais.removeAll(estadosFinais);

        P.add(estadosFinais);
        P.add(estadosNaoFinais);
        W.add(estadosFinais);
        W.add(estadosNaoFinais);

        // Passo 2: Refinar partições
        while (!W.isEmpty()) {
            Set<Estado> A = W.iterator().next();
            W.remove(A);

            for (Character simbolo : afd.alfabeto().caracteres()) {
                // Obter o conjunto de estados que, ao ler o símbolo, se dirigem a A
                Set<Estado> X = new HashSet<>();
                for (Transicao t : afd.conjuntoTransicoes()) {
                    if (A.contains(t.estadoDestino()) && t.simboloEntrada().equals(simbolo)) {
                        X.add(t.estadoAtual());
                    }
                }

                // Refinar as partições em P usando X
                Set<Set<Estado>> PRefinada = new HashSet<>();
                for (Set<Estado> Y : P) {
                    Set<Estado> interseccao = new HashSet<>(Y);
                    interseccao.retainAll(X);

                    Set<Estado> diferenca = new HashSet<>(Y);
                    diferenca.removeAll(X);

                    if (!interseccao.isEmpty() && !diferenca.isEmpty()) {
                        PRefinada.add(interseccao);
                        PRefinada.add(diferenca);
                        if (W.contains(Y)) {
                            W.remove(Y);
                            W.add(interseccao);
                            W.add(diferenca);
                        } else {
                            if (interseccao.size() <= diferenca.size()) {
                                W.add(interseccao);
                            } else {
                                W.add(diferenca);
                            }
                        }
                    } else {
                        PRefinada.add(Y);
                    }
                }
                P = PRefinada;
            }
        }

        // Passo 3: Construir o novo AFD minimizado
        Map<Set<Estado>, Estado> mapaConjuntosParaEstado = new HashMap<>();
        List<Estado> novosEstados = new ArrayList<>();
        List<Transicao> novasTransicoes = new ArrayList<>();

        for (Set<Estado> conjunto : P) {
            if (!conjunto.isEmpty()) {
                Estado novoEstado = new Estado(conjuntoParaNome(conjunto));
                mapaConjuntosParaEstado.put(conjunto, novoEstado);
                novosEstados.add(novoEstado);
            }
        }

        for (Transicao transicao : afd.conjuntoTransicoes()) {
            Set<Estado> origemConjunto = null;
            Set<Estado> destinoConjunto = null;
            for (Set<Estado> conjunto : P) {
                if (conjunto.contains(transicao.estadoAtual())) {
                    origemConjunto = conjunto;
                }
                if (conjunto.contains(transicao.estadoDestino())) {
                    destinoConjunto = conjunto;
                }
            }
            if (origemConjunto != null && destinoConjunto != null) {
                novasTransicoes.add(new Transicao(
                        mapaConjuntosParaEstado.get(origemConjunto),
                        transicao.simboloEntrada(),
                        mapaConjuntosParaEstado.get(destinoConjunto)
                ));
            }
        }

        Estado novoEstadoInicial = mapaConjuntosParaEstado.values().stream()
                .filter(estado -> estado.nome().equals(afd.estadoInicial().nome()))
                .findFirst().orElse(null);

        List<Estado> novosEstadosFinais = new ArrayList<>();
        for (Set<Estado> conjunto : P) {
            for (Estado estado : afd.estadosFinais()) {
                if (conjunto.contains(estado)) {
                    novosEstadosFinais.add(mapaConjuntosParaEstado.get(conjunto));
                }
            }
        }

        return new AFD(novosEstados, afd.alfabeto(), novasTransicoes, novoEstadoInicial, novosEstadosFinais);
    }

    private static String conjuntoParaNome(Set<Estado> conjunto) {
        StringBuilder nome = new StringBuilder();
        for (Estado estado : conjunto) {
            nome.append(estado.nome());
        }
        return nome.toString();
    }
}
