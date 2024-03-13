package org.apache.camel.learn;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;


public class MyRoute5 extends RouteBuilder {

    DataFormat bindy = new BindyCsvDataFormat(org.apache.camel.learn.Persona.class);

    @Override
    public void configure() throws Exception {

        from("file:src/datos?noop=True&fileName=cardsclients.csv&delay=50000")
                //from("file:src/datos?move=.done&moveFailed=.error&fileName=cardsclients.csv")
                .split().tokenize("\n")
                .filter(body().isNotNull())
                .unmarshal(bindy)
                .process(new PersonaProcesor())
                //.to("jdbc:myDataSource")
                .to("log:PERSONAS")
               // .to("jdbc:dataSource")
//                .to("jdbc:dataSource?useHeaderAsParameters=true&resetAutoCommit=false&outputType=SelectOne")
                ;
    }
}
