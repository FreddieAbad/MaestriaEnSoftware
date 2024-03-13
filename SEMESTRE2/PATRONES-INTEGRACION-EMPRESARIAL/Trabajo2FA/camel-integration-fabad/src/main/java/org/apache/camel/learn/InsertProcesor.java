package org.apache.camel.learn;

import javax.sql.DataSource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.dbcp.BasicDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertProcesor implements Processor {

    private DataSource dataSource;

    public InsertProcesor() {
        String jdbcUrl = "jdbc:postgresql://localhost:5433/postgres";
        DataSource dataSource = setupDatasource(jdbcUrl);

        this.dataSource = dataSource;
    }
    public static DataSource setupDatasource(String jdbcUrl){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUsername("postgres");
        ds.setPassword("admin");
        ds.setUrl(jdbcUrl);
        return ds;
    }
    @Override
    public void process(Exchange exchange) throws Exception {
        Persona persona = (Persona) exchange.getIn().getBody();
        System.out.println("Prueba ####  :" + persona.toString());
        String sql = "INSERT INTO persona (ID, LIMIT_BAL, SEX, EDUCATION, MARRIAGE, AGE, PAY_0, PAY_2, PAY_3, PAY_4, PAY_5, PAY_6, BILL_1, BILL_2, BILL_3, BILL_4, BILL_5, BILL_6, PAY_1, PAY_22, PAY_23, PAY_24, PAY_25, PAY_26, default_payment_next_month) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // Establecer los valores de los parámetros en la consulta preparada
            statement.setString(1, persona.getID());
            statement.setString(2, persona.getLIMIT_BAL());
            statement.setString(3, persona.getSEX());
            statement.setString(4, persona.getEDUCATION());
            statement.setString(5, persona.getMARRIAGE());
            statement.setString(6, persona.getAGE());
            statement.setString(7, persona.getPAY_0());
            statement.setString(8, persona.getPAY_2());
            statement.setString(9, persona.getPAY_3());
            statement.setString(10, persona.getPAY_4());
            statement.setString(11, persona.getPAY_5());
            statement.setString(12, persona.getPAY_6());
            statement.setString(13, persona.getBILL_1());
            statement.setString(14, persona.getBILL_2());
            statement.setString(15, persona.getBILL_3());
            statement.setString(16, persona.getBILL_4());
            statement.setString(17, persona.getBILL_5());
            statement.setString(18, persona.getBILL_6());
            statement.setString(19, persona.getPAY_1());
            statement.setString(20, persona.getPAY_22());
            statement.setString(21, persona.getPAY_23());
            statement.setString(22, persona.getPAY_24());
            statement.setString(23, persona.getPAY_25());
            statement.setString(24, persona.getPAY_26());
            statement.setString(25, persona.getDefault_payment_next_month());

            // Ejecutar la inserción
            statement.executeUpdate();
        }
        /*try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO persona (ID, LIMIT_BAL, SEX, EDUCATION, MARRIAGE, AGE, PAY_0, PAY_2, PAY_3, PAY_4, PAY_5, PAY_6, BILL_1, BILL_2, BILL_3, BILL_4, BILL_5, BILL_6, PAY_1, PAY_22, PAY_23, PAY_24, PAY_25, PAY_26, default_payment_next_month)  VALUES ('1', '20000', '2', '2', '1', '24', '2', '2', '-1', '-1', '-2', '-2', '3913', '3102', '689', '0', '0', '0', '0', '689', '0', '0', '0', '0', '1');\n")) {
            statement.executeUpdate();
        }*/
    }
}
