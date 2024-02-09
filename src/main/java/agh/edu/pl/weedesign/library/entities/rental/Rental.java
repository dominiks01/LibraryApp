package agh.edu.pl.weedesign.library.entities.rental;

import javax.persistence.*;

import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.review.Review;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Rental {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    private int price;

    private LocalDateTime start_date;

    private LocalDateTime end_date;

    @ManyToOne
    @JoinColumn(name="book_copy_id")
    private BookCopy bookCopy;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @OneToOne
    @JoinColumn(name="review_id", referencedColumnName = "id")
    private Review review;

    public Rental(){};

    public Rental(LocalDateTime start_date){
        this.start_date = start_date;
        this.end_date = null;
        this.price = 0;
    }

    public Rental(LocalDateTime start_date, LocalDateTime end_date){
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = 0;
    }


    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
        this.price = this.countPrice(end_date);
    }

    public int countPrice(LocalDateTime end){
        if(this.bookCopy != null){
            int nOfWeeks = (int)this.start_date.until(end, ChronoUnit.WEEKS) + 1;
            return nOfWeeks*this.bookCopy.getWeek_unit_price();
        }
        return 0;
    }

    public void setBookCopy(BookCopy bookCopy){
        this.bookCopy = bookCopy;
        if(end_date == null)
            this.price = this.countPrice(LocalDateTime.now());
        else
            this.price = this.countPrice(end_date);
    }

    public BookCopy getBookCopy(){
        return bookCopy;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader getReader() {
        return reader;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setReview(Review review){
        this.review = review;
    }

    public Review getReview(){
        return review;
    }
}
