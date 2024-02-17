package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.BookFilterStrategy;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataService {
    private Book selectedBook;
    private Reader reader;
    private Employee employee;

    private Rental rental;
    private Map<Book, ImageView> bookCovers = new HashMap<Book, ImageView>();
    private ArrayList<ImageView> visibleBookCovers = new ArrayList<>();
    private ArrayList<ImageView> popularBooksCovers = new ArrayList<>();
    private ArrayList<ImageView> recommendedBooksCovers = new ArrayList<>();

    private BookFilterStrategy strategy = new BookFilterStrategy();
    private Boolean mainBookView = true;

    private static DataService instance;
    private ArrayList<Label> categories = new ArrayList<>();

    public Book getSelectedBook() {
        return this.selectedBook;
    }

    public void selectBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public BookFilterStrategy getStrategy(){
        return this.strategy;
    }

    public void setStrategy(BookFilterStrategy strategy){
        this.strategy = strategy;
    }

    public void bookListMainDisplay(Boolean b){
        mainBookView = b;
    }

    public Boolean getMainBookView(){
        return mainBookView;
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

    public ArrayList<ImageView> getPopularBooksCovers() {
        return popularBooksCovers;
    }

    public void setPopularBooksCovers(ArrayList<ImageView> popularBooksCovers) {
        this.popularBooksCovers = popularBooksCovers;
    }

    public ArrayList<ImageView> getRecommendedBooksCovers() {
        return recommendedBooksCovers;
    }

    public void setRecommendedBooksCovers(ArrayList<ImageView> recommendedBooksCovers) {
        this.recommendedBooksCovers = recommendedBooksCovers;
    }
}
