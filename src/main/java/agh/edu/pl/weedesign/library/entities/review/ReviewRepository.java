package agh.edu.pl.weedesign.library.entities.review;

import agh.edu.pl.weedesign.library.entities.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r JOIN r.rental re JOIN re.bookCopy bc JOIN bc.book b WHERE b.id = :bookID")
    List<Review> findReviewsOfBook(int bookID);

    @Query("SELECT AVG(r.stars) FROM Review r JOIN r.rental re JOIN re.bookCopy bc JOIN bc.book b WHERE b = :book")
    Double findAverageRating(Book book);
}
