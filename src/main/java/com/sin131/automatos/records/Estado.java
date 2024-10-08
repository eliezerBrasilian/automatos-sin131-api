package com.sin131.automatos.records;

import java.util.*;

public record Estado(String nome){
    @Override
    public String toString() {
        return "{ " + nome + " }";
    }

    public static ArrayList<Estado> getEstadosSimples(AFD afd, List<Estado> estadosFinais) {
        var estadosSimples = new ArrayList<Estado>();

        for(Estado estado:afd.conjuntoEstados()){
            if(!estadosFinais.contains(estado)) {
                estadosSimples.add(estado);
            }
        }

        return estadosSimples;
    }


    public static ArrayList<Estado> getlistaDeDestinosValidos(ArrayList<Estado> retornoListaDestinosSimplesValidos,
                                                              ArrayList<Estado> retornoListaDestinosFinaisValidos,
                                                              List<Estado> estadosFinais,
                                                              ArrayList<Estado> estadosSimples) {
        var listadeDestinosValidos = new ArrayList<>(retornoListaDestinosSimplesValidos);

        listadeDestinosValidos.addAll(retornoListaDestinosFinaisValidos);
        listadeDestinosValidos.addAll(estadosFinais);
        listadeDestinosValidos.addAll(estadosSimples);

        return  listadeDestinosValidos;

    }

    public static ArrayList<Estado> getListadedestinos(List<Estado> estadosFinais) {

        var listaDeDestinosPossiveis = new ArrayList<Estado>();

        for (int i = 0; i < estadosFinais.size(); i++) {
            var estadoAtual = estadosFinais.get(i).nome();

            for (int j = i + 1; j < estadosFinais.size(); j++) {
                var proximoEstado = estadosFinais.get(j).nome();

                Estado novoEstado = getEstadoConcatenadoSemRepeticao(estadoAtual, proximoEstado);
                if (!listaDeDestinosPossiveis.contains(novoEstado)) {
                    listaDeDestinosPossiveis.add(novoEstado);
                }
            }
        }

        return listaDeDestinosPossiveis;
    }

    private static Estado getEstadoConcatenadoSemRepeticao(String estadoAtual, String proximoEstado) {
        Set<String> conjuntoEstados = new TreeSet<>();

        for (String estado : estadoAtual.split("q")) {
            if (!estado.isEmpty()) {
                conjuntoEstados.add("q" + estado);
            }
        }
        for (String estado : proximoEstado.split("q")) {
            if (!estado.isEmpty()) {
                conjuntoEstados.add("q" + estado);
            }
        }

        StringBuilder estadoConcatenado = new StringBuilder();
        for (String estado : conjuntoEstados) {
            estadoConcatenado.append(estado);
        }

        Estado novoEstado = new Estado(estadoConcatenado.toString());
        return novoEstado;
    }

    public static ArrayList<String> getEstadosNaoMinimzados(List<Transicao> transicoesRefinadas,
                                                            List<Transicao> transicoesDaAntigaAfd){
        var copiaDeTransicoes = new ArrayList<>(transicoesRefinadas);

        Set<String> estadosNaoMinimizados = new HashSet<>();

        //obtem estadados nao minimizados
        for (Transicao transicaoAntigaAfd: transicoesDaAntigaAfd){

            var estadosExistentesNaListaMinimizada = copiaDeTransicoes.stream().filter(t->{
                return Objects.equals(t.estadoAtual().nome(), transicaoAntigaAfd.estadoAtual().nome());
            }).toList();

            estadosExistentesNaListaMinimizada.forEach(t-> estadosNaoMinimizados.add(t.estadoAtual().nome()) );
        }

        return new ArrayList<>(estadosNaoMinimizados);
    }

};
