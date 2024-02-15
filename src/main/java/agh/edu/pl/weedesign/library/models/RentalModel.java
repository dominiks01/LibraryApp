package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.services.ReaderService;
import agh.edu.pl.weedesign.library.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Component
public class RentalModel {

    private ConfigurableApplicationContext spring_context;
    private final ReaderService readerService;

    private final RentalService rentalService;

    @Autowired
    public RentalModel(ConfigurableApplicationContext spring_context, ReaderService readerService, RentalService rentalService){
        this.spring_context = spring_context;
        this.readerService = readerService;
        this.rentalService = rentalService;
    }

    @PostMapping
    public void rentBook(@RequestBody BookCopy bookCopy){
        Rental rental = new Rental(LocalDateTime.now());

        rental.setBookCopy(bookCopy);
//        rental.setReader(LibraryApplication.getReader());

        rentalService.addNewRental(rental);
    }
}
