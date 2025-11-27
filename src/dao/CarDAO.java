package dao;

import model.Car;
import dao_interfaces.ICarDAO;
import database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO implements ICarDAO {

    @Override
    public boolean addCar(Car car) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO cars (brand, model, year, category, price_per_day, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setString(4, car.getCategory());
            stmt.setDouble(5, car.getPricePerDay());
            stmt.setString(6, car.getStatus());

            int rows = stmt.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    car.setCarId(rs.getInt(1)); // set generated ID
                }
            }

            return true;
        }
    }

    @Override
    public boolean updateCar(Car car) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE cars SET brand=?, model=?, year=?, category=?, price_per_day=?, status=? WHERE car_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setString(4, car.getCategory());
            stmt.setDouble(5, car.getPricePerDay());
            stmt.setString(6, car.getStatus());
            stmt.setInt(7, car.getCarId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteCar(int carId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM cars WHERE car_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Car getCarById(int carId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM cars WHERE car_id=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Car(
                            rs.getInt("car_id"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getInt("year"),
                            rs.getString("category"),
                            rs.getDouble("price_per_day"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Car> getAllCars() throws SQLException, ClassNotFoundException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("car_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("category"),
                        rs.getDouble("price_per_day"),
                        rs.getString("status")
                ));
            }
        }

        return cars;
    }
}
