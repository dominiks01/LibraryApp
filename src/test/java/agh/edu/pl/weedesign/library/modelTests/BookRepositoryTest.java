package agh.edu.pl.weedesign.library.modelTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.book.BookRepository;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;


@DataJpaTest
public class BookRepositoryTest {

    @Resource
	private BookRepository bookRepository; 

	@Test
    public void test_book_bookCopy_many_to_one_relationship() {
		Book book = new Book(
            "First Book Title", 
            "It's first book used for testing", 
            412, 
            "Table of content", 
            "BookCover url"
            );

        BookCopy copy1 = new BookCopy(30, "Excellent");
        copy1.setBook(book);
        BookCopy copy2 = new BookCopy(1, "Bad");
        copy2.setBook(book);

        // Make OneToMany relation
        Set<BookCopy> bookCopies = new HashSet<>();
        bookCopies.add(copy1);
        bookCopies.add(copy2);
        book.setCopies(bookCopies);

        bookRepository.save(book);

        // Get book from database
        Book savedBook = bookRepository.findById(book.getId()).orElse(null);
        assertNotNull(savedBook);

        assertEquals("It's first book used for testing", savedBook.getDescription());

        Set<BookCopy> savedCopies = savedBook.getCopies();
        assertNotNull(savedCopies);
        assertEquals(2, savedCopies.size());

        for (BookCopy savedCopy : savedCopies) {
            assertEquals(savedBook, savedCopy.getBook());
        }

    }
}
