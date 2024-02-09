package agh.edu.pl.weedesign.library.entities.reservation;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository  extends JpaRepository<Reservation, Integer> {

    Reservation findByBookAndReader(Book book, Reader reader);

    List<Reservation> getReservationByBook(Book book);

    void deleteAllByBook(Book book);
}
