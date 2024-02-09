package agh.edu.pl.weedesign.library.modelTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.reader.ReaderRepository;
import agh.edu.pl.weedesign.library.entities.rental.Rental;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ReaderRepositoryTest {

	@Resource
	private ReaderRepository readerRepository;

	@Test
	public void test_rental_reader_many_to_one_relationship() {
		Reader reader = new Reader("Jan", "Kowalski", "Krakow", "Malopolska", "31-503", "Polska", "example1@email.com", "pass1", "123 456 789", LocalDate.of(1987, 12, 3), "male");

		Rental r1 = new Rental(
				LocalDateTime.of(2023, 11, 14, 13, 30),
				LocalDateTime.of(2023, 12, 2, 9, 17));

		Rental r2 = new Rental(
				LocalDateTime.of(2023, 10, 10, 11, 27),
				LocalDateTime.of(2023, 10, 24, 15, 5));

		Rental r3 = new Rental(
				LocalDateTime.of(2023, 12, 1, 13, 1),
				LocalDateTime.of(2024, 1, 1, 12, 0));

		// Create One-to-Many Relationship
		r1.setReader(reader);
		r2.setReader(reader);
		r3.setReader(reader);
		Set<Rental> rentals = Stream.of(r1, r2, r3).collect(Collectors.toCollection(HashSet::new));

		reader.setRentals(rentals);
		readerRepository.save(reader);

		Reader retrievedReader = readerRepository.findById(reader.getId()).orElse(null);
		assertNotNull(retrievedReader);
		assertEquals("Kowalski", retrievedReader.getSurname());

		Set<Rental> retrievedReaderRentals = retrievedReader.getRentals();
		assertNotNull(retrievedReaderRentals);
		assertEquals(3, retrievedReaderRentals.size());



	}
}
