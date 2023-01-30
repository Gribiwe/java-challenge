package jp.co.axa.apidemo.controller;

import jp.co.axa.apidemo.entity.Employee;
import jp.co.axa.apidemo.repository.EmployeeRepository;
import jp.co.axa.apidemo.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

    public static final Employee FIRST_EMPLOYEE = new Employee(null, "Test", 10, "Good Company");
    public static final Employee SECOND_EMPLOYEE = new Employee(null, "Test2", 100, "Good Company2");
    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private EmployeeService service;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void beforeEach() {
        repository.deleteAll();
    }

    @Test
    public void saveEmployeeTest() {
        //execution
        ResponseEntity<Employee> response = this.testRestTemplate
                .exchange("/api/v1/employees", HttpMethod.POST, new HttpEntity<>(FIRST_EMPLOYEE), Employee.class);

        assertThat(response.getStatusCodeValue())
                .isEqualTo(200);

        Employee savedEmployee = response.getBody();

        //assert employee really saved
        Long savedEmployeeId = savedEmployee.getId();
        assertThat(savedEmployeeId).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo(FIRST_EMPLOYEE.getName());
        assertThat(savedEmployee.getSalary()).isEqualTo(FIRST_EMPLOYEE.getSalary());
        assertThat(savedEmployee.getDepartment()).isEqualTo(FIRST_EMPLOYEE.getDepartment());

        List<Employee> allEmployee = repository.findAll();
        assertThat(allEmployee.size()).isEqualTo(1);

        Optional<Employee> foundEmployee = service.getEmployee(savedEmployeeId);
        assertTrue(foundEmployee.isPresent());

        //execution
        response = this.testRestTemplate
                .exchange("/api/v1/employees", HttpMethod.POST, new HttpEntity<>(SECOND_EMPLOYEE), Employee.class);

        assertThat(response.getStatusCodeValue())
                .isEqualTo(200);

        savedEmployee = response.getBody();

        //assert employee really saved
        savedEmployeeId = savedEmployee.getId();
        assertThat(savedEmployeeId).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo(SECOND_EMPLOYEE.getName());
        assertThat(savedEmployee.getSalary()).isEqualTo(SECOND_EMPLOYEE.getSalary());
        assertThat(savedEmployee.getDepartment()).isEqualTo(SECOND_EMPLOYEE.getDepartment());

        //assert total number of employee in DB has increased
        allEmployee = repository.findAll();
        assertThat(allEmployee.size()).isEqualTo(2);
    }

    @Test
    public void updateEmployeeEmployeeTest() {
        //preparing second employee to update
        Employee firstEmployeeToUpdate = repository.save(FIRST_EMPLOYEE);
        firstEmployeeToUpdate.setDepartment("Test Department");
        firstEmployeeToUpdate.setName("New Name 123");
        firstEmployeeToUpdate.setSalary(12333);

        //execution
        ResponseEntity<Employee> response = this.testRestTemplate
                .exchange("/api/v1/employees", HttpMethod.PUT, new HttpEntity<>(firstEmployeeToUpdate), Employee.class);

        assertThat(response.getStatusCodeValue())
                .isEqualTo(200);

        //assert first updated employee is really updated
        Employee updatedEmployee = response.getBody();
        assertThat(updatedEmployee.getId()).isEqualTo(firstEmployeeToUpdate.getId());
        assertThat(updatedEmployee.getName()).isEqualTo(firstEmployeeToUpdate.getName());
        assertThat(updatedEmployee.getDepartment()).isEqualTo(firstEmployeeToUpdate.getDepartment());
        assertThat(updatedEmployee.getSalary()).isEqualTo(firstEmployeeToUpdate.getSalary());

        //preparing second employee to update
        Employee secondEmployeeToUpdate = repository.save(SECOND_EMPLOYEE);
        secondEmployeeToUpdate.setDepartment("Test Department");
        secondEmployeeToUpdate.setName("New Name 123");
        secondEmployeeToUpdate.setSalary(12333);

        //execution
        response = this.testRestTemplate
                .exchange("/api/v1/employees", HttpMethod.PUT, new HttpEntity<>(secondEmployeeToUpdate), Employee.class);

        assertThat(response.getStatusCodeValue())
                .isEqualTo(200);

        //assert second updated employee is really updated
        updatedEmployee = response.getBody();
        assertThat(updatedEmployee.getId()).isEqualTo(secondEmployeeToUpdate.getId());
        assertThat(updatedEmployee.getName()).isEqualTo(secondEmployeeToUpdate.getName());
        assertThat(updatedEmployee.getDepartment()).isEqualTo(secondEmployeeToUpdate.getDepartment());
        assertThat(updatedEmployee.getSalary()).isEqualTo(secondEmployeeToUpdate.getSalary());

        //assert first updated employee is not overridden
        Optional<Employee> employee = service.getEmployee(firstEmployeeToUpdate.getId());
        assertTrue(employee.isPresent());

        Employee employeeValue = employee.get();
        assertThat(employeeValue.getId()).isEqualTo(firstEmployeeToUpdate.getId());
        assertThat(employeeValue.getName()).isEqualTo(firstEmployeeToUpdate.getName());
        assertThat(employeeValue.getDepartment()).isEqualTo(firstEmployeeToUpdate.getDepartment());
        assertThat(employeeValue.getSalary()).isEqualTo(firstEmployeeToUpdate.getSalary());
    }

    @Test
    public void getEmployeeTest() {
        //preparation
        Employee firstSavedEmployee = repository.save(FIRST_EMPLOYEE);
        Employee secondSavedEmployee = repository.save(SECOND_EMPLOYEE);

        //execution
        Long firstId = firstSavedEmployee.getId();
        ResponseEntity<Employee> response = this.testRestTemplate
                .getForEntity("/api/v1/employees/" + firstId, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        Employee foundEmployee = response.getBody();

        assertThat(foundEmployee.getId()).isEqualTo(firstId);
        assertThat(foundEmployee.getName()).isEqualTo(firstSavedEmployee.getName());
        assertThat(foundEmployee.getSalary()).isEqualTo(firstSavedEmployee.getSalary());
        assertThat(foundEmployee.getDepartment()).isEqualTo(firstSavedEmployee.getDepartment());

        //execution
        Long secondId = secondSavedEmployee.getId();
        response = this.testRestTemplate
                .getForEntity("/api/v1/employees/" + secondId, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        foundEmployee = response.getBody();

        assertThat(foundEmployee.getId()).isEqualTo(secondId);
        assertThat(foundEmployee.getName()).isEqualTo(secondSavedEmployee.getName());
        assertThat(foundEmployee.getSalary()).isEqualTo(secondSavedEmployee.getSalary());
        assertThat(foundEmployee.getDepartment()).isEqualTo(secondSavedEmployee.getDepartment());
    }

    @Test
    public void getEmployeeListTest() {
        //preparation
        repository.save(FIRST_EMPLOYEE);
        repository.save(SECOND_EMPLOYEE);

        //execution
        ResponseEntity<List<Employee>> response = this.testRestTemplate
                .getForEntity("/api/v1/employees", (Class<List<Employee>>) (Object) List.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        List<Employee> employeeList = response.getBody();

        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void deleteEmployeeTest() {
        //preparation
        Employee firstSavedEmployee = repository.save(FIRST_EMPLOYEE);
        Employee secondSavedEmployee = repository.save(SECOND_EMPLOYEE);

        //execution
        Long firstId = firstSavedEmployee.getId();
        this.testRestTemplate.delete("/api/v1/employees/" + firstId);

        //assert deleted 1 employee and deleted employee can't be found
        List<Employee> allEmployee = repository.findAll();
        assertThat(allEmployee.size()).isEqualTo(1);

        Optional<Employee> foundEmployee = repository.findById(firstId);
        assertFalse(foundEmployee.isPresent());

        //execution second employee
        Long secondId = secondSavedEmployee.getId();
        this.testRestTemplate.delete("/api/v1/employees/" + secondId);

        //assert deleted 1 employee and deleted employee can't be found
        allEmployee = repository.findAll();
        assertThat(allEmployee.size()).isEqualTo(0);

        foundEmployee = repository.findById(secondId);
        assertFalse(foundEmployee.isPresent());
    }
}
