package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseManager;
import model.Client;

public class ClientService {

	
	public boolean addClient(Client client) {
		String sql="insert into clients(user_id,name,email,phone,license_number, address)values (?, ?, ?, ?, ?, ?)";
		
		try(Connection conn=DatabaseManager.getConnection();
				PreparedStatement stmt=conn.prepareStatement(sql)){
			
			stmt.setInt(1, client.getUserId());
			stmt.setString(2,client.getName());
			stmt.setString(3,client.getEmail());
			stmt.setString(4,client.getPhone());
			stmt.setString(5,client.getLicenseNumber());
			stmt.setString(6,client.getAddress());
			
			return stmt.executeUpdate()>0;
		}catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
		
	}
	
	//get client by id 
	public Client getclientbyid(int clientid) {
		String sql="select * from clients where client_id=?";
		 try (Connection conn = DatabaseManager.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, clientid);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return new Client(
	                    rs.getInt("client_id"),
	                    rs.getInt("user_id"),
	                    rs.getString("name"),
	                    rs.getString("email"),
	                    rs.getString("phone"),
	                    rs.getString("license_number"),
	                    rs.getString("address")
	                );
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;	
	}
	
	public List<Client> getallclients(){
		List<Client>clients=new ArrayList<>();
		String sql="select * from clients";
		try(Connection conn = DatabaseManager.getConnection();
				Statement stmt=conn.createStatement();
				ResultSet rs=stmt.executeQuery(sql)){
			
			
			while(rs.next()) {
				clients.add(new Client(
						rs.getInt("client_id"),
						rs.getInt("user_id"),
						rs.getString("name"),
						rs.getString("email"),
	                    rs.getString("phone"),
	                    rs.getString("license_number"),
	                    rs.getString("address")
						));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return clients;
	}
	
	
}
