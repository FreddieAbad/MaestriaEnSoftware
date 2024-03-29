package org.apache.camel.learn;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.dbcp.BasicDataSource;
import javax.sql.DataSource;

public class PersonaProcesor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Persona persona = (Persona) exchange.getIn().getBody();
        /*boolean pagos = isAnyPayLessOrEqualZero(persona);
        boolean bills = isAnyBillLessOrEqualZero(persona);*/
        boolean pagos = isAnyPayLessOrEqualZero(persona);
        boolean bills = isAnyBillLessOrEqualZero(persona);
        exchange.getMessage().setHeader("pagos", pagos);
        exchange.getMessage().setHeader("bills", bills);

        if (!pagos && !bills) {
            System.out.println("Linea :" + persona.getID());
            System.out.println("Todos los valores de factura a pagar y pagados son mayores a 0");
            System.out.println("Persona " + persona.toString());
        }
    }
    public static boolean isAnyPayLessOrEqualZero(Persona persona) {
        if (Integer.parseInt(persona.getPAY_1()) <= 0 ||
                Integer.parseInt(persona.getPAY_22()) <= 0 ||
                Integer.parseInt(persona.getPAY_23()) <= 0 ||
                Integer.parseInt(persona.getPAY_24()) <= 0 ||
                Integer.parseInt(persona.getPAY_25()) <= 0 ||
                Integer.parseInt(persona.getPAY_26()) <= 0) {
            return true;
        }
        return false;
    }
    public static boolean isAnyBillLessOrEqualZero(Persona persona) {
        if (Integer.parseInt(persona.getBILL_1()) <= 0 ||
                Integer.parseInt(persona.getBILL_2()) <= 0 ||
                Integer.parseInt(persona.getBILL_3()) <= 0 ||
                Integer.parseInt(persona.getBILL_4()) <= 0 ||
                Integer.parseInt(persona.getBILL_5()) <= 0 ||
                Integer.parseInt(persona.getBILL_6()) <= 0) {
            return true;
        }
        return false;
    }
}
