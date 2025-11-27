package service;

import dao_interfaces.IRentalDAO;

import dao.RentalDAO;
import model.Rental;

import java.sql.SQLException;
import java.util.List;

public class RentalService {

    private final IRentalDAO rentalDAO;

    public RentalService() {
        this.rentalDAO = new RentalDAO();
    }

    public boolean createRental(Rental rental) throws ClassNotFoundException, SQLException {
        return rentalDAO.createRental(rental);
    }

    public boolean returnCar(int rentalId) throws ClassNotFoundException, SQLException {
        return rentalDAO.returnCar(rentalId);
    }

    public Rental getRentalById(int rentalId) throws ClassNotFoundException, SQLException {
        return rentalDAO.getRentalById(rentalId);
    }

    public List<Rental> getAllRentals() throws ClassNotFoundException, SQLException {
        return rentalDAO.getAllRentals();
    }
}
