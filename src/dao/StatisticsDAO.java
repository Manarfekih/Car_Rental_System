package dao;

import database.DatabaseManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import dao_interfaces.IStatisticsDAO;


public class StatisticsDAO implements IStatisticsDAO {
	
	@Override
	// 1. Total Revenue
    public double getTotalRevenue() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(total_price) as total FROM rentals WHERE status = 'completed'";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }
    
	
	
	
	@Override
 // 3. Average Rental Duration
    public double getAverageRentalDuration() throws SQLException, ClassNotFoundException {
        String sql = "SELECT AVG(DATEDIFF(end_date, start_date)) as avg_days " +
                    "FROM rentals WHERE status = 'completed'";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getDouble("avg_days") : 0.0;
        }
    }
    
	
	@Override
    // 4. Active Rentals Count
    public int getActiveRentalsCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as count FROM rentals WHERE status = 'active'";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getInt("count") : 0;
        }
    }
	
	
	
	@Override
    // 5. Total Cars Count
    public int getTotalCarsCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as count FROM cars";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getInt("count") : 0;
        }
    }
    
	
	
	@Override
    // 6. Total Clients Count
    public int getTotalClientsCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as count FROM clients";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getInt("count") : 0;
        }
    }
	
	
	@Override
    // 7. Cars by Status
    public Map<String, Integer> getCarsByStatus() throws SQLException, ClassNotFoundException {
        String sql = "SELECT status, COUNT(*) as count FROM cars GROUP BY status";
        
        Map<String, Integer> result = new HashMap<>();
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                result.put(rs.getString("status"), rs.getInt("count"));
            }
        }
        return result;
    }
    


}
