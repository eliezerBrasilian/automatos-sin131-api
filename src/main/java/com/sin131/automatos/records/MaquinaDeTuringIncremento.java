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
        this.tape = tape + "_";
        this.head = tape.length() - 1;
        this.estadoAtual = "q0";
        this.estadoAceitacao = "qAceito";
        definirTransicoes();
    }

    private void definirTransicoes() {
        transicoes = new HashMap<>();

        // Transições da Máquina de Turing para incremento binário
        transicoes.put("q0", new HashMap<>() {{
            put('0', new Transition("qAceito", '1', 0));
            put('1', new Transition("q1", '0', -1));
            put('_', new Transition("qAceito", '1', 0));
        }});
        transicoes.put("q1", new HashMap<>() {{
            put('1', new Transition("q1", '0', -1));
            put('0', new Transition("qAceito", '1', 0));
            put('_', new Transition("qAceito", '1', 0));
        }});
    }

    public String processar() {
        while (true) {
            char simboloAtual = tape.charAt(head);
            Transition transicao = transicoes.get(estadoAtual).get(simboloAtual);

            if (transicao == null) {
                return tape;
            }

            // Atualiza a fita, o estado e a posição da cabeça
            tape = tape.substring(0, head) + transicao.simboloEscrever + tape.substring(head + 1);
            estadoAtual = transicao.estadoDestino;
            head += transicao.movimento;

            if (estadoAtual.equals(estadoAceitacao)) {
                return tape.replace("_", "");
            }
        }
    }

    // Classe interna para representar uma transição
    private static class Transition {
        String estadoDestino;
        char simboloEscrever;
        int movimento;

        Transition(String estadoDestino, char simboloEscrever, int movimento) {
            this.estadoDestino = estadoDestino;
            this.simboloEscrever = simboloEscrever;
            this.movimento = movimento;
        }
    }

}
