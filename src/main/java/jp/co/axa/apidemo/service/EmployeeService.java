package jp.co.axa.apidemo.service;

import jp.co.axa.apidemo.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the Employee service to implement.
 *
 * @see Employee
 */
public interface EmployeeService {

    /**
     * Save the employee in the database.
     *
     * @param employee employee object to save.
     * @return the saved employee value.
     */
    public Employee saveEmployee(Employee employee);

    /**
     * Get the list of all the employee.
     *
     * @return list of found employee.
     */
    public List<Employee> getEmployeeList();

    /**
     * Get an employee with specified id.
     *
     * @param employeeId id of employee to find.
     * @return the found employee.
     */
    public Optional<Employee> getEmployee(Long employeeId);

    /**
     * Delete an employee with specified id
     *
     * @param employeeId id of employee to delete
     */
    public void deleteEmployee(Long employeeId);
}