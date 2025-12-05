package service;

import model.Car;

import java.sql.SQLException;
import java.util.List;
import dao.CarDAO;
import dao_interfaces.ICarDAO;

public class CarService {

    private final ICarDAO carDAO;

    public CarService() {
        this.carDAO = new CarDAO();}

    public boolean addCar(Car car) throws ClassNotFoundException, SQLException {
        return carDAO.addCar(car);
    }

    public boolean updateCar(Car car) throws ClassNotFoundException, SQLException {
        return carDAO.updateCar(car);
    }

    public boolean deleteCar(int carId) throws ClassNotFoundException, SQLException {
        return carDAO.deleteCar(carId);
    }

    public Car getCarById(int carId) throws ClassNotFoundException, SQLException {
        return carDAO.getCarById(carId);
    }

    public List<Car> getAllCars() throws ClassNotFoundException, SQLException {
        return carDAO.getAllCars();
    }
    
    public int getTotalCarsCount() throws ClassNotFoundException, SQLException {
        List<Car> cars = getAllCars();
        return cars.size();
    }
    
    public List<Car> searchCars(String keyword) throws ClassNotFoundException, SQLException {
        return carDAO.searchCars(keyword);
    }
    
    // Add getAvailableCars method
    public List<Car> getAvailableCars() throws ClassNotFoundException, SQLException {
        return carDAO.getAvailableCars();
    }
    
}
