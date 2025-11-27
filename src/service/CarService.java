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
}
