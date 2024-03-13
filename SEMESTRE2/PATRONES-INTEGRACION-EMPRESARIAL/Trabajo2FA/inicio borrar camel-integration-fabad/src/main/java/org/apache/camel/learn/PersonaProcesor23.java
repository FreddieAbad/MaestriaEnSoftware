package org.apache.camel.learn;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultRegistry;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class PersonaProcesor23 implements Processor {
    private BasicDataSource dataSource;

    @Override
    public void process(Exchange exchange) throws Exception {
        //configureDataSource(exchange);
        Persona persona = (Persona) exchange.getIn().getBody();
        boolean pagos = isAnyPayLessOrEqualZero(persona);
        boolean bills = isAnyBillLessOrEqualZero(persona);
        if (!pagos && !bills) {
            System.out.println("Linea :" + persona.getID());
            System.out.println("Todos los valores de factura a pagar y pagados son mayores a 0");
            System.out.println("Persona " + persona.toString());
            conexion2DB(persona);
            //insertarPersona(exchange,persona);
            Thread.sleep(1);
        }
    }

    /*public static void conexion2DB() {
        String urljdbc = "jdbc:postgresql://localhost:5432/postgres";
        DataSource dataSource = setupDatasource(urljdbc);
        DefaultRegistry registry = new DefaultRegistry();
        registry.bind("myDataSource", dataSource);

        CamelContext context = new DefaultCamelContext(registry); // Pass the custom registry to CamelContext constructor
        try {
            context.addRoutes( new PersonaProcesor(). new MyRouteBuilder());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        context.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        context.stop();
    }*/
    public static void conexion2DB(Persona persona) {
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
                            .process(new PersonaProcesor23())
                            .to("jdbc:myDataSource");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        context.start();

        // Crear un intercambio con el insert y enviarlo al inicio de la ruta
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


    public static DataSource setupDatasource (String jdbcUrl){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUsername("postgres");
        ds.setPassword("mysecretpassword");
        ds.setUrl(jdbcUrl);
        return ds;
    }


    /*private void configureDataSource(Exchange exchange) {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("mysecretpassword");

        // Configura el endpoint JDBC y establece el DataSource
        JdbcComponent jdbcComponent = exchange.getContext().getComponent("jdbc", JdbcComponent.class);
        jdbcComponent.setDataSource(dataSource);

    }

    public void insertarPersona(Exchange exchange, Persona persona){
        String query = "INSERT INTO persona (ID, LIMIT_BAL, SEX, EDUCATION, MARRIAGE, AGE, PAY_0, PAY_2, PAY_3, PAY_4, PAY_5, PAY_6, BILL_1, BILL_2, BILL_3, BILL_4, BILL_5, BILL_6, PAY_1, PAY_2_2, PAY_3_2, PAY_4_2, PAY_5_2, PAY_6_2, DEFAULT_PAYMENT_NEXT_MONTH) VALUES (" +
                "'" + persona.getID() + "'," +
                "'" + persona.getLIMIT_BAL() + "'," +
                "'" + persona.getSEX() + "'," +
                "'" + persona.getEDUCATION() + "'," +
                "'" + persona.getMARRIAGE() + "'," +
                "'" + persona.getAGE() + "'," +
                "'" + persona.getPAY_0() + "'," +
                "'" + persona.getPAY_2() + "'," +
                "'" + persona.getPAY_3() + "'," +
                "'" + persona.getPAY_4() + "'," +
                "'" + persona.getPAY_5() + "'," +
                "'" + persona.getPAY_6() + "'," +
                "'" + persona.getBILL_1() + "'," +
                "'" + persona.getBILL_2() + "'," +
                "'" + persona.getBILL_3() + "'," +
                "'" + persona.getBILL_4() + "'," +
                "'" + persona.getBILL_5() + "'," +
                "'" + persona.getBILL_6() + "'," +
                "'" + persona.getPAY_1() + "'," +
                "'" + persona.getPAY_22() + "'," +
                "'" + persona.getPAY_23() + "'," +
                "'" + persona.getPAY_24() + "'," +
                "'" + persona.getPAY_25() + "'," +
                "'" + persona.getPAY_26() + "'," +
                "'" + persona.getDefault_payment_next_month() + "');";

        System.out.println(query);
        // Ejecutar la consulta SQL usando el componente JDBC de Apache Camel
        exchange.getIn().setBody(query);

        try {
            exchange.getContext().createProducerTemplate().send("jdbc:dataSource", exchange);
            System.out.println("Persona insertada correctamente");
        } catch (Exception e) {
            System.err.println("Error al insertar persona: " + e.getMessage());
            e.printStackTrace();
        }
    }*/


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
