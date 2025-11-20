package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import database.DatabaseManager;
import model.Rental;

public class RentalService {
	
	
	//rent a car
	public boolean createRental(Rental rental) {
		String sql="insert into rentals (client_id,car_id,start_date,end_date,total_price,status)values(?,?,?,?,?,?)";
		try(Connection conn=DatabaseManager.getConnection();
				PreparedStatement stmt=conn.prepareStatement(sql)){
					stmt.setInt(1, rental.getClientId());
		            stmt.setInt(2, rental.getCarId());
		            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
		            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
		            stmt.setDouble(5, rental.getTotalPrice());
		            stmt.setString(6, rental.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	
	//return a car-->update rental+status of the car
	public boolean returncar(int rentalid) {
		String sql="update rentals r join cars c on c.car_id=r.car_id set r.status='completed',c.status='available' where r.rental_id=?";
		try(Connection conn=DatabaseManager.getConnection();
			PreparedStatement stmt=conn.prepareStatement(sql)){
			
			stmt.setInt(1,rentalid);
			return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	
	//get all rentals
	
	public List<Rental>rentals=new ArrayList<>() {
		List<Rental>rentals=new ArrayList<>();
		String sql="select * from rentals";
		try(Connection conn=DatabaseManager.getConnection();
				Statement stmt=conn.createStatement();
				ResultSet rs=stmt.executeQuery(sql)){
			
			
			while(rs.next()) {
				rentals.add(new Rental(
						 rs.getInt("rental_id"),
		                    rs.getInt("client_id"),
		                    rs.getInt("car_id"),
		                    rs.getDate("start_date").toLocalDate(),
		                    rs.getDate("end_date").toLocalDate(),
		                    rs.getDouble("total_price"),
		                    rs.getString("status")
						));
			}
		}catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
	}
	
	

}
