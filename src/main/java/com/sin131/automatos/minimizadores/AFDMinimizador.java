
package com.sin131.automatos.minimizadores;

import com.sin131.automatos.records.AFD;
import com.sin131.automatos.records.Estado;
import com.sin131.automatos.records.Transicao;

import java.util.*;
import java.util.stream.Collectors;

public class AFDMinimizador {

    public static AFD minimizar(AFD afd) {
        List<Estado> estados =  afd.conjuntoEstados();
        List<Estado> estadosFinais = afd.estadosFinais();
        Set<Estado> estadosNaoFinais = new HashSet<>(estados);
        estadosNaoFinais.removeAll(estadosFinais);

        Map<Estado, Integer> particao = new HashMap<>();
        int idParticao = 0;
        for (Estado estado : estadosNaoFinais) {
            particao.put(estado, idParticao);
        }
        idParticao++;
        for (Estado estado : estadosFinais) {
            particao.put(estado, idParticao);
        }

        boolean houveMudanca;
        do {
            houveMudanca = false;
            Map<Estado, Integer> novaParticao = new HashMap<>();
            Map<String, Integer> grupos = new HashMap<>();

            for (Estado estado : estados) {
                StringBuilder grupoIdentificador = new StringBuilder();
                grupoIdentificador.append(particao.get(estado));

                for (Character simbolo : afd.alfabeto().caracteres()) {
                    grupoIdentificador.append("-");

                    Optional<Estado> destino = afd.conjuntoTransicoes().stream()
                            .filter(transicao -> transicao.estadoAtual().equals(estado) && transicao.simboloEntrada().equals(simbolo))
                            .map(Transicao::estadoDestino)
                            .findFirst();

                    Map<Estado, Integer> finalParticao = particao;
                    destino.ifPresentOrElse(
                            destinoEstado -> grupoIdentificador.append(finalParticao.get(destinoEstado)),
                            () -> grupoIdentificador.append("N")
                    );
                }


                String identificador = grupoIdentificador.toString();
                if (!grupos.containsKey(identificador)) {
                    grupos.put(identificador, grupos.size());
                }
                novaParticao.put(estado, grupos.get(identificador));
            }

            if (!novaParticao.equals(particao)) {
                particao = novaParticao;
                houveMudanca = true;
            }
        } while (houveMudanca);

        Map<Integer, Estado> novoEstadoPorGrupo = new HashMap<>();
        for (Estado estado : estados) {
            int grupo = particao.get(estado);
            if (!novoEstadoPorGrupo.containsKey(grupo)) {
                novoEstadoPorGrupo.put(grupo, new Estado("q" + grupo));
            }
        }

        Set<Estado> novosEstados = new HashSet<>(novoEstadoPorGrupo.values());
        Set<Transicao> novasTransicoes = new HashSet<>();
        Estado novoEstadoInicial = novoEstadoPorGrupo.get(particao.get(afd.estadoInicial()));
        Map<Estado, Integer> finalParticao1 = particao;
        Set<Estado> novosEstadosFinais = estadosFinais.stream()
                .map(estado -> novoEstadoPorGrupo.get(finalParticao1.get(estado)))
                .collect(Collectors.toSet());

        for (Transicao transicao : afd.conjuntoTransicoes()) {
            Estado origem = novoEstadoPorGrupo.get(particao.get(transicao.estadoAtual()));
            Estado destino = novoEstadoPorGrupo.get(particao.get(transicao.estadoDestino()));
            novasTransicoes.add(new Transicao(origem, transicao.simboloEntrada(), destino));
        }
        return new AFD(
                new ArrayList<>(novosEstados),
                afd.alfabeto(),
                new ArrayList<>(novasTransicoes),
                novoEstadoInicial,
                new ArrayList<>(novosEstadosFinais)
        );
    }

}


