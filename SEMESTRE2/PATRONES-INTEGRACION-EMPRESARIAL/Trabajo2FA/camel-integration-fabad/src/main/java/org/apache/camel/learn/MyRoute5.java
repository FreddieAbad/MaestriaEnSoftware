
package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.builder.PredicateBuilder;


public class MyRoute5 extends RouteBuilder {

    DataFormat bindy = new BindyCsvDataFormat(org.apache.camel.learn.Persona.class);

    @Override
    public void configure() throws Exception {

        from("file:src/datos?noop=True&fileName=cardsclients.csv&delay=50000")
                .split().tokenize("\n")
                .filter(body().isNotNull())
                .unmarshal(bindy)
                .process(new PersonaProcesor())
                .choice()
                .when(PredicateBuilder.and(header("pagos").isEqualTo(false), header("bills").isEqualTo(false)))
                        .to("direct:procesarInsert")
                .end();


        from("direct:procesarInsert")
                .process(new InsertProcesor())
                .to("log:FIN");
    }
}
