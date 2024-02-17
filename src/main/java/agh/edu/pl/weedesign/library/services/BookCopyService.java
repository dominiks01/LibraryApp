package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.book.BookRepository;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopyRepository;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.category.CategoryRepository;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.rental.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final RentalService rentalService;

    public BookCopyService(BookCopyRepository bookCopyRepository, RentalService rentalService) {
        this.bookCopyRepository = bookCopyRepository;
        this.rentalService = rentalService;
    }

    public List<BookCopy> getCopiesAvailable(Book b){
        List<BookCopy> bookCopies = bookCopyRepository.findBookCopiesByBook(b);
        List<Integer> rentedBooks = rentalService.getRentedBooks().stream().map(x -> x.getBookCopy().getId()).toList();

        return bookCopies.stream().filter(x -> !rentedBooks.contains(x.getId())).toList();
    }

    public List<BookCopy> getBookCopies(Book b){
        return this.bookCopyRepository.findBookCopiesByBook(b);
    }


}
