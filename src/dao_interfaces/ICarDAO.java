package dao_interfaces;

import java.sql.SQLException;
import java.util.List;

import model.Car;

public interface ICarDAO {
	
	 boolean addCar(Car car) throws SQLException, ClassNotFoundException;
	    boolean updateCar(Car car) throws SQLException, ClassNotFoundException;
	    boolean deleteCar(int carId) throws SQLException, ClassNotFoundException;
	    Car getCarById(int carId) throws SQLException, ClassNotFoundException;
	    List<Car> getAllCars() throws SQLException, ClassNotFoundException;

}
