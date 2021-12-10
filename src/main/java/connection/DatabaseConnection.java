package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    final String DB_URL = "jdbc:mysql://root@localhost:3306/Labor5";
    final String USER = "root";
    final String PASS = "lutianxing7";

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
