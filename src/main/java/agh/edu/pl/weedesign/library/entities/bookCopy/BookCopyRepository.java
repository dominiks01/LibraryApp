package agh.edu.pl.weedesign.library.entities.bookCopy;

import java.util.List;

import agh.edu.pl.weedesign.library.entities.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
    @Query("SELECT b, COUNT(bc) FROM Book b JOIN b.bookCopies bc WHERE b IN :books  GROUP BY b")
    List<Object[]> findBooksWithCopyCount(@Param("books") List<Book> books);

    List<BookCopy> findBookCopiesByBook(Book book);
}
