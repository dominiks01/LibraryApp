package agh.edu.pl.weedesign.library.modelTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopyRepository;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.employee.EmployeeRepository;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BookCopyRepositoryTest {

	@Resource
	private BookCopyRepository bookCopyRepository;

	@Test
	public void givenBookCopy_whenSave_thenGetOk() {
		BookCopy bookCopy = new BookCopy(10, "Great Condition");
		bookCopyRepository.save(bookCopy);

		BookCopy retrievedBook = bookCopyRepository.findById(bookCopy.getId()).orElse(null);
		assertNotNull(retrievedBook);

		assertEquals("Great Condition", retrievedBook.getCondition());
		assertEquals(10, retrievedBook.getWeek_unit_price());
	}
}
