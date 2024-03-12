package org.apache.camel.learn;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PersonaProcesor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        ArrayList<Persona> lista = (ArrayList)exchange.getIn().getBody();
        for(Persona persona: lista){
            System.out.println("Persona " + persona.toString());
        }
    }

    
}
