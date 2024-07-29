package com.sin131.automatos.records;
import java.util.List;

public record AFN(List<Estado> conjuntoEstados,
                  Alfabeto alfabeto,
                  List<Transicao> conjuntoTransicoes,
                  Estado estadoInicial,
                  List<Estado> estadosFinais){ };