package org.apache.camel.learn;
import org.apache.camel.main.Main;
import org.apache.commons.dbcp.BasicDataSource;
import javax.sql.DataSource;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class MainApp {
    public static void main(String... args) throws Exception {
        /*String urljdbc = "jdbc:postgresql://localhost:5432/postgres";
        DataSource dataSource = setupDatasource(urljdbc);
        DefaultRegistry registry = new DefaultRegistry();
        registry.bind("myDataSource", dataSource);
        /////
        CamelContext context = new DefaultCamelContext(registry); // Pass the custom registry to CamelContext constructor
        context.addRoutes(new MyRoute5());
        context.start();
        Thread.sleep(2000);
        context.stop();*/

        Main main = new Main();
        main.configure().addRoutesBuilder(new MyRoute5());
        main.run(args);
    }
    public static DataSource setupDatasource(String jdbcUrl) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUsername("postgres");
        ds.setPassword("mysecretpassword");
        ds.setUrl(jdbcUrl);
        return ds;
    }

}