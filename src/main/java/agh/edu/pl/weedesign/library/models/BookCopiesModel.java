package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.services.BookCopyService;
import agh.edu.pl.weedesign.library.services.BookService;
import agh.edu.pl.weedesign.library.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BookCopiesModel {
    private final BookService bookService;
    private final RentalService rentalService;
    private final BookCopyService bookCopyService;

    @Autowired
    public BookCopiesModel(BookService bookService, RentalService rentalService, BookCopyService bookCopyService) {
        this.bookService = bookService;
        this.rentalService = rentalService;
        this.bookCopyService = bookCopyService;
    }

    public List<Rental> getRentalsByBookCopy(BookCopy bookCopy){
        return rentalService.getRentalsByBookCopy(bookCopy);
    }

    public List<BookCopy> getBookCopies(Book b){
        return bookCopyService.getCopiesAvailable(b);
    }

    public List<Rental> getRentalsByBook(Book book) {

        List<Rental> toReturn = new ArrayList<>();

        for(BookCopy bc : bookCopyService.getBookCopies(book))
            toReturn.addAll(rentalService.getRentalsByBookCopy(bc));

        return toReturn;
    }

}

