package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;

public class MyRoute1 extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("direct:inicio")
        .id("saludosRoute1")
        .to("log:foo");
    }

    
}
