package dao;

import dao_interfaces.IRentalDAO;
import model.Rental;
import model.Client;
import model.Car;
import service.ClientService;
import service.CarService;
import database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO implements IRentalDAO {

    private final ClientService clientService = new ClientService();
    private final CarService carService = new CarService();

    // ---------------- CREATE RENTAL ----------------
    @Override
    public boolean createRental(Rental rental) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO rentals (client_id, car_id, start_date, end_date, total_price, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, rental.getClient().getClientId());
            stmt.setInt(2, rental.getCar().getCarId());
            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
            stmt.setDouble(5, rental.getTotalPrice());
            stmt.setString(6, rental.getStatus());

            int rows = stmt.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    rental.setRentalId(keys.getInt(1));
                }
            }
            return true;
        }
    }

    // ---------------- UPDATE RENTAL ----------------
    @Override
    public boolean updateRental(Rental rental) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE rentals SET client_id=?, car_id=?, start_date=?, end_date=?, total_price=?, status=? WHERE rental_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rental.getClient().getClientId());
            stmt.setInt(2, rental.getCar().getCarId());
            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
            stmt.setDouble(5, rental.getTotalPrice());
            stmt.setString(6, rental.getStatus());
            stmt.setInt(7, rental.getRentalId());

            return stmt.executeUpdate() > 0;
        }
    }

    // ---------------- DELETE RENTAL ----------------
    @Override
    public boolean deleteRental(int rentalId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM rentals WHERE rental_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ---------------- RETURN CAR ----------------
    @Override
    public boolean returnCar(int rentalId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE rentals r JOIN cars c ON r.car_id = c.car_id " +
                     "SET r.status = 'completed', c.status = 'available' " +
                     "WHERE r.rental_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ---------------- GET RENTAL BY ID ----------------
    @Override
    public Rental getRentalById(int rentalId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM rentals WHERE rental_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = clientService.getClientById(rs.getInt("client_id"));
                    Car car = carService.getCarById(rs.getInt("car_id"));
                    return new Rental(
                            rs.getInt("rental_id"),
                            client,
                            car,
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getDouble("total_price"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    // ---------------- GET ALL RENTALS ----------------
    @Override
    public List<Rental> getAllRentals() throws SQLException, ClassNotFoundException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client client = clientService.getClientById(rs.getInt("client_id"));
                Car car = carService.getCarById(rs.getInt("car_id"));
                rentals.add(new Rental(
                        rs.getInt("rental_id"),
                        client,
                        car,
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                ));
            }
        }
        return rentals;
    }

    // ---------------- SEARCH RENTAL ----------------
    @Override
    public List<Rental> searchRental(String keyword) throws SQLException, ClassNotFoundException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals r JOIN clients c ON r.client_id=c.client_id " +
                     "JOIN cars car ON r.car_id=car.car_id " +
                     "WHERE c.name LIKE ? OR car.model LIKE ? OR r.status LIKE ?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);
            stmt.setString(3, like);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Client client = clientService.getClientById(rs.getInt("client_id"));
                    Car car = carService.getCarById(rs.getInt("car_id"));
                    rentals.add(new Rental(
                            rs.getInt("rental_id"),
                            client,
                            car,
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getDouble("total_price"),
                            rs.getString("status")
                    ));
                }
            }
        }
        return rentals;
    }

    // ---------------- CHECK RENTAL EXISTS ----------------
    @Override
    public boolean rentalExists(int rentalId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM rentals WHERE rental_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    @Override
    public int getActiveRentalsCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as count FROM rentals WHERE status = 'active'";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

}
