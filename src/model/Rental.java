package model;

import java.time.LocalDate;

public class Rental {
	
	private int rentalId;
    private int clientId;
    private int carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private String status; // active, completed, cancelled
    
    //Constructor 
	public Rental(int rentalId, int clientId, int carId, LocalDate startDate, LocalDate endDate, double totalPrice,
			String status) {
		super();
		this.rentalId = rentalId;
		this.clientId = clientId;
		this.carId = carId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalPrice = totalPrice;
		this.status = status;
	}

	
	//Getters & Setters
	public int getRentalId() {
		return rentalId;
	}

	public void setRentalId(int rentalId) {
		this.rentalId = rentalId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
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

    
	//ToString
	@Override
	public String toString() {
		return "Rental [rentalId=" + rentalId + ", clientId=" + clientId + ", carId=" + carId + ", startDate="
				+ startDate + ", endDate=" + endDate + ", totalPrice=" + totalPrice + ", status=" + status + "]";
	}
    
    
	
    


}
