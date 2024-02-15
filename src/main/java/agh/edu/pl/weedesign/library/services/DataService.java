package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.book.BookRepository;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.review.Review;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataService {
    private Review selectedReview;
    private Book selectedBook;
    private Reader reader;
    private Employee employee;


    private Rental rental;
    private Map<Book, ImageView> bookCovers = new HashMap<Book, ImageView>();
    private ArrayList<ImageView> visibleBookCovers = new ArrayList<>();
    private static DataService instance;
    private ArrayList<Label> categories = new ArrayList<>();

    public Review getSelectedReview() {
        return this.selectedReview;
    }

    public void setSelectedReview(Review selectedReview) {
        this.selectedReview = selectedReview;
    }

    public Book getSelectedBook() {
        return this.selectedBook;
    }

    public void selectBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public void setReader(Reader r){
        this.reader = r;
    }

    public Reader getReader(){
        return this.reader;
    }

    public void setEmployee(Employee e){
        this.employee = e;
    }

    public Employee getEmployee(){
        return this.employee;
    }

    public Map<Book, ImageView> getBookCovers() {
        return bookCovers;
    }

    public void setBookCovers(Map<Book, ImageView> bookCovers) {
        this.bookCovers = bookCovers;
    }

    public ArrayList<ImageView> getVisibleBookCovers() {
        return visibleBookCovers;
    }

    public void setVisibleBookCovers(ArrayList<ImageView> visibleBookCovers) {
        this.visibleBookCovers = visibleBookCovers;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public ArrayList<Label> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Label> categories) {
        this.categories = categories;
    }
}
