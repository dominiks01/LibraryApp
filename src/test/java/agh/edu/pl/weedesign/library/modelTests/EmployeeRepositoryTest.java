package agh.edu.pl.weedesign.library.modelTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.employee.EmployeeRepository;

@SpringBootTest
class EmployeeRepositoryTest {

	@Resource
	private EmployeeRepository employeeRepository; 

	@Test
	public void givenEmployee_whenSave_thenGetOk() {
		Employee employee = new Employee("Dominik", "S", 1000, "", "");
		Employee savedEmployee =  employeeRepository.save(employee);

		Employee retrievedEmployee = employeeRepository.findById(savedEmployee.getId()).orElse(null);
		assertNotNull(retrievedEmployee);
		assertEquals("Dominik", retrievedEmployee.getName());
		assertEquals("S", retrievedEmployee.getSurname());
		assertEquals(1000, retrievedEmployee.getSalary());
	}

}
