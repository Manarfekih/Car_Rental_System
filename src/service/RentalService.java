package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseManager;
import model.Rental;

public class RentalService {

    // rent a car
    public boolean createRental(Rental rental) {
        String sql = "INSERT INTO rentals (client_id, car_id, start_date, end_date, total_price, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rental.getClientId());
            stmt.setInt(2, rental.getCarId());
            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
            stmt.setDouble(5, rental.getTotalPrice());
            stmt.setString(6, rental.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // return a car --> update rental + update car status
    public boolean returnCar(int rentalId) {
        String sql = "UPDATE rentals r JOIN cars c ON c.car_id = r.car_id "
                   + "SET r.status = 'completed', c.status = 'available' "
                   + "WHERE r.rental_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // get all rentals
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("client_id"),
                        rs.getInt("car_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }
}
