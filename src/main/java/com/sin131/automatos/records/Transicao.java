package com.sin131.automatos.records;

// q0 -> a -> qf
//q0 -> b -> q2
//q0  -> a -> q0

public record Transicao(Estado estadoAtual,
                        Character simboloEntrada,
                        Estado estadoDestino){ };
