package agh.edu.pl.weedesign.library.entities.book;

import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b JOIN b.category c JOIN b.bookCopies bc JOIN bc.rentals rent JOIN rent.reader r WHERE c = :category AND r <> :reader GROUP BY b")
    List<Book> findNotReadBooksFromCategory(Reader reader, Category category);
    @Query("SELECT b FROM Book b JOIN b.category c JOIN b.bookCopies bc JOIN bc.rentals r WHERE c = :category GROUP BY b.id ORDER BY COUNT(r) DESC")
    List<Book> findBooksFromCategoryOrderedByRentalCountDesc(Category category);

    @Query("SELECT b FROM Book b JOIN b.category c JOIN b.bookCopies bc JOIN bc.rentals rent JOIN rent.reader r WHERE c = :category AND r <> :reader GROUP BY b.id ORDER BY COUNT(rent.id) DESC")
    List<Book> findNotReadBooksFromCategoryOrderedByRentalCountDesc(Reader reader, Category category);

    @Query("SELECT b FROM Book b JOIN b.bookCopies bc JOIN bc.rentals rent GROUP BY b.id, bc.id ORDER BY COUNT(rent.id) DESC")
    List<Book> findBooksOrderedByRentalCountDesc();

    List<Book> findBookByCategory(Category category);

    @Query("SELECT b FROM Book b JOIN Author a JOIN Category c WHERE a.surname LIKE :authorSurname OR c.name LIKE :categoryName")

    List<Book> getAllByAuthorSurnameOrCategoryName(String authorSurname, String categoryName);
    @Query("SELECT b FROM Book b JOIN BookCopy bc JOIN Rental rent JOIN Reader r WHERE r = :r GROUP BY b ")
    List<Book> findBooksRentedByReader(Reader r);
//    List<Book> findAllByAuthorSurname(String authorSurname, String categoryName);

    @Query("SELECT b FROM Book b WHERE (:category is null OR b.category.name = :category)")
    Page<Book> findBooksByCategoryAndSort(
            @Param("category") String category,
            Pageable pageable
    );

}
