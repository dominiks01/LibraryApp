package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.entities.employee.AccessLevel;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.services.BookService;
import agh.edu.pl.weedesign.library.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NewEmployeeModel {

    private EmployeeService employeeService;
    private final int MIN_PASSWORD_LEN = 5;

    private String name;
    private String surname;
    private String salary;
    private String email;
    private String password;

    private Employee supervisor;
    private AccessLevel permissions;

    @Autowired
    public NewEmployeeModel(EmployeeService employeeService){
        this.employeeService = employeeService;
    };

    public void register(){
        Employee newEmployee = new Employee(
                name, surname, Integer.parseInt(salary),
                email, password, permissions);

        employeeService.addNewEmployee(newEmployee);
    }

    public List<Employee> getEmployees(){
        return this.employeeService.getEmployees();
    }

    public void setName(String name) {
        if (Objects.equals(name, ""))
            throw new IllegalArgumentException("Empty Name");

        if (name.length() > 255)
            throw new IllegalArgumentException("Name too long");

        this.name = name;
    }

    public void setSurname(String surname) {
        if (Objects.equals(surname, ""))
            throw new IllegalArgumentException("Empty Name");

        if (surname.length() > 255)
            throw new IllegalArgumentException("Surname too long");

        this.surname = surname;
    }

    public void setSalary(String salary) {
        if (Objects.equals(salary, ""))
            throw new IllegalArgumentException("Empty salary");

        this.salary = salary;
    }

    public void setEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.find())
            throw new IllegalArgumentException("Wrong email format");


        if (!employeeService.isEmailFree(email))
            throw new IllegalArgumentException("Email already used");

        this.email = email;
    }

    public void setPassword(String password) {
        if (Objects.equals(password, ""))
            throw new IllegalArgumentException("Empty password");

        if (password.length() < MIN_PASSWORD_LEN)
            throw new IllegalArgumentException("Password too short");

        this.password = password;
    }

    public void setSupervisor(Employee supervisor) {
        if(supervisor == null)
            throw new IllegalArgumentException("Empty supervisor");

        this.supervisor = supervisor;
    }

    public void setPermissions(AccessLevel permissions) {
        if(permissions == null)
            throw new IllegalArgumentException("Empty permissions");

        this.permissions = permissions;
    }
}
