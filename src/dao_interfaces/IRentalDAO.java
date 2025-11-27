package dao_interfaces;
import model.Rental;

import java.sql.SQLException;
import java.util.List;

public interface IRentalDAO {

	
	boolean createRental(Rental rental) throws SQLException, ClassNotFoundException;
    boolean returnCar(int rentalId) throws SQLException, ClassNotFoundException;
    List<Rental> getAllRentals() throws SQLException, ClassNotFoundException;
    Rental getRentalById(int rentalId) throws SQLException, ClassNotFoundException;
    
}
