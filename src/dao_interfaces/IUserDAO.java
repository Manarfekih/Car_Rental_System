package dao_interfaces;

import java.sql.SQLException;
import java.util.List;

import model.User;

public interface IUserDAO {
    
    boolean isEmailTaken(String email) throws SQLException, ClassNotFoundException;

    int addUser(User user) throws SQLException, ClassNotFoundException;

    User login(String username, String password) throws SQLException, ClassNotFoundException;

    User getUserById(int userId) throws SQLException, ClassNotFoundException;

    List<User> getAllUsers() throws SQLException, ClassNotFoundException;
    
    boolean updateUser(User user) throws SQLException, ClassNotFoundException;
}