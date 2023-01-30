package jp.co.axa.apidemo.service;

import jp.co.axa.apidemo.entity.Employee;
import jp.co.axa.apidemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EmployeeService interface which allows to process the Employee objects.
 * and assume CRUD operations using the EmployeeRepository.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Employee> getEmployeeList() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Employee> getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}