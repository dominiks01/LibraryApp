package agh.edu.pl.weedesign.library.helpers;

import agh.edu.pl.weedesign.library.entities.book.*;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.services.BookService;
import agh.edu.pl.weedesign.library.services.ModelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookListProcessor {
    private BookService bookService;


    @Autowired
    public BookListProcessor(BookService service){
        bookService = service;
    }

    public List<Book> processList(List<Book> books, Category category, SearchStrategy searchBy, String searchName, boolean onlyAvailable){

        if (onlyAvailable)
            books = onlyAvailableBooks(books);

        if (category != null)
            books = onlyBooksInCategory(books, category);

        if (searchBy != null && searchName != null && !searchName.isEmpty())
            books = search(books,searchBy,searchName);

        return books;
    }

    private List<Book> search(List<Book> books, SearchStrategy searchBy, String searchName){
        String searchLowerCase = searchName.toLowerCase();

        return switch (searchBy){
            case NAME -> books.stream().filter(b -> b.getAuthorsNames().toLowerCase().contains(searchLowerCase)).toList();
            case SURNAME -> books.stream().filter(b -> b.getAuthorsSurnames().toLowerCase().contains(searchLowerCase)).toList();
            case TITLE -> books.stream().filter(b -> b.getTitle().toLowerCase().contains(searchLowerCase)).toList();
        };
    }

    private List<Book> onlyAvailableBooks(List<Book> books){
       return books;
    }

    private List<Book> onlyBooksInCategory(List<Book> books, Category c){
        return books.stream()
                .filter(i -> Objects.equals(i.getCategory().getName(), c.getName()))
                .toList();

    }

}
