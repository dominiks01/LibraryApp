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
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegisterModel {
    private final ReaderService readerService;
    private final int AGE_RESTRICTION = 0;
    private final int MIN_PASSWORD_LEN = 5;

    private String name;
    private String surname;
    private String voivodeship;
    private String postcode;
    private String country;
    private String password;
    private String sex;
    private String city;
    private String email;
    private LocalDate birthDate;
    private String phone;
    private String salary;

    @Autowired
    public RegisterModel(ReaderService readerService){
        this.readerService = readerService;
    }

    public ValidCheck register(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            addNewReader(
                    new Reader(
                            this.name, this.surname, this.city, this.voivodeship,
                            this.postcode, this.country, this.email, this.password, this.phone,
                            this.birthDate, this.sex)
            );

            return new ValidCheck(true, "");
        } catch (Exception e ){
            return new ValidCheck(false, e.getMessage());
        }
    };


    public void setName(String name){
        if (Objects.equals(name, ""))
            throw new IllegalArgumentException("Empty Name");

        if (name.length() > 255)
            throw new IllegalArgumentException("Name too long");

        this.name = name;
    }


    public void setSurname(String surname){
        if (Objects.equals(surname, ""))
            throw new IllegalArgumentException("Empty Name");

        if (surname.length() > 255)
            throw new IllegalArgumentException("Surname too long");

        this.surname = surname;
    }

    public void setVoivodeship(String voivodeship){
        if (Objects.equals(voivodeship, ""))
            throw new IllegalArgumentException("Empty voivodeship");

        this.voivodeship = voivodeship;
    }

    public void setPostcode(String postcode){
        if (Objects.equals(postcode, ""))
            throw new IllegalArgumentException("Empty postcode");

        this.postcode = postcode;
    }

    public void setCountry(String country){
        if (Objects.equals(country, ""))
            throw new IllegalArgumentException("Empty country");

        this.country = country;
    }

    public void setPassword(String password){
        if (Objects.equals(password, ""))
            throw new IllegalArgumentException("Empty password");

        if (password.length() < MIN_PASSWORD_LEN)
            throw new IllegalArgumentException("Password too short");

        this.password = password;
    }

    public void setSex(String sex){
        if (Objects.equals(sex, ""))
            throw new IllegalArgumentException("Empty Sex");

        this.sex = sex;
    }

    public void setCity(String city){
        if (Objects.equals(city, ""))
            throw new IllegalArgumentException("Empty City");

        this.city = city;
    }

    public void setEmail(String email){

        // regex pattern for valid email address
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.find())
            throw new IllegalArgumentException("Wrong email format");


        if (!readerService.isEmailFree(email))
            throw new IllegalArgumentException("Email already used");

        this.email = email;
    }

    public void setBirthDate(LocalDate birthDate){
        if (birthDate == null)
            throw new IllegalArgumentException("Empty birthDate");

        if (birthDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Wrong birthDate");

        if (Period.between(birthDate, LocalDate.now()).getYears() < AGE_RESTRICTION)
            throw new IllegalArgumentException("Too young to register");

        this.birthDate = birthDate;
    }

    public void setPhone(String phone) {
        Pattern pattern = Pattern.compile("^[0-9 ]+$");
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.find())
            throw new IllegalArgumentException("Wrong Phone format");

        this.phone = phone;
    }


    @PostMapping
    public Reader addNewReader(@RequestBody Reader newReader) {
        return readerService.addNewReader(newReader);
    }
}
