package com.sin131.automatos.records;
import java.util.List;

public record AFD(List<Estado> conjuntoEstados,
                  Alfabeto alfabeto,
                  List<Transicao> conjuntoTransicoes,
                  Estado estadoInicial,
                  List<Estado> estadosFinais){

    @Override
    public String toString() {
        return "{\n" +
                "\t" + "estados: " + conjuntoEstados + "," + "\n" +
                "\t" + "alfabeto: " + alfabeto + "," + "\n" +
                "\t" + "transições: " + conjuntoTransicoes + "," + "\n" +
                "\t" + "estado_inicial: " + estadoInicial + "," + "\n" +
                "\t" + "estados_finais: " + estadosFinais  +
                "\n}";
    }
};