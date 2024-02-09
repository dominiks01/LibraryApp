package agh.edu.pl.weedesign.library.entities.reader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Integer> {
    boolean existsByEmail(String email);

    Reader findByEmail(String email);
}
