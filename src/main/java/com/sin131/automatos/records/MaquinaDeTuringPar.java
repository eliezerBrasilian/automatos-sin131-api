package com.sin131.automatos.records;

import java.util.HashMap;
import java.util.Map;

public class MaquinaDeTuringPar {
    private String tape;
    private int head;
    private String estadoAtual;
    private Map<String, Map<Character, Transition>> transicoes;

    public MaquinaDeTuringPar(String tape) {
        this.tape = tape + "_";
        this.head = 0;
        this.estadoAtual = "estadoInicial";
        definirTransicoes();
    }

    private void definirTransicoes() {
        transicoes = new HashMap<>();

        transicoes.put("estadoInicial", new HashMap<>() {{
            put('0', new Transition("q_verifica_fim", '0', 1));
            put('1', new Transition("q_verifica_fim", '1', 1));
            put('_', new Transition("q_rejeicao", '_', 0));
        }});

        transicoes.put("q_verifica_fim", new HashMap<>() {{
            put('0', new Transition("q_verifica_fim", '0', 1));
            put('1', new Transition("q_verifica_fim", '1', 1));
            put('_', new Transition("q_verifica_paridade", '_', -1));
        }});


        transicoes.put("q_verifica_paridade", new HashMap<>() {{
            put('0', new Transition("q_aceitacao", '0', 0));
            put('1', new Transition("q_rejeicao", '1', 0));
        }});


        transicoes.put("q_aceitacao", new HashMap<>() {{
            put('0', new Transition("q_aceitacao", '0', 0));
            put('1', new Transition("q_aceitacao", '1', 0));
        }});


        transicoes.put("q_rejeicao", new HashMap<>() {{
            put('0', new Transition("q_rejeicao", '0', 0));
            put('1', new Transition("q_rejeicao", '1', 0));
        }});
    }

    public boolean processar() {
        while (true) {
            char simboloAtual = tape.charAt(head);
            Transition transicao = transicoes.get(estadoAtual).get(simboloAtual);

            if (transicao == null) {
                return false;
            }


            tape = tape.substring(0, head) + transicao.simboloEscrever + tape.substring(head + 1);
            estadoAtual = transicao.estadoDestino;


            head += transicao.movimento;


            if (estadoAtual.equals("q_aceitacao")) {
                return true; // Aceita
            } else if (estadoAtual.equals("q_rejeicao")) {
                return false; // Rejeita
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

