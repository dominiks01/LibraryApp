package agh.edu.pl.weedesign.library.entities.author;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByNameAndSurname(String name, String surname);
}
