package com.sin131.automatos.records;
import java.util.List;

//[a,b]
public record Alfabeto(List<Character> caracteres){
    @Override
    public String toString() {
        return caracteres.toString();
    }
};