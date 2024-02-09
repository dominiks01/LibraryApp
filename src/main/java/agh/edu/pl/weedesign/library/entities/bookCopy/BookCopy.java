package agh.edu.pl.weedesign.library.entities.bookCopy;

import javax.persistence.*;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.rental.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="BOOK_COPY")
public class BookCopy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int week_unit_price;

    private String condition;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookCopy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rental> rentals;

    public BookCopy(){};

    public BookCopy(int week_unit_price, String condition){
        this.week_unit_price = week_unit_price;
        this.condition = condition;
    }


    public int getId() {
        return id;
    }

    public int getWeek_unit_price() {
        return week_unit_price;
    }

    public void setWeek_unit_price(int week_unit_price) {
        this.week_unit_price = week_unit_price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setBook(Book book){
        this.book = book;
    }

    public Book getBook(){
        return book;
    }

}
