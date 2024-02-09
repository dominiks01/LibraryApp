package agh.edu.pl.weedesign.library.services;

import org.springframework.stereotype.Service;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.book.BookRepository;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopyRepository;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.category.CategoryRepository;
import agh.edu.pl.weedesign.library.entities.rental.RentalRepository;

@Service
public class BookService {
    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final AuthorService authorService;
    private final CategoryRepository categoryRepository;

    public BookService(RentalRepository rentalRepository, BookRepository bookRepository, BookCopyRepository bookCopyRepository, AuthorService authorService, CategoryRepository categoryRepository) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.authorService = authorService;
        this.categoryRepository =  categoryRepository;
    }

    public Author getAuthor(String first_name, String surname){
        return this.authorService.findByName(first_name, surname);
    }

    public Category getCategory(String name){
        return this.categoryRepository.findByName(name);
    }

    public void addNewAuthor(Author author){
        this.authorService.addNewAuthor(author);
    }

    public void addNewBookCopy(BookCopy bookCopy){
        this.bookCopyRepository.save(bookCopy);
    }

    public void addNewBook(Book book){
        this.bookRepository.save(book);
    }
}
