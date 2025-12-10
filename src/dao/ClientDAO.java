package dao;

import model.Client;
import model.User;
import database.DatabaseManager;
import dao_interfaces.IClientDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements IClientDAO {

    @Override
    public boolean addClient(Client client) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO clients(user_id, name, email, phone, license_number, address) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, client.getUser().getUserId());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());
            stmt.setString(5, client.getLicenseNumber());
            stmt.setString(6, client.getAddress());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        client.setClientId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean updateClient(Client client) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE clients SET name=?, email=?, phone=?, license_number=?, address=? " +
                     "WHERE client_id=?";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getLicenseNumber());
            stmt.setString(5, client.getAddress());
            stmt.setInt(6, client.getClientId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteClient(int clientId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM clients WHERE client_id=?";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Client getClientById(int clientId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT c.*, u.username, u.email AS user_email, u.role " +
                     "FROM clients c JOIN users u ON c.user_id = u.user_id WHERE c.client_id=?";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("user_email"),
                            rs.getString("role")
                    );

                    return new Client(
                            rs.getInt("client_id"),
                            user,
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("license_number"),
                            rs.getString("address")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<Client> getAllClients() throws SQLException, ClassNotFoundException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT c.*, u.username, u.email AS user_email, u.role " +
                     "FROM clients c JOIN users u ON c.user_id = u.user_id";

        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("user_email"),
                        rs.getString("role")
                );

                clients.add(new Client(
                        rs.getInt("client_id"),
                        user,
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("license_number"),
                        rs.getString("address")
                ));
            }
        }

        return clients;
    }
    
    
    
 // Add to ClientDAO.java
    public Client getClientByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT c.*, u.username, u.email AS user_email, u.role " +
                     "FROM clients c JOIN users u ON c.user_id = u.user_id " +
                     "WHERE u.user_id=?";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("user_email"),
                        rs.getString("role")
                    );
                    
                    return new Client(
                        rs.getInt("client_id"),
                        user,
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("license_number"),
                        rs.getString("address")
                    );
                }
            }
        }
        return null;
    }

    public Client getClientByEmail(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT c.*, u.username, u.email AS user_email, u.role " +
                     "FROM clients c JOIN users u ON c.user_id = u.user_id " +
                     "WHERE c.email=?";
        
        try (Connection conn = DatabaseManager.getSingleton().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("user_email"),
                        rs.getString("role")
                    );
                    
                    return new Client(
                        rs.getInt("client_id"),
                        user,
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("license_number"),
                        rs.getString("address")
                    );
                }
            }
        }
        return null;
    }
}
