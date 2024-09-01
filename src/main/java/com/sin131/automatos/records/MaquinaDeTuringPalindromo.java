package com.sin131.automatos.records;

import java.util.HashMap;
import java.util.Map;

public class MaquinaDeTuringPalindromo {
    private String tape;
    private int head;
    private String estadoAtual;
    private Map<String, Map<Character, Transition>> transicoes;

    public MaquinaDeTuringPalindromo(String tape) {
        this.tape = tape + "_";
        this.head = 0;
        this.estadoAtual = "q_inicio";
        definirTransicoes();
    }

    private void definirTransicoes() {
        transicoes = new HashMap<>();

        transicoes.put("q_inicio", new HashMap<>() {{
            put('0', new Transition("q_marcar_0_direita", 'X', 1)); // Marca 0 e move para a direita
            put('1', new Transition("q_marcar_1_direita", 'Y', 1)); // Marca 1 e move para a direita
            put('_', new Transition("q_verificar_fim", '_', 0)); // Fim da fita com entrada válida
        }});

        transicoes.put("q_marcar_0_direita", new HashMap<>() {{
            put('0', new Transition("q_marcar_0_direita", '0', 1)); // Continua para a direita
            put('1', new Transition("q_marcar_0_direita", '1', 1)); // Continua para a direita
            put('_', new Transition("q_voltar", '_', -1)); // Volta para o início
        }});

        transicoes.put("q_marcar_1_direita", new HashMap<>() {{
            put('0', new Transition("q_marcar_1_direita", '0', 1)); // Continua para a direita
            put('1', new Transition("q_marcar_1_direita", '1', 1)); // Continua para a direita
            put('_', new Transition("q_voltar", '_', -1)); // Volta para o início
        }});

        transicoes.put("q_voltar", new HashMap<>() {{
            put('X', new Transition("q_voltar", 'X', -1)); // Continua para a esquerda
            put('Y', new Transition("q_voltar", 'Y', -1)); // Continua para a esquerda
            put('_', new Transition("q_verificar", '_', 1)); // Verifica se todos os pares foram marcados
        }});

        transicoes.put("q_verificar", new HashMap<>() {{
            put('X', new Transition("q_inicio", 'X', 1)); // Se encontrar X, reinicia o processo
            put('Y', new Transition("q_inicio", 'Y', 1)); // Se encontrar Y, reinicia o processo
            put('_', new Transition("q_aceitacao", '_', 0)); // Aceita se a fita estiver vazia de símbolos não marcados
        }});

        transicoes.put("q_aceitacao", new HashMap<>() {{
            put('X', new Transition("q_aceitacao", 'X', 0)); // Permanece em aceitação
            put('Y', new Transition("q_aceitacao", 'Y', 0)); // Permanece em aceitação
            put('_', new Transition("q_aceitacao", '_', 0)); // Permanece em aceitação
        }});
    }

    public boolean processar() {
        while (true) {
            if (head < 0 || head >= tape.length()) {
                return false; // Se a cabeça estiver fora dos limites, rejeita
            }

            char simboloAtual = tape.charAt(head);
            Transition transicao = transicoes.get(estadoAtual).get(simboloAtual);

            if (transicao == null) {
                return false; // Se não houver transição, rejeita
            }

            tape = tape.substring(0, head) + transicao.simboloEscrever + tape.substring(head + 1);
            estadoAtual = transicao.estadoDestino;
            head += transicao.movimento;

            if (estadoAtual.equals("q_aceitacao")) {
                return true; // Aceita
            }
        }
    }

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
