package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        try {
            // For MySQL Connector 5.0.7, use this driver class:
            Class.forName("com.mysql.jdbc.Driver");
            
            // For newer MySQL connectors, you would use:
            // Class.forName("com.mysql.cj.jdbc.Driver");
            
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
}