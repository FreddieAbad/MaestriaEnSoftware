package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;

public class MyRoute4 extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("file:src/datos?noop=True&fileName=notas.csv")
        .bean(new Escala(), "convertNota")
        .to("log:NOTAS");
    }

    
}
