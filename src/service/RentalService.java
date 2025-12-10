package service;

import dao.RentalDAO;
import dao_interfaces.IRentalDAO;
import model.Rental;

import java.sql.SQLException;
import java.util.List;

public class RentalService {

    private final IRentalDAO rentalDAO;

    public RentalService() {
        this.rentalDAO = new RentalDAO();
    }

    // ---------------- ADD RENTAL ----------------
    public boolean addRental(Rental rental) throws SQLException, ClassNotFoundException {
        return rentalDAO.createRental(rental);
    }

    // ---------------- UPDATE RENTAL ----------------
    public boolean updateRental(Rental rental) throws SQLException, ClassNotFoundException {
        return rentalDAO.updateRental(rental);
    }

    // ---------------- DELETE RENTAL ----------------
    public boolean deleteRental(int rentalId) throws SQLException, ClassNotFoundException {
        return rentalDAO.deleteRental(rentalId);
    }

    // ---------------- RETURN CAR ----------------
    public boolean returnCar(int rentalId) throws SQLException, ClassNotFoundException {
        return rentalDAO.returnCar(rentalId);
    }

    // ---------------- GET ALL RENTALS ----------------
    public List<Rental> getAllRentals() throws SQLException, ClassNotFoundException {
        return rentalDAO.getAllRentals();
    }

    // ---------------- GET RENTAL BY ID ----------------
    public Rental getRentalById(int rentalId) throws SQLException, ClassNotFoundException {
        return rentalDAO.getRentalById(rentalId);
    }

    // ---------------- SEARCH RENTALS ----------------
    public List<Rental> searchRental(String keyword) throws SQLException, ClassNotFoundException {
        return rentalDAO.searchRental(keyword);
    }

    // ---------------- CHECK IF RENTAL EXISTS ----------------
    public boolean rentalExists(int rentalId) throws SQLException, ClassNotFoundException {
        return rentalDAO.rentalExists(rentalId);
    }
 // ---------------- ACTIVE RENTALS COUNT ----------------
    public int getActiveRentalsCount() throws SQLException, ClassNotFoundException {
        return rentalDAO.getActiveRentalsCount();
    }

}