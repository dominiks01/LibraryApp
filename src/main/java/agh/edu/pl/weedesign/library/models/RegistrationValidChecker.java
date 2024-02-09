package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.helpers.ValidCheck;
import agh.edu.pl.weedesign.library.services.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationValidChecker {
    private int AGE_RESTRICTION = 0;
    private int MIN_PASSWORD_LEN = 5;

    @Autowired
    private ReaderService service;

    public ValidCheck isRegisterNameValid(String name){
        if (Objects.equals(name, ""))
            return new ValidCheck(false, "Name is empty");

        if (name.length() > 255)
            return new ValidCheck(false, "Name too long. Should be less than 255");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterSurnameValid(String surname){
        if (Objects.equals(surname, ""))
            return new ValidCheck(false, "Surname is empty");

        if (surname.length() > 255)
            return new ValidCheck(false, "Surname too long. Should be less than 255");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterVoivodeshipValid(String voivodeship){
        if (Objects.equals(voivodeship, ""))
            return new ValidCheck(false, "Voivodeship is empty");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterPostcodeValid(String postcode){
        if (Objects.equals(postcode, ""))
            return new ValidCheck(false, "Postcode is empty");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterCountryValid(String country){
        if (Objects.equals(country, ""))
            return new ValidCheck(false, "Country is empty");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterPasswordValid(String password){
        if (Objects.equals(password, ""))
            return new ValidCheck(false, "Password is empty");

        if (password.length() < MIN_PASSWORD_LEN)
            return new ValidCheck(false, "Password to short");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterSexValid(String sex){
        if (Objects.equals(sex, ""))
            return new ValidCheck(false, "Sex is empty");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterCityValid(String city){
        if (Objects.equals(city, ""))
            return new ValidCheck(false, "City is empty");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isRegisterEmailValid(String email){
        // regex pattern for valid email address
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.find()){
            return new ValidCheck(false, "Wrong email address format");
        }

        if (!service.isEmailFree(email))
            return new ValidCheck(false, "Account with this email already exists");


        return new ValidCheck(true,"ok");
    }

    public ValidCheck isRegisterBirthdateValid(LocalDate birthDate){
        if (birthDate == null)
            return new ValidCheck(false, "Birthdate field is not filled");

        if (birthDate.isAfter(LocalDate.now()))
            return new ValidCheck(false, "Birth date is in the future");

        if (Period.between(birthDate, LocalDate.now()).getYears() < AGE_RESTRICTION)
            return new ValidCheck(false, "You are to young to register");

        return new ValidCheck(true,"ok");
    }

    public ValidCheck isPhoneNumberValid(String phone){
        Pattern pattern = Pattern.compile("^[0-9 ]+$");
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.find())
            return new ValidCheck(false, "Invalid phone number");

        return new ValidCheck(true, "ok");
    }

    public ValidCheck isSalaryValid(String salary){
        if (!salary.matches("\\d+")){
            return new ValidCheck(false, "Salary must be an integer");
        }
        return new ValidCheck(true, "ok");
    }

}
