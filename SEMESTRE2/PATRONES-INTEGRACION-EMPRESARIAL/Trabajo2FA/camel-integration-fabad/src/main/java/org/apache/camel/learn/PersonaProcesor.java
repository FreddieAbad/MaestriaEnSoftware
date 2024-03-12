package org.apache.camel.learn;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.dbcp.BasicDataSource;
import javax.sql.DataSource;

public class PersonaProcesor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Persona persona = (Persona) exchange.getIn().getBody();
        boolean pagos = isAnyPayLessOrEqualZero(persona);
        boolean bills = isAnyBillLessOrEqualZero(persona);
        if (!pagos && !bills) {
            System.out.println("Linea :" + persona.getID());
            System.out.println("Todos los valores de factura a pagar y pagados son mayores a 0");
            System.out.println("Persona " + persona.toString());
            //exchange.getContext().createProducerTemplate().sendBody("direct:insert", persona);
            //exchange.getIn().setBody("select now()");
            //Thread.sleep(1000);
        }
    }
    public static boolean isAnyPayLessOrEqualZero(Persona persona) {
        if (persona.getPAY_1() <= 0 ||
                persona.getPAY_22() <= 0 ||
                persona.getPAY_23() <= 0 ||
                persona.getPAY_24() <= 0 ||
                persona.getPAY_25() <= 0 ||
                persona.getPAY_26() <= 0) {
            return true;
        }
        return false;
    }
    public static boolean isAnyBillLessOrEqualZero(Persona persona) {
        if (persona.getBILL_1() <= 0 ||
                persona.getBILL_2() <= 0 ||
                persona.getBILL_3() <= 0 ||
                persona.getBILL_4() <= 0 ||
                persona.getBILL_5() <= 0 ||
                persona.getBILL_6() <= 0) {
            return true;
        }
        return false;
    }
}
