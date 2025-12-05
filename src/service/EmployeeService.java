package service;

import dao.EmployeeDAO;
import dao_interfaces.IEmployeeDAO;
import model.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    private final IEmployeeDAO employeeDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }

    public List<Employee> getAllEmployees() throws SQLException, ClassNotFoundException {
        return employeeDAO.getAllEmployees();
    }

    public int addEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        if (((EmployeeDAO) employeeDAO).isEmailTaken(employee.getEmail())) {
            throw new SQLException("Email already used by another employee.");
        }
        return employeeDAO.addEmployee(employee);
    }

    public boolean updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        if (((EmployeeDAO) employeeDAO).isEmailTakenByOtherEmployee(employee.getEmail(), employee.getUserId())) {
            throw new SQLException("Email already used by another employee.");
        }
        return employeeDAO.updateEmployee(employee);
    }

    public boolean deleteEmployee(int userId) throws SQLException, ClassNotFoundException {
        return employeeDAO.deleteEmployee(userId);
    }

    public List<Employee> searchEmployees(String keyword) throws SQLException, ClassNotFoundException {
        return employeeDAO.searchEmployees(keyword);
    }
}
