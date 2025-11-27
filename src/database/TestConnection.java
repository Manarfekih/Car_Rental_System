package database;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Get connection through the singleton
            Connection conn = DatabaseManager.getSingleton().getConnection();
            System.out.println("✅ Database connection SUCCESS!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Database connection FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
