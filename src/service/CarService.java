package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseManager;
import model.Car;

public class CarService {
	
	
	public boolean addCar(Car car) {
		String sql="insert into cars(car_id,brand,model,year,category,price_per_day,status)Values(?,?,?,?,?,?,?)";
		try(Connection conn=DatabaseManager.getConnection();
				PreparedStatement stmt=conn.prepareStatement(sql)){
				
			   		stmt.setString(1,car.getBrand());
			   		stmt.setString(2,car.getModel());
			   		stmt.setInt(3,car.getYear());
			   		stmt.setString(4, car.getCategory());
		            stmt.setDouble(5, car.getPricePerDay());
		            stmt.setString(6, car.getStatus());
		           
		            
		            return stmt.executeUpdate()>0;	
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		
		  return false;
	}
	
	
	public Car getcarbyid(int carid) {
		String sql="select * from cars where car_id=?";
		try(Connection conn=DatabaseManager.getConnection();
				PreparedStatement stmt=conn.prepareStatement(sql)){
			
			stmt.setInt(1,carid);
			ResultSet rs=stmt.executeQuery();
			if(rs.next()) {
				return new Car(
						rs.getInt("car_id"),
						 rs.getString("brand"),
		                 rs.getString("model"),
		                 rs.getInt("year"),
		                 rs.getString("category"),
		                 rs.getDouble("price_per_day"),
		                 rs.getString("status")
		                 
		            );
						
			}
			
		}catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	
	}
	
	
	public List<Car> getallCars(){
		List<Car>cars=new ArrayList<>();
		String sql="select * from cars";
		try (Connection conn=DatabaseManager.getConnection();
				Statement stmt=conn.createStatement();
				ResultSet rs=stmt.executeQuery(sql)){
			
				while(rs.next()) {
					cars.add(new Car(
							rs.getInt("car_id"),
		                    rs.getString("brand"),
		                    rs.getString("model"),
		                    rs.getInt("year"),
		                    rs.getString("category"),
		                    rs.getDouble("price_per_day"),
		                    rs.getString("status")
							));
				}
			
		}catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
	}

}
