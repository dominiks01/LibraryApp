package agh.edu.pl.weedesign.library.entities.category;

import agh.edu.pl.weedesign.library.entities.reader.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
    @Query("SELECT c FROM Book b JOIN  b.category c JOIN b.bookCopies bc JOIN bc.rentals r GROUP BY c ORDER BY COUNT(r) DESC")
    List<Category> findCategoriesSortedByPopularity();
    @Query("SELECT c FROM Book b JOIN  b.category c JOIN b.bookCopies bc JOIN bc.rentals rent JOIN rent.reader r WHERE r = :r GROUP BY c ORDER BY COUNT(rent) DESC")
    List<Category> findCategoryWithMostRentals(Reader r);
}
