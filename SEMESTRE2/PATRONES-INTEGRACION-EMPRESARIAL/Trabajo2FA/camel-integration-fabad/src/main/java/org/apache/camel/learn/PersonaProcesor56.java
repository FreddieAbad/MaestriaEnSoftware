package org.apache.camel.learn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class PersonaProcesor56 implements Processor {

    /*public PersonaProcesor() throws Exception {
            SimpleRegistry registry = new SimpleRegistry();
            DataSource dataSource = setupDatasource("jdbc:postgresql://localhost:5432/postgres");
            registry.bind("myDataSource", dataSource);

            DefaultCamelContext context = new DefaultCamelContext(registry);

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:insert")
                            .setBody(simple("INSERT INTO persona (ID, LIMIT_BAL, SEX, EDUCATION, MARRIAGE, AGE, PAY_0, PAY_2, PAY_3, PAY_4, PAY_5, PAY_6, BILL_1, BILL_2, BILL_3, BILL_4, BILL_5, BILL_6, PAY_1, PAY_2_2, PAY_3_2, PAY_4_2, PAY_5_2, PAY_6_2, DEFAULT_PAYMENT_NEXT_MONTH) VALUES ('393','310000','1','2','1','44','0','0','0','0','0','0','103797','91989','87720','76218','72090','71620','4400','4013','3011','3000','3001','2653','0');\n"))
                            .to("jdbc:myDataSource");
                }
            });

            context.start();

            // Insertar datos si se cumple la condición
            Persona persona = new Persona();
            // Establecer datos de la persona
            persona.setID(1);
            persona.setLIMIT_BAL(20000);
            persona.setSEX(2);
            persona.setEDUCATION(2);
            persona.setMARRIAGE(1);
            persona.setAGE(24);
            persona.setPAY_0(2);
            persona.setPAY_2(2);
            persona.setPAY_3(-1);
            persona.setPAY_4(-1);
            persona.setPAY_5(-2);
            persona.setPAY_6(-2);
            persona.setBILL_1(3913);
            persona.setBILL_2(3102);
            persona.setBILL_3(689);
            persona.setBILL_4(0);
            persona.setBILL_5(0);
            persona.setBILL_6(0);
            persona.setPAY_1(0);
            persona.setPAY_22(689);
            persona.setPAY_23(0);
            persona.setPAY_24(0);
            persona.setPAY_25(0);
            persona.setPAY_26(0);
            persona.setDefault_payment_next_month(1);

            if (!isAnyPayLessOrEqualZero(persona) && !isAnyBillLessOrEqualZero(persona)) {
                context.createProducerTemplate().sendBody("direct:insert", persona);
            }

            Thread.sleep(2000);
            context.stop();

    }*/

    @Override
    public void process(Exchange exchange) throws Exception {


        Persona persona = (Persona) exchange.getIn().getBody();
        boolean pagos = isAnyPayLessOrEqualZero(persona);
        boolean bills = isAnyBillLessOrEqualZero(persona);
        if (!pagos && !bills) {
            System.out.println("Linea :" + persona.getID());
            System.out.println("Todos los valores de factura a pagar y pagados son mayores a 0");
            System.out.println("Persona " + persona.toString());
            //conexion2DB(persona);
            exchange.getContext().createProducerTemplate().sendBody("direct:insert", persona);
            Thread.sleep(1000);
        }

    }
   /* public static void conexion2DB(Persona persona) {
        String urljdbc = "jdbc:postgresql://localhost:5432/postgres";
        DataSource dataSource = setupDatasource(urljdbc);
        DefaultRegistry registry = new DefaultRegistry();
        registry.bind("myDataSource", dataSource);

        CamelContext context = new DefaultCamelContext(registry); // Pass the custom registry to CamelContext constructor
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start")
                            .process(new PersonaProcesor())
                            .to("jdbc:myDataSource");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        context.start();

        Exchange exchange = context.createProducerTemplate().send("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(persona);
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        context.stop();
    }
    */
   public static DataSource setupDatasource (String jdbcUrl){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUsername("postgres");
        ds.setPassword("mysecretpassword");
        ds.setUrl(jdbcUrl);
        return ds;
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
