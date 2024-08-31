package com.sin131.automatos.records;

// q0 -> a -> qf
//q0 -> b -> q2
//q0  -> a -> q0

import java.util.*;
import java.util.stream.Collectors;

public record Transicao(Estado estadoAtual,
                        Character simboloEntrada,
                        Estado estadoDestino){
    @Override
    public String toString() {
        return STR."{\{estadoAtual.nome()} <\{simboloEntrada}> \{estadoDestino.nome()}}";
    }

    public static ArrayList<Transicao> getTransicoes(AFD afd, List<Estado> estados) {
        var transicoesFinais = new ArrayList<Transicao>();

        for (int i = 0; i < estados.size(); i++) {
            var estadoAtual = estados.get(i).nome();//q0

            for (int j = i + 1; j < estados.size(); j++) { // j começa em i+1 para evitar repetição de pares
                var proximoEstado = estados.get(j).nome();

                // garantindo que os destinos não sejam repetidos
                var destinosSet = new HashSet<String>();

                // Analisando transições para cada símbolo de entrada
                for (Character simbolo : afd.alfabeto().caracteres()) {//a
                    var grupodestino = new ArrayList<String>();

                    for (Transicao transicao : afd.conjuntoTransicoes()) {
                        var estadoAtualDaTransicao = transicao.estadoAtual().nome();

                        // Verificar se a transição é relevante para o par de estados e símbolo atual
                        if ((estadoAtualDaTransicao.equals(estadoAtual) && transicao.simboloEntrada().equals(simbolo)) ||
                                (estadoAtualDaTransicao.equals(proximoEstado) && transicao.simboloEntrada().equals(simbolo))) {

                            // Adicionar ao set para evitar duplicação
                            destinosSet.add(transicao.estadoDestino().nome());//[q2,q3,q4] para o a, [q5,q6] para o b
                        }
                    }

                    // Convertendo o set para uma lista ordenada
                    grupodestino.addAll(destinosSet);//q2
                    // Ordenando a lista para consistência (opcional)
                    Collections.sort(grupodestino);//[q2,q3,q4]

                    // Concatenando os estados de destino em uma única string

                    var estadoAtualeProximoConcatenado = estadoAtual + proximoEstado; //q2q3
                    var destinosConcatenados_ = String.join("", grupodestino); //[q2q3q4]

                    var transicaoCriada = new Transicao(
                            new Estado(estadoAtualeProximoConcatenado),
                            simbolo,
                            new Estado(destinosConcatenados_)
                    );

                    transicoesFinais.add(transicaoCriada);

                    // Limpar o set para a próxima iteração
                    destinosSet.clear();
                }
            }
        }

        return transicoesFinais;
    }

    public static List<Transicao> getNovasTransicoes(AFD afd, ArrayList<Transicao> transicoes, ArrayList<Estado> listaDeDestinosValidosUnida) {
        var estadosFinaisDestList = new HashSet<String>();
        var transicoesSet = new HashSet<Transicao>();

        for (Character simbolo: afd.alfabeto().caracteres()){
            //a
            for (int i = 0; i < transicoes.size(); i++) {
                var tAtual = transicoes.get(i);//q2q3 a q3q4
                var estadoAtual = tAtual.estadoAtual().nome();//q2q3

                for(int j = 1; j < transicoes.size(); j ++){//q2q3 a q3q4 >
                    var tProxima = transicoes.get(j);
                    var estadoDestaProximaTransicao = tProxima.estadoAtual().nome();
                    var destinoDestaProximaTransicao =tProxima.estadoDestino().nome();
                    var simboloDaProximaTransicao = tProxima.simboloEntrada();

                    var resultado = getEstadosConcatenadosSemRepeticoes(tAtual, estadoDestaProximaTransicao);


                    if(!Objects.equals(estadoAtual, estadoDestaProximaTransicao)
                            && tAtual.simboloEntrada().equals(simbolo) //a
                            && simboloDaProximaTransicao.equals(simbolo)
                            && tAtual.estadoDestino().nome().equals(destinoDestaProximaTransicao)
                            && listaDeDestinosValidosUnida.contains(tAtual.estadoDestino())
                    ){

                        if(!estadosFinaisDestList.contains(resultado)){
                            estadosFinaisDestList.add(resultado);

                            transicoesSet.add(
                                    new Transicao(
                                            new Estado(resultado),//q2
                                            simbolo,
                                            new Estado(tAtual.estadoDestino.nome())//q2q3q4q5
                                    )
                            );
                        }
                    }

                }
            }
        }

        return transicoesSet.stream().toList();
    }

    private static String getEstadosConcatenadosSemRepeticoes(Transicao tAtual, String estadoDestaProximaTransicao) {
        var estadosConcatenados = tAtual.estadoAtual().nome() + estadoDestaProximaTransicao;

        // pegando só estados unicos
        Set<String> estadosUnicos = new TreeSet<>();

        // Separando a string em pedaços de 2 caracteres (supondo que cada estado tem 2 caracteres)
        for (int k = 0; k < estadosConcatenados.length(); k += 2) {
            if (k + 2 <= estadosConcatenados.length()) {
                estadosUnicos.add(estadosConcatenados.substring(k,k+2));
            }else{
                estadosUnicos.add(estadosConcatenados.substring(k));
            }
        }
        var resultado = estadosUnicos.stream()
                .distinct().collect(Collectors.joining());
        return resultado;
    }

    public static void garanteTransicoesComTodosSimbolos(ArrayList<Transicao> transicoesRefinadas,
                                                         List<Transicao> novasTransicoesList) {
        var contemB = true;
        var contemA = true;

        for (Transicao transicao: transicoesRefinadas){
            if(transicao.simboloEntrada() != 'a'){
                contemA = false;
            }if(transicao.simboloEntrada() != 'b'){
                contemB = false;
            }
        }

        if (!contemA || !contemB) {
            for (Transicao transicao : novasTransicoesList) {
                if (!contemA && transicao.simboloEntrada() == 'a') {

                    transicoesRefinadas.add(transicao);
                }
                if (!contemB && transicao.simboloEntrada() == 'b') {
                    System.out.println("nao tem b");
                    transicoesRefinadas.add(transicao);
                }
            }
        }
    }

    public static void adicionandoTransicoesNaoMinimizadas(AFD afd, List<Transicao> transicoesRefinadas, AFD novoAfd) {
        var copiaDeTransicoes = new ArrayList<>(transicoesRefinadas);
        for (Transicao copiaDeTransicoe : copiaDeTransicoes) {
            var estadoAtual = copiaDeTransicoe.estadoAtual();

            for (int j = 0; j < afd.conjuntoEstados().size(); j++) {
                var estadoAtualDaAntigaAfd = afd.conjuntoEstados().get(j);

                if (!estadoAtual.nome().contains(estadoAtualDaAntigaAfd.nome())) {
                    System.out.println(STR."Estado da transicao atual: \{estadoAtual} não contem: \{estadoAtualDaAntigaAfd}");

                    for (Character simbolo : afd.alfabeto().caracteres()) {
                        // Encontrar a transição correspondente no AFD antigo
                        Transicao transicaoOriginal = null;
                        for (Transicao transicao : afd.conjuntoTransicoes()) {
                            if (transicao.estadoAtual().nome().equals(estadoAtualDaAntigaAfd.nome()) &&
                                    transicao.simboloEntrada().equals(simbolo)) {
                                transicaoOriginal = transicao;
                                break;
                            }
                        }

                        if (transicaoOriginal != null) {
                            var transicaoPreenchida = new Transicao(
                                    estadoAtualDaAntigaAfd,
                                    simbolo,
                                    transicaoOriginal.estadoDestino()
                            );

                            if (!novoAfd.conjuntoTransicoes().contains(transicaoPreenchida)) {
                                novoAfd.conjuntoTransicoes().add(transicaoPreenchida);
                                System.out.println(STR."Transição: \{transicaoPreenchida}");
                            }
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<Transicao> getTransicoesDosEstadosNaoMinimizados(ArrayList<String> estadosNaoMinimizados,
                                                                             List<Transicao> transicoesRefinadas, List<Character> simbolos) {
        var copiaDeTransicoes = new ArrayList<>(transicoesRefinadas);

        for (String estadoNaoMinimizado: estadosNaoMinimizados){
            System.out.println("estadoNaoMinimizado " + estadoNaoMinimizado);
            for(Character simbolo: simbolos){
                System.out.println("simbolo: " + simbolo);
                for (int i = 0; i < copiaDeTransicoes.size(); i++){

                    var transicao = copiaDeTransicoes.get(i);

                    if(transicao.simboloEntrada().equals(simbolo) &&
                            transicao.estadoAtual().nome().contains(estadoNaoMinimizado)){

                        var estadoDestino = transicao.estadoDestino().nome();

                        var optionalTransicaoDeIda = copiaDeTransicoes.stream().filter(t-> t.estadoAtual().nome().contains(estadoDestino)
                                && t.simboloEntrada().equals(simbolo)
                        ).findFirst();

                        if(optionalTransicaoDeIda.isPresent()){
                            var estadoDeIda = optionalTransicaoDeIda.get().estadoAtual();

                            var transicaoAtualizada = new Transicao(
                                    new Estado(estadoNaoMinimizado),
                                    simbolo,
                                    estadoDeIda
                            );

                            System.out.println("transicaoAtualizada");
                            System.out.println(transicaoAtualizada);
                            copiaDeTransicoes.set(i,transicaoAtualizada);
                        }


                    }
                }
            }
        }
        return copiaDeTransicoes;
    }


};
