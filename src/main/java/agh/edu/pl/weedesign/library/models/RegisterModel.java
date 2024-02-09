package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.services.ReaderService;
import agh.edu.pl.weedesign.library.helpers.ValidCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Component
public class RegisterModel {
    private ConfigurableApplicationContext springContext;
    private final ReaderService readerService;
    private final RegistrationValidChecker checker;

    @Autowired
    public RegisterModel(ConfigurableApplicationContext springContext, ReaderService readerService){
        this.springContext = springContext;
        this.readerService = readerService;
        this.checker = new RegistrationValidChecker();
    }

    public ValidCheck register(HashMap<String, String> data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            checkerGuard(checker.isRegisterNameValid(data.get("nameField")));
            checkerGuard(checker.isRegisterSurnameValid(data.get("surnameField")));
            checkerGuard(checker.isRegisterCityValid(data.get("cityField")));
            checkerGuard(checker.isRegisterVoivodeshipValid(data.get("voivodeshipField")));
            checkerGuard(checker.isRegisterPostcodeValid(data.get("postcodeField")));
            checkerGuard(checker.isRegisterCountryValid(data.get("countryField")));
            checkerGuard(checker.isRegisterEmailValid(data.get("emailField")));
            checkerGuard(checker.isRegisterPasswordValid(data.get("passwordField")));
            checkerGuard(checker.isPhoneNumberValid(data.get("phoneField")));
            checkerGuard(checker.isRegisterBirthdateValid( LocalDate.parse(data.get("birthDateField"), formatter)));
            checkerGuard(checker.isRegisterSexValid(data.get("sexField")));


        } catch (Exception e ){
            return new ValidCheck(false, e.getMessage());
        };

        addNewReader(
            new Reader(
                data.get("nameField"),
                data.get("surnameField"),
                data.get("cityField"),
                data.get("voivodeshipField"),
                data.get("postcodeField"),
                data.get("countryField"),
                data.get("emailField"),
                data.get("passwordField"),
                data.get("phoneField"),
                LocalDate.parse(data.get("birthDateField"), formatter),
                data.get("sexField")
            )
        );

        return new ValidCheck(true, "");
    };

    private void checkerGuard(ValidCheck check){
        if (!check.isValid()){
            throw new IllegalArgumentException(check.message());
        }
    }

    @GetMapping("/readers")
    public List<Reader> getAllReaders() {
        return readerService.getAllReaders();
    }

    @GetMapping("/readers/{id}")
    public Reader getReaderById(@PathVariable Long id) {
        return readerService.getReaderById(id);
    }

    @PostMapping
    public Reader addNewReader(@RequestBody Reader newReader) {
        return readerService.addNewReader(newReader);
    }
}
