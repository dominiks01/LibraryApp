package agh.edu.pl.weedesign.library.entities.author;


import javax.persistence.*;

import agh.edu.pl.weedesign.library.entities.book.Book;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Author")
public class Author {
    @Id @GeneratedValue
    private int id;

    private String name;

    private String surname;

    @Column(length = 1024)
    private String bio;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<Book>();

    public Author(){}

    public Author(String name, String surname, String bio){
        this.name = name;
        this.surname = surname;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setBook(Book book){
        this.books.add(book);
    }

    public String getFormattedName(){
        return this.getName() + this.getSurname();
    }
}
