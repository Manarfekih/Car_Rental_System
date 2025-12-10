package model;

import java.time.LocalDate;
import java.util.Comparator;

public class Rental {

    private int rentalId;
    private Client client;   
    private Car car;         
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private String status; 

    // Constructor
    public Rental(int rentalId, Client client, Car car,
                  LocalDate startDate, LocalDate endDate,
                  double totalPrice, String status) {

        this.rentalId = rentalId;
        this.client = client;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters & Setters
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", client=" + client.getName() +
                ", car=" + car.getBrand() + " " + car.getModel() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                '}';
    }
    
    ///---------------Compartors----------------
    
    public static Comparator<Rental> SORT_BY_START_DATE = Comparator.comparing(Rental::getStartDate);
    public static Comparator<Rental> SORT_BY_END_DATE = Comparator.comparing(Rental::getEndDate);
    public static Comparator<Rental> SORT_BY_TOTAL_PRICE = Comparator.comparingDouble(Rental::getTotalPrice);
    public static Comparator<Rental> SORT_BY_CLIENT_NAME = 
        Comparator.comparing(r -> r.getClient().getName());
  
    
  
}
