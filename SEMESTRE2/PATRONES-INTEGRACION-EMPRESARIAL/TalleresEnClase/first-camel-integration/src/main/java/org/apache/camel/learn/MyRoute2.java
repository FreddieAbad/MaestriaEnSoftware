package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;

public class MyRoute2 extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("direct:inicio")
        .id("saludos2")
        .to("mock:salida");
    }

    
}
