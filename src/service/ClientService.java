package service;

import dao.ClientDAO;
import dao_interfaces.IClientDAO;
import model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    private final IClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public boolean addClient(Client client) throws ClassNotFoundException, SQLException {
        return clientDAO.addClient(client);
    }

    public boolean updateClient(Client client) throws ClassNotFoundException, SQLException {
        return clientDAO.updateClient(client);
    }

    public boolean deleteClient(int clientId) throws ClassNotFoundException, SQLException {
        return clientDAO.deleteClient(clientId);
    }

    public Client getClientById(int clientId) throws ClassNotFoundException, SQLException {
        return clientDAO.getClientById(clientId);
    }

    public List<Client> getAllClients() throws ClassNotFoundException, SQLException {
        return clientDAO.getAllClients();
    }
}
