package agh.edu.pl.weedesign.library.entities.rental;

import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    @Query("SELECT r FROM Rental r WHERE r.reader = :reader")
    List<Rental> findRentalsByReader(Reader reader);

    List<Rental> findRentalsByBookCopy(BookCopy bookCopy);

    @Query("SELECT r FROM Rental r WHERE r.employee IS NULL")
    List<Rental> findRentalsWithoutAcceptance();

    @Query("SELECT r FROM Rental r WHERE r.end_date IS NULL")
    List<Rental> getActualRentedBooks();


}
