package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.helpers.Encryptor;
import agh.edu.pl.weedesign.library.helpers.ValidCheck;
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
    private Encryptor passwordEncryptor;

    @Autowired
    public LoginModel(ConfigurableApplicationContext springContext, ReaderService userService, EmployeeService employeeService){
        this.springContext = springContext;
        this.readerService = userService;
        this.employeeService = employeeService;
    }

    public ValidCheck login(String login, String userPassword, Boolean superUser){
        try {
            if(!superUser)
                return new ValidCheck(false, "");

            String encryptedRealPassword = employeeService.getEmployeePasswordByEmail(login);

            if (Objects.equals(userPassword, encryptedRealPassword) ) {
                System.out.println("Access granted");
                LibraryApplication.setEmployee(this.employeeService.getEmployeeByEmail(login));
            }

        } catch (Exception e ){
            return new ValidCheck(false, e.getMessage());
        };

        return new ValidCheck(true, "");
    }

    public ValidCheck login(String login, String userPassword){
        try {
            String encryptedRealPassword = readerService.getReaderPasswordByEmail(login);

            if (Objects.equals(userPassword, encryptedRealPassword) ) {
                System.out.println("Access granted");
                LibraryApplication.setReader(this.readerService.findByEmail(login));
            }

        } catch (Exception e ){
            return new ValidCheck(false, e.getMessage());
        };

        return new ValidCheck(true, "");
    };

    private void checkerGuard(ValidCheck check){
        if (!check.isValid()){
            throw new IllegalArgumentException(check.message());
        }
    }

}
