package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager singleton;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Private constructor
    private DatabaseManager() throws SQLException, ClassNotFoundException {
        // Load MySQL driver
        Class.forName("com.mysql.jdbc.Driver");
        // Initialize connection
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Public method to get the singleton instance
    public static DatabaseManager getSingleton() throws SQLException, ClassNotFoundException {
        if (singleton == null || singleton.getConnection().isClosed()) {
            singleton = new DatabaseManager();
        }
        return singleton;
    }

    // Getter for connection
    public Connection getConnection() {
        return connection;
    }
}
