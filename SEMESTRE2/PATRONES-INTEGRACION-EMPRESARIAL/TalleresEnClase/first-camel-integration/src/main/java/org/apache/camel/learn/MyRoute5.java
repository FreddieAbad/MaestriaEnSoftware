package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat; // Cambiado a DataFormat

public class MyRoute5 extends RouteBuilder {

    DataFormat bindy = new BindyCsvDataFormat(org.apache.camel.learn.Persona.class);

    @Override
    public void configure() throws Exception {

        from("file:src/datos?noop=True&fileName=personas.csv")
            .unmarshal(bindy)
            .process(new PersonaProcesor())
            .to("log:PERSONAS");
    }
}
