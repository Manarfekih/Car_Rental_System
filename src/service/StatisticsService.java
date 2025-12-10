/*
 * package service;
 * 
 * import dao.StatisticsDAO; import dao_interfaces.IStatisticsDAO; import
 * java.sql.SQLException; import java.util.Map;
 * 
 * public class StatisticsService {
 * 
 * private final IStatisticsDAO statisticsDAO;
 * 
 * public StatisticsService() { this.statisticsDAO = new StatisticsDAO(); }
 * 
 * // Direct methods from DAO (same pattern as ClientService)
 * 
 * public double getTotalRevenue() throws ClassNotFoundException, SQLException {
 * return statisticsDAO.getTotalRevenue(); }
 * 
 * public double getAverageRentalDuration() throws ClassNotFoundException,
 * SQLException { return statisticsDAO.getAverageRentalDuration(); }
 * 
 * public int getActiveRentalsCount() throws ClassNotFoundException,
 * SQLException { return statisticsDAO.getActiveRentalsCount(); }
 * 
 * public int getTotalCarsCount() throws ClassNotFoundException, SQLException {
 * return statisticsDAO.getTotalCarsCount(); }
 * 
 * public int getTotalClientsCount() throws ClassNotFoundException, SQLException
 * { return statisticsDAO.getTotalClientsCount(); }
 * 
 * public Map<String, Integer> getCarsByStatus() throws ClassNotFoundException,
 * SQLException { return statisticsDAO.getCarsByStatus(); } }
 */