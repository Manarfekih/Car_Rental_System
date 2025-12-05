package dao_interfaces;

import model.Employee;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDAO {
    int addEmployee(Employee employee) throws SQLException, ClassNotFoundException;
    boolean updateEmployee(Employee employee) throws SQLException, ClassNotFoundException;
    boolean deleteEmployee(int userId) throws SQLException, ClassNotFoundException;
    List<Employee> getAllEmployees() throws SQLException, ClassNotFoundException;
    List<Employee> searchEmployees(String keyword) throws SQLException, ClassNotFoundException;
}
