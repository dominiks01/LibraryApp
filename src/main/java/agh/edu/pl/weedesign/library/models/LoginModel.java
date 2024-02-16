package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.helpers.Encryptor;
import agh.edu.pl.weedesign.library.helpers.ValidCheck;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.EmployeeService;
import agh.edu.pl.weedesign.library.services.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoginModel {
    private ConfigurableApplicationContext springContext;
    private final ReaderService readerService;
    private final EmployeeService employeeService;
    private DataService dataService;


    @Autowired
    public LoginModel(ConfigurableApplicationContext springContext, ReaderService userService, EmployeeService employeeService){
        this.springContext = springContext;
        this.readerService = userService;
        this.employeeService = employeeService;
    }

    public void setDataService(DataService dataService) {
        System.out.println("XD " + dataService);
        this.dataService = dataService;
    }

    public ValidCheck login(String login, String userPassword, Boolean superUser){
        try {
            if(!superUser)
                return new ValidCheck(false, "");

            String encryptedRealPassword = employeeService.getEmployeePasswordByEmail(login);

            if(encryptedRealPassword == null || userPassword == null)
                return new ValidCheck(false, "");

            if (employeeService.getEmployeeByEmail(login) == null){
                throw new IllegalArgumentException("User not found!");
            }

            if (userPassword.equals(encryptedRealPassword )) {
                dataService.setEmployee(employeeService.getEmployeeByEmail(login));
                System.out.println("Access granted");
                return new ValidCheck(true, "");
            }

        } catch (Exception e ){
            return new ValidCheck(false, e.getMessage());
        };

        return new ValidCheck(false, "");
    }

    public ValidCheck login(String login, String userPassword){
        try {
            String encryptedRealPassword = readerService.getReaderPasswordByEmail(login);

            if(encryptedRealPassword == null || userPassword == null)
                return new ValidCheck(false, "");

            if (Objects.equals(userPassword, encryptedRealPassword) ) {
                System.out.println("Access granted");
                return new ValidCheck(true, "");
            }

        } catch (Exception e ){
            return new ValidCheck(false, e.getMessage());
        };

        return new ValidCheck(false, "");
    };
}
