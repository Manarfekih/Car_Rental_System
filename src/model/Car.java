package model;

public class Car {
	
	private int carId;
    private String brand;
    private String model;
    private int year;
    private String category;
    private double pricePerDay;
    private String status; // available, rented, maintenance
    
    //Constructor
	public Car(int carId, String brand, String model, int year, String category, double pricePerDay, String status) {
		super();
		this.carId = carId;
		this.brand = brand;
		this.model = model;
		this.year = year;
		this.category = category;
		this.pricePerDay = pricePerDay;
		this.status = status;
	}
	
	//Getters & Setters

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	//ToString method
	@Override
	public String toString() {
		return "Car [carId=" + carId + ", brand=" + brand + ", model=" + model + ", year=" + year + ", category="
				+ category + ", pricePerDay=" + pricePerDay + ", status=" + status + "]";
	}

	
	
	
	
    
    
    
}
