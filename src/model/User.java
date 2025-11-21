package model;

import java.util.Arrays;
import java.util.List;

public class User {

    private int userId;
    private String username;
    private String email;
    private String password;
    private String role;

    // Define valid roles
    private static final List<String> VALID_ROLES = Arrays.asList("admin", "employee", "client");

    // Constructor
    public User(int userId, String username, String email, String password, String role) {
        this.userId = userId;
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setRole(role);
    }

    // --- Getters & Setters ---

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        this.email = email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.password = password.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || !VALID_ROLES.contains(role.trim().toLowerCase())) {
            throw new IllegalArgumentException("Invalid role. Must be one of: " + VALID_ROLES);
        }
        this.role = role.trim().toLowerCase();
    }

    // Optional: String representation for debugging
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
