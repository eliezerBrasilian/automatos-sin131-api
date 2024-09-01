package com.sin131.automatos.records;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MaquinaDeTuringIncremento {
    private String tape;
    private int head;
    private String estadoAtual;
    private Map<String, Map<Character, Transition>> transicoes;
    private String estadoAceitacao;

    public MaquinaDeTuringIncremento(String tape) {
        this.tape = tape + "_"; // Adiciona um delimitador no final
        this.head = tape.length() - 1; // Começa na última posição válida da palavra
        this.estadoAtual = "q0"; // Estado inicial
        this.estadoAceitacao = "qAceito";
        definirTransicoes();
    }

    private void definirTransicoes() {
        transicoes = new HashMap<>();

        // Transições da Máquina de Turing para incremento binário
        transicoes.put("q0", new HashMap<>() {{
            put('0', new Transition("qAceito", '1', 0)); // Troca 0 por 1 e aceita
            put('1', new Transition("q1", '0', -1)); // Troca 1 por 0 e move à esquerda
            put('_', new Transition("qAceito", '1', 0)); // Se chegar ao início, adiciona 1 e aceita
        }});
        transicoes.put("q1", new HashMap<>() {{
            put('1', new Transition("q1", '0', -1)); // Continua trocando 1 por 0 e movendo à esquerda
            put('0', new Transition("qAceito", '1', 0)); // Troca 0 por 1 e aceita
            put('_', new Transition("qAceito", '1', 0)); // Se chegar ao início, adiciona 1 e aceita
        }});
    }

    public String processar() {
        while (true) {
            char simboloAtual = tape.charAt(head);
            Transition transicao = transicoes.get(estadoAtual).get(simboloAtual);

            if (transicao == null) {
                return tape; // Retorna a fita atual se não houver transição
            }

            // Atualiza a fita, o estado e a posição da cabeça
            tape = tape.substring(0, head) + transicao.simboloEscrever + tape.substring(head + 1);
            estadoAtual = transicao.estadoDestino;
            head += transicao.movimento;

            if (estadoAtual.equals(estadoAceitacao)) {
                return tape.replace("_", ""); // Retorna a fita sem o delimitador
            }
        }
    }

    // Classe interna para representar uma transição
    private static class Transition {
        String estadoDestino;
        char simboloEscrever;
        int movimento; // 1 para direita, -1 para esquerda, 0 para não mover

        Transition(String estadoDestino, char simboloEscrever, int movimento) {
            this.estadoDestino = estadoDestino;
            this.simboloEscrever = simboloEscrever;
            this.movimento = movimento;
        }
    }

}
