package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.controllers.BookListController;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.helpers.Recommender;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.BookService;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.RentalService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookCopiesModel {
    private BookService bookService;
    private RentalService rentalService;

    @Autowired
    public BookCopiesModel(BookService bookService, RentalService rentalService) {
        this.bookService = bookService;
        this.rentalService = rentalService;
    }

    public List<Rental> getRentalsByBookCopy(BookCopy bookCopy){
        return rentalService.getRentalsByBookCopy(bookCopy);
    }

    public List<BookCopy> getBookCopies(Book b){
        return bookService.getCopies(b);
    }

    public List<Rental> getRentalsByBook(Book book) {

        List<Rental> toReturn = new ArrayList<>();

        for(BookCopy bc : bookService.getCopies(book))
            toReturn.addAll(rentalService.getRentalsByBookCopy(bc));

        return toReturn;
    }

}

