package dao_interfaces;

import model.Rental;

import java.sql.SQLException;
import java.util.List;

public interface IRentalDAO {

    // Check if a rental exists by ID
    boolean rentalExists(int rentalId) throws SQLException, ClassNotFoundException;

    // Create a new rental
    boolean createRental(Rental rental) throws SQLException, ClassNotFoundException;

    // Update an existing rental
    boolean updateRental(Rental rental) throws SQLException, ClassNotFoundException;

    // Delete a rental
    boolean deleteRental(int rentalId) throws SQLException, ClassNotFoundException;

    // Mark a rental as returned (completed)
    boolean returnCar(int rentalId) throws SQLException, ClassNotFoundException;

    // Get a rental by ID
    Rental getRentalById(int rentalId) throws SQLException, ClassNotFoundException;

    // Get all rentals
    List<Rental> getAllRentals() throws SQLException, ClassNotFoundException;

    // Search rentals by client, car, or status
    List<Rental> searchRental(String keyword) throws SQLException, ClassNotFoundException;

	int getActiveRentalsCount() throws SQLException, ClassNotFoundException;

}
