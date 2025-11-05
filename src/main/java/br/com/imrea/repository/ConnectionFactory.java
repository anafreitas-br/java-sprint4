package br.com.imrea.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private String user = "RM565559";
    private String pwd = "fiap25";
    private String jdbc = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";

    public Connection getConnection() throws SQLException {
        Connection con = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(this.jdbc, this.user, this.pwd);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
