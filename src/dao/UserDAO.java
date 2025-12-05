package dao;

import dao_interfaces.IUserDAO;
import database.DatabaseManager;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    @Override
    public boolean isEmailTaken(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM users WHERE email=?";
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            return stmt.executeQuery().next();
        }
    }

    @Override
    public int addUser(User user) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            int rows = stmt.executeUpdate();
            if (rows == 0) return -1;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public User login(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public User getUserById(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE user_id=?";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() throws SQLException, ClassNotFoundException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User u = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                list.add(u);
            }
        }

        return list;
    }
    
    @Override
    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getUserId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
}