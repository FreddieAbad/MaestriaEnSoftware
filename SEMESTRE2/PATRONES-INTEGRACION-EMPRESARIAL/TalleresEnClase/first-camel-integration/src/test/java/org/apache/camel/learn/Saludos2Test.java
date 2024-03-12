package org.apache.camel.learn;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class Saludos2Test extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new MyRoute2();
    }

    @Test
    public void testSaludo() throws InterruptedException {
        template.sendBody("direct:inicio", "Hola mundo");
        MockEndpoint mockEndpoint = getMockEndpoint("mock:salida");
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
    }

}
