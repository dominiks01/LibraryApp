package agh.edu.pl.weedesign.library.entities.reservation;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.reader.Reader;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    private LocalDateTime request_date;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    public Reservation() {
    }

    public Reservation(Reader reader, LocalDateTime date, Book book) {
        this.reader = reader;
        this.request_date = date;
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public LocalDateTime getRequest_date() {
        return request_date;
    }

    public void setRequest_date(LocalDateTime request_date) {
        this.request_date = request_date;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
