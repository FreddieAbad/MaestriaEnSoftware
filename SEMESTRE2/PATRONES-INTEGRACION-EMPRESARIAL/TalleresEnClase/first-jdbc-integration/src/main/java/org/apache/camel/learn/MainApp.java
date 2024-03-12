package org.apache.camel.learn;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.support.DefaultRegistry;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    // public static void main(String... args) throws Exception {
    //    String urljdbc = "jdbc:postgresql://localhost:5432/postgres";
    //    DataSource dataSource = setupDatasource(urljdbc);
    //    DefaultRegistry registry = new DefaultRegistry();
    //    registry.bind("myDataSource", dataSource);

    //    CamelContext context = new DefaultCamelContext();
    //    context.addRoutes(new MainApp().new MyRouteBuilder());
    //    context.start();
    //    Thread.sleep(2000);
    //    context.stop();
    // }

    public static void main(String... args) throws Exception {
        String urljdbc = "jdbc:postgresql://localhost:5432/postgres";
        DataSource dataSource = setupDatasource(urljdbc);
        DefaultRegistry registry = new DefaultRegistry();
        registry.bind("myDataSource", dataSource);

        CamelContext context = new DefaultCamelContext(registry); // Pass the custom registry to CamelContext constructor
        context.addRoutes(new MainApp().new MyRouteBuilder());
        context.start();
        Thread.sleep(2000);
        context.stop();
    }

    public static DataSource setupDatasource (String jdbcUrl){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUsername("camel");
        ds.setPassword("camle");
        ds.setUrl(jdbcUrl);
        return ds;
    }
    public class MyRouteBuilder extends RouteBuilder {
        public void configure(){
            from("timer://foo?period=1000")
            .process(new SimpleProcesador())
            .to("jdbc:myDataSource")
            .to("log:JDBC");
        }
    }

}

