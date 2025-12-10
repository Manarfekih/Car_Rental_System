package dao;

import dao_interfaces.IRentalDAO;
import model.Rental;
import model.Client;
import model.Car;
import model.User;
import service.ClientService;
import service.CarService;
import database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO implements IRentalDAO {

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
                    // Get client and car IDs
                    int clientId = rs.getInt("client_id");
                    int carId = rs.getInt("car_id");
                    
                    // Get client and car objects
                    ClientService clientService = new ClientService();
                    CarService carService = new CarService();
                    
                    Client client = clientService.getClientById(clientId);
                    Car car = carService.getCarById(carId);
                    
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

    // ---------------- GET ALL RENTALS - SIMPLIFIED ----------------
    @Override
    public List<Rental> getAllRentals() throws SQLException, ClassNotFoundException {
        List<Rental> rentals = new ArrayList<>();
        
        // Simple query to get all rentals
        String sql = "SELECT * FROM rentals ORDER BY rental_id";
        
        System.out.println("DEBUG: Executing getAllRentals query...");
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // First, collect all rental data
            List<Object[]> rentalData = new ArrayList<>();
            List<Integer> clientIds = new ArrayList<>();
            List<Integer> carIds = new ArrayList<>();
            
            while (rs.next()) {
                int rentalId = rs.getInt("rental_id");
                int clientId = rs.getInt("client_id");
                int carId = rs.getInt("car_id");
                
                clientIds.add(clientId);
                carIds.add(carId);
                
                rentalData.add(new Object[]{
                    rentalId,
                    clientId,
                    carId,
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rs.getDouble("total_price"),
                    rs.getString("status")
                });
                
                System.out.println("DEBUG: Found rental ID: " + rentalId + 
                                 ", Client ID: " + clientId + ", Car ID: " + carId);
            }
            
            // Now get all clients and cars
            ClientService clientService = new ClientService();
            CarService carService = new CarService();
            
            // Get unique client IDs
            for (int clientId : clientIds) {
                try {
                    Client client = clientService.getClientById(clientId);
                    if (client == null) {
                        System.err.println("WARNING: Client not found for ID: " + clientId);
                    }
                } catch (Exception e) {
                    System.err.println("Error getting client " + clientId + ": " + e.getMessage());
                }
            }
            
            // Get unique car IDs
            for (int carId : carIds) {
                try {
                    Car car = carService.getCarById(carId);
                    if (car == null) {
                        System.err.println("WARNING: Car not found for ID: " + carId);
                    }
                } catch (Exception e) {
                    System.err.println("Error getting car " + carId + ": " + e.getMessage());
                }
            }
            
            // Now create Rental objects
            for (Object[] data : rentalData) {
                try {
                    int rentalId = (Integer) data[0];
                    int clientId = (Integer) data[1];
                    int carId = (Integer) data[2];
                    Date startDate = (Date) data[3];
                    Date endDate = (Date) data[4];
                    double totalPrice = (Double) data[5];
                    String status = (String) data[6];
                    
                    Client client = clientService.getClientById(clientId);
                    Car car = carService.getCarById(carId);
                    
                    if (client != null && car != null) {
                        Rental rental = new Rental(
                            rentalId,
                            client,
                            car,
                            startDate.toLocalDate(),
                            endDate.toLocalDate(),
                            totalPrice,
                            status
                        );
                        rentals.add(rental);
                        System.out.println("DEBUG: Created rental ID: " + rentalId);
                    } else {
                        System.err.println("SKIPPING rental " + rentalId + 
                                         " - Client or Car not found");
                    }
                    
                } catch (Exception e) {
                    System.err.println("ERROR creating rental object: " + e.getMessage());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("SQL ERROR in getAllRentals: " + e.getMessage());
            throw e;
        }
        
        System.out.println("DEBUG: getAllRentals returned " + rentals.size() + " rentals");
        return rentals;
    }

    // ---------------- SEARCH RENTAL ----------------
    @Override
    public List<Rental> searchRental(String keyword) throws SQLException, ClassNotFoundException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals r " +
                     "WHERE r.status LIKE ?";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            stmt.setString(1, like);

            try (ResultSet rs = stmt.executeQuery()) {
                // Collect data first
                List<Object[]> rentalData = new ArrayList<>();
                
                while (rs.next()) {
                    int rentalId = rs.getInt("rental_id");
                    int clientId = rs.getInt("client_id");
                    int carId = rs.getInt("car_id");
                    
                    rentalData.add(new Object[]{
                        rentalId,
                        clientId,
                        carId,
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                    });
                }
                
                // Create Rental objects
                ClientService clientService = new ClientService();
                CarService carService = new CarService();
                
                for (Object[] data : rentalData) {
                    int rentalId = (Integer) data[0];
                    int clientId = (Integer) data[1];
                    int carId = (Integer) data[2];
                    
                    Client client = clientService.getClientById(clientId);
                    Car car = carService.getCarById(carId);
                    
                    if (client != null && car != null) {
                        rentals.add(new Rental(
                            rentalId,
                            client,
                            car,
                            ((Date) data[3]).toLocalDate(),
                            ((Date) data[4]).toLocalDate(),
                            (Double) data[5],
                            (String) data[6]
                        ));
                    }
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
    
    // ---------------- SIMPLE TEST METHOD ----------------
    public List<Rental> getRentalsForClient(int clientId) throws SQLException, ClassNotFoundException {
        List<Rental> rentals = new ArrayList<>();
        
        String sql = "SELECT * FROM rentals WHERE client_id = ? ORDER BY rental_id";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Object[]> rentalData = new ArrayList<>();
                List<Integer> carIds = new ArrayList<>();
                
                while (rs.next()) {
                    int rentalId = rs.getInt("rental_id");
                    int carId = rs.getInt("car_id");
                    
                    carIds.add(carId);
                    
                    rentalData.add(new Object[]{
                        rentalId,
                        carId,
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                    });
                }
                
                // Get car service once
                CarService carService = new CarService();
                
                for (Object[] data : rentalData) {
                    int rentalId = (Integer) data[0];
                    int carId = (Integer) data[1];
                    
                    Car car = carService.getCarById(carId);
                    
                    if (car != null) {
                        // Get client
                        ClientService clientService = new ClientService();
                        Client client = clientService.getClientById(clientId);
                        
                        if (client != null) {
                            Rental rental = new Rental(
                                rentalId,
                                client,
                                car,
                                ((Date) data[2]).toLocalDate(),
                                ((Date) data[3]).toLocalDate(),
                                (Double) data[4],
                                (String) data[5]
                            );
                            rentals.add(rental);
                        }
                    }
                }
            }
        }
        
        return rentals;
    }
}