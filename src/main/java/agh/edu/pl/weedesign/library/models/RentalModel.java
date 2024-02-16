package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.reservation.Reservation;
import agh.edu.pl.weedesign.library.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RentalModel {

    private final RentalService rentalService;
    private final ReservationService reservationService;
    private DataService dataService;

    @Autowired
    public RentalModel( ReservationService reservationService, RentalService rentalService){
        this.rentalService = rentalService;
        this.reservationService = reservationService;
    }

    public List<Rental> getRentalsWithoutAcceptance(){
        return rentalService.getRentalsWithoutAcceptance();
    }

    public void setDataService(DataService dataService){
        this.dataService = dataService;
    }

    public List<Rental> getReaderRentals(Reader r){
        return this.rentalService.getRentalsByReader(r);
    }

    public void finishRental(Rental r){
        if(r.getEmployee() == null)
            this.rentalService.removeRental(r);
        else {
            r.setEndDate(LocalDateTime.now());
            this.rentalService.updateRental(r);
            informAboutReturnedBook(r);
        }
    }

    private void informAboutReturnedBook(Rental rental){
        List<Reservation> reservations = this.reservationService.getReservationsByBook(rental.getBookCopy().getBook());
        this.reservationService.deleteAllReservationsByBook(reservations);

        if (reservations == null) return;
        for (Reservation r: reservations){
            EmailService emailService = new EmailService();
            emailService.sendSimpleMessage(
                    r.getReader().getEmail(),
                    "Dostępna książka",
                    "Książka: \"" + r.getBook().getTitle() + "\" jest już dostępna w bibliotece!"
            );
        }
    }

    @PostMapping
    public void rentBook(@RequestBody BookCopy bookCopy){
        Rental rental = new Rental(LocalDateTime.now());
        rental.setBookCopy(bookCopy);

        rentalService.addNewRental(rental);
    }
}
