package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;

public class Escala {

    public String convertNota(String data) {
        int notaInicial = Integer.valueOf(data);
        double notaFinal = notaInicial * 100 / 20;
        return Double.toString(notaFinal);
    }

}
