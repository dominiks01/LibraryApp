package agh.edu.pl.weedesign.library.models;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.services.BookService;

@Component
public class NewBookModel {

    private ConfigurableApplicationContext spring_context;
    private BookService book_service;

    private String title; 
    private String description; 
    private String table_of_content; 
    private String cover; 
    private String author_first_name; 
    private String author_second_name; 
    private String condition; 
    private Integer page_count; 
    private Integer no_of_copies; 

    @Autowired
    public NewBookModel(ConfigurableApplicationContext spring_context, BookService book_service){
        this.spring_context = spring_context;
        this.book_service = book_service;
    };

    public void addNewBook(){
        Book book = new Book(
            getTitle(),
            getDescription(),
            getPageCount(), 
            getTableOfContent(),
            getCover()
        );

        Author author = this.book_service.getAuthor(
            getAuthorFirstName(), 
            getAuthorSecondName()
        );

        if(author == null){
            author = new Author(
                getAuthorFirstName(),
                getAuthorSecondName(),
                ""
            );

            this.book_service.addNewAuthor(author);
        }
        
        book.setAuthor(author);
        book.setCategory(this.book_service.getCategory("Fiction"));

        book_service.addNewBook(book);
        System.out.println(book);

        for(int i = 0; i < getNumberOfCopies(); i++){
            BookCopy bc = new BookCopy(12, getCondition());
            bc.setBook(book);

            this.book_service.addNewBookCopy(bc);
        }
    }

    public void setTitle(String title){
        if(title == "")
            throw new IllegalArgumentException("[ERR] Pusty tytuł!");
        
        if(title.length() >= 255)
            throw new IllegalArgumentException("[ERR] Zbyt długi tytuł (Max. 255 znaków)!");

        this.title = title; 
    }

    public String getTitle(){
        return this.title;
    }

    public void setDescription(String description){
        if(description == "")
            throw new IllegalArgumentException("[ERR] Brak Opisu!");
        
        if(description.length() >= 4000)
            throw new IllegalArgumentException("[ERR] Zbyt długi opis (Max. 255 znaków)!");
        
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setCover(String cover){
        if(cover == "")
            throw new IllegalArgumentException("[ERR] Cover not covered!");

        this.cover = cover; 
    }

    public String getCover(){
        return this.cover;
    }

    public void setAuthorFirstName(String author_first_name){
        if(author_first_name == "")
            throw new IllegalArgumentException("[ERR] Empty Author!");

        this.author_first_name = author_first_name; 
    }

    public String getAuthorFirstName(){
        return this.author_first_name;
    }

    public void setAuthorSecondName(String author_second_name){
        if(author_second_name == "")
            throw new IllegalArgumentException("[ERR] Empty Author!");
        
        this.author_second_name = author_second_name; 
    }

    public String getAuthorSecondName(){
        return this.author_second_name;
    }

    public void setTableOfContent(String table_of_content){
        if(table_of_content == "")
            throw new IllegalArgumentException("[ERR] Empty Table of Content!");
        
        this.table_of_content = table_of_content; 
    }

    public String getTableOfContent(){
        return this.table_of_content;
    }

    public void setPageCount(Integer page_count){
        if(page_count == 0)
            throw new IllegalArgumentException("[ERR] Enter valid number of pages!");

        this.page_count = page_count; 
    }

    public Integer getPageCount(){
        return this.page_count;
    }

    public void setCondition(String condition){

        ArrayList<String> valid_condition =  new ArrayList<String>(){
            {
                add("good");
                add("bad");
                add("very good");
                add("very bad");

            }
        };

        if(!valid_condition.contains(condition)){
            String message = "[ERR] Valid conditions: | ";
            for(String cond: valid_condition)
                message += " " + cond + " |";

            throw new IllegalArgumentException(message);

        }

        this.condition = condition; 
    }

    public String getCondition(){
        return this.condition;
    }

     public void setNoOfCopies(Integer no_of_copies){
        if(no_of_copies == 0)
            throw new IllegalArgumentException("[ERR] Enter number of copies!");

        this.no_of_copies = no_of_copies; 
    }

    public Integer getNumberOfCopies(){
        return this.no_of_copies;
    }

}
