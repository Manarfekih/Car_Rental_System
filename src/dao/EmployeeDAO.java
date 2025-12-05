package dao;

import database.DatabaseManager;
import model.Employee;
import dao_interfaces.IEmployeeDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements IEmployeeDAO {

    @Override
    public int addEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, 'employee')";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getUsername());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE user_id = ? AND role='employee'";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getUsername());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPassword());
            stmt.setInt(4, employee.getUserId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteEmployee(int userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM users WHERE user_id = ? AND role='employee'";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws SQLException, ClassNotFoundException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='employee'";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        }
        return list;
    }

    @Override
    public List<Employee> searchEmployees(String keyword) throws SQLException, ClassNotFoundException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='employee' AND (username LIKE ? OR email LIKE ?)";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Employee(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password")
                    ));
                }
            }
        }
        return list;
    }

    // -------------------------
    // Check duplicate email for update
    // -------------------------
    public boolean isEmailTakenByOtherEmployee(String email, int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND user_id != ? AND role='employee'";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // -------------------------
    // Check duplicate email for new employee
    // -------------------------
    public boolean isEmailTaken(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND role='employee'";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
