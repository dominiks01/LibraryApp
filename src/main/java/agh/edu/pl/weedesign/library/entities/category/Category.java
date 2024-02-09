package agh.edu.pl.weedesign.library.entities.category;

import javax.persistence.*;

import agh.edu.pl.weedesign.library.entities.book.Book;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id @GeneratedValue
    private int id;

    private String name;

    public Category(){};

    public Category(String name){
        this.name = name;
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

}
