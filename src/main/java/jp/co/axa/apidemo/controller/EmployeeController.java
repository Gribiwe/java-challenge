package jp.co.axa.apidemo.controller;

import jp.co.axa.apidemo.entity.Employee;
import jp.co.axa.apidemo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

/**
 * Controller class for the processing the REST requests related to Employee object
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Create a new employee endpoint.
     *
     * @param employee employee object to save.
     * @return saved employee object.
     */
    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        log.info("Employee Saved Successfully: {}", savedEmployee);
        return ResponseEntity.ok(savedEmployee);
    }

    /**
     * Update an employee endpoint.
     *
     * @param employee employee reference to update.
     * @return updated employee value.
     */
    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        if (isNull(employee) && isNull(employee.getId())) {
            log.error("User {} is null or has the null id", employee);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    /**
     * Get an employee with the specified id endpoint.
     *
     * @param employeeId id of employee to get.
     * @return found employee, or 404 error if not found.
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        Optional<Employee> employee = employeeService.getEmployee(employeeId);
        return ResponseEntity.of(employee);
    }

    /**
     * Endpoint for getting the information about all the Employee.
     *
     * @return employee list.
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getEmployeeList() {
        List<Employee> employees = employeeService.getEmployeeList();
        return ResponseEntity.ok(employees);
    }

    /**
     * Delete an employee endpoint.
     *
     * @param employeeId id of employee to delete.
     */
    @DeleteMapping("/{employeeId}")
    public void deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        log.info("Employee (id={}) Deleted Successfully", employeeId);
    }
}
