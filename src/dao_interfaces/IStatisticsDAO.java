package dao_interfaces;

import java.sql.SQLException;
import java.util.Map;

public interface IStatisticsDAO {

	Map<String, Integer> getCarsByStatus() throws SQLException, ClassNotFoundException;

	int getTotalClientsCount() throws SQLException, ClassNotFoundException;

	int getTotalCarsCount() throws SQLException, ClassNotFoundException;

	int getActiveRentalsCount() throws SQLException, ClassNotFoundException;

	double getAverageRentalDuration() throws SQLException, ClassNotFoundException;


	double getTotalRevenue() throws SQLException, ClassNotFoundException;

}
