package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;

public class MyRoute6 extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        // getContext().getRegistry().bind("myBeanMask", new MyBeanMask());

        from("file:src/datos?noop=True&fileName=personas.csv")
            .pipeline()
            .bean("myBeanMask", "masking")
            .log("Contenido ${body}")
            .to("mock:end");
    }
}
