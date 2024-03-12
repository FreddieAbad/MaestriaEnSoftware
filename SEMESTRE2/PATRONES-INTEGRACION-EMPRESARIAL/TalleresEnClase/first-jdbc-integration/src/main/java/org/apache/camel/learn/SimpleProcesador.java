package org.apache.camel.learn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SimpleProcesador implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody("select now()");
    }
    
}
