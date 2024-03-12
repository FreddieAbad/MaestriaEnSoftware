package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;

public class MyRoute3 extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("direct:iniciaSaludo")
        .log("imprimir mensaje de entrada ${body}")
        .choice()
        .when().simple("${body} contains 'mundo'")
            .log("el saludo tiene mundo")
        .otherwise()
            .log("el saludo no tiene mundo")
        .end()
        .to("direct:finalizarSaludo");

        from("direct:finalizarSaludo")
        .to("log:FIN");


    }

    
}
