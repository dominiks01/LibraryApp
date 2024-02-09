package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.employee.EmployeeRepository;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.reader.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    public void addNewEmployee(Employee e){
        employeeRepository.save(e);
    }

    public Employee getEmployeeByEmail(String email){
        return employeeRepository.findByEmail(email);
    }

    public boolean isEmailFree(String email){
        return employeeRepository.findByEmail(email) == null;
    }

    public String getEmployeePasswordByEmail(String email){
        if (this.employeeRepository.findByEmail(email) == null){
            return null;
        }
        return this.employeeRepository.findByEmail(email).getPassword();
    }



}
