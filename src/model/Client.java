package model;

import java.util.Comparator;

public class Client implements Comparable<Client> {

    private int clientId;
    private User user; 
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private String address;

    // Constructor
    public Client(int clientId, User user, String name, 
                  String email, String phone, 
                  String licenseNumber, String address) {

        this.clientId = clientId;
        this.user = user;   // Object reference, not primitive ID
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.address = address;
    }

    // Getters & Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public User getUser() {
        return user;
    }    

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name; 
    }
    
    
    @Override
    public int compareTo(Client other) {
        if (other == null) return 1;
        return this.name.compareToIgnoreCase(other.name);
    }
    
    
    public static Comparator<Client> SORT_BY_EMAIL = Comparator.comparing(Client::getEmail, String.CASE_INSENSITIVE_ORDER);
    public static Comparator<Client> SORT_BY_LICENSE = Comparator.comparing(Client::getLicenseNumber, String.CASE_INSENSITIVE_ORDER);

}
