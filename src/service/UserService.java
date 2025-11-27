package service;

import dao_interfaces.IUserDAO;

import java.sql.SQLException;

import dao.UserDAO;
import model.User;

public class UserService {

    private final IUserDAO userDAO = new UserDAO();

    public boolean isEmailTaken(String email) throws ClassNotFoundException, SQLException {
        return userDAO.isEmailTaken(email);
    }

    public int addUser(User user) throws ClassNotFoundException, SQLException {
        // Force role = client for GUI registration
        user.setRole("client");
        return userDAO.addUser(user);
    }

    public User login(String username, String password) throws ClassNotFoundException, SQLException {
        return userDAO.login(username, password);
    }

    public User getUserById(int id) throws ClassNotFoundException, SQLException {
        return userDAO.getUserById(id);
    }
}
