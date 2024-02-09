package agh.edu.pl.weedesign.library.entities.employee;

import javax.persistence.*;

import agh.edu.pl.weedesign.library.entities.rental.Rental;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {
    @Id @GeneratedValue
    private int id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private int salary;

    @ManyToOne
    @JoinColumn
    private Employee reports_to;
    private AccessLevel accessLevel;

    public Employee(){};
    public Employee(String name, String surname, int salary, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.email = email;
        this.password = password;
        this.accessLevel = AccessLevel.NONE;
    }
    public Employee(String name, String surname, int salary, String email, String password, AccessLevel accessLevel) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.email = email;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public Employee(String name, String surname, int salary){
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.accessLevel = AccessLevel.NONE;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Employee getReports_to() {
        return reports_to;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setSupervisor(Employee employee){
        this.reports_to = employee;
    }

    public Employee getSupervisor(){
        return reports_to;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String toString(){
        return this.name + " " + this.surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
