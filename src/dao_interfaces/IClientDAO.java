package dao_interfaces;

import java.sql.SQLException;
import java.util.List;

import model.Client;

public interface IClientDAO {

    boolean addClient(Client client) throws SQLException, ClassNotFoundException;

    boolean updateClient(Client client) throws SQLException, ClassNotFoundException;

    boolean deleteClient(int clientId) throws SQLException, ClassNotFoundException;

    Client getClientById(int clientId) throws SQLException, ClassNotFoundException;

    List<Client> getAllClients() throws SQLException, ClassNotFoundException;
    
    
    
    Client getClientByUserId(int userId) throws SQLException, ClassNotFoundException;
    Client getClientByEmail(String email) throws SQLException, ClassNotFoundException;
}
