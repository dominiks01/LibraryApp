package agh.edu.pl.weedesign.library.helpers;

import agh.edu.pl.weedesign.library.entities.book.*;
import agh.edu.pl.weedesign.library.services.ModelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class BookListProcessor {
    ModelService service;

    @Autowired
    public BookListProcessor(ModelService service){
        this.service = service;
    }

    public List<Book> processList(List<Book> books, SearchStrategy searchBy, String searchName, Map<Book,Long> booksCount, boolean onlyAvailable){
        List<Book> tempBooks = books;
        System.out.println(tempBooks);
        if (onlyAvailable){
            tempBooks = onlyAvailableBooks(tempBooks, booksCount);
        }
        System.out.println(tempBooks);
        if (searchBy != null && searchName != null && !searchName.isEmpty()){
            tempBooks = search(tempBooks,searchBy,searchName);
        }
        System.out.println(tempBooks);

        return tempBooks;
    }

    //
    private List<Book> search(List<Book> books, SearchStrategy searchBy, String searchName){
        String searchLowerCase = searchName.toLowerCase();
        return switch (searchBy){
            case NAME -> books.stream().filter(b -> b.getAuthorsNames().toLowerCase().contains(searchLowerCase)).toList();
            case SURNAME -> books.stream().filter(b -> b.getAuthorsSurnames().toLowerCase().contains(searchLowerCase)).toList();
            case TITLE -> books.stream().filter(b -> b.getTitle().toLowerCase().contains(searchLowerCase)).toList();
        };
    }

    private List<Book> onlyAvailableBooks(List<Book> books, Map<Book,Long> booksCount){
        return books.stream().filter(b -> booksCount.get(b) != null).toList();
    }
}
