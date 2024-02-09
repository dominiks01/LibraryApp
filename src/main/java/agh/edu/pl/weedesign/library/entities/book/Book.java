package agh.edu.pl.weedesign.library.entities.book;


import javax.persistence.*;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.category.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="Book")
public class Book {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(length = 4000)
    private String description;

    private int page_count;

    @Column(length = 512)
    private String table_of_content;

    private String cover_url;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "book_author", 
            joinColumns = { @JoinColumn(name = "book_id") }, 
            inverseJoinColumns = { @JoinColumn(name = "author_id") })
    private Set<Author> authors = new HashSet<Author>();

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy="book")
    private Set<BookCopy> bookCopies;

    public Book(){}

    public Book(String title, String description, int page_count, String table_of_content, String cover_url){
        this.title = title;
        this.description = description;
        this.page_count = page_count;
        this.table_of_content = table_of_content;
        this.cover_url = cover_url;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return page_count;
    }

    public void setPageCount(int page_count) {
        this.page_count = page_count;
    }

    public String getTableOfContent() {
        return table_of_content;
    }

    public void setTableOfContent(String table_of_content) {
        this.table_of_content = table_of_content;
    }

    public String getCoverUrl() {
        return cover_url;
    }

    public void setCoverUrl(String cover_url) {
        this.cover_url = cover_url;
    }

    public void setAuthor(Author author){
        this.authors.add(author);
    }

    public Set<Author> getAuthors(){
        return authors;
    }

    public String getAuthorString(){
        return this.authors.stream()
            .map(Author::getFormattedName)
            .collect(Collectors.joining(", "));
    }

    public String getAuthorsNames() {
        return this.authors.stream().map(Author::getName).collect(Collectors.joining());
    }

    public String getAuthorsSurnames() {
        return this.authors.stream().map(Author::getSurname).collect(Collectors.joining());
    }



    public void setCategory(Category category){
        this.category = category;
    }

    public Category getCategory(){
        return this.category;
    }

    public Set<BookCopy> getCopies(){
        return this.bookCopies;
    }

    public void setCopies(Set<BookCopy> bookCopies){
        this.bookCopies = bookCopies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
