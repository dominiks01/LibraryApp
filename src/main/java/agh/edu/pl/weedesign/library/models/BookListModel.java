package agh.edu.pl.weedesign.library.models;

import agh.edu.pl.weedesign.library.controllers.BookListController;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.helpers.Recommender;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.BookService;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookListModel {
    private ConfigurableApplicationContext springContext;
    private BookService service;
    private Category selectedCategory;
    private DataService dataService;
    private BookListController controller;
    private Recommender recommender;

    private BookListProcessor bookListProcessor;

    @Autowired
    public BookListModel(ConfigurableApplicationContext springContext, BookService bookService, Recommender recommender ){
        this.springContext = springContext;
        this.service = bookService;
        this.recommender = recommender;
    }

    public ArrayList<ImageView> getBookCovers(){

        if(selectedCategory != null){
            return dataService.getBookCovers().entrySet().stream()
                    .filter(x->x.getKey().getCategory().getName().equals(this.selectedCategory.getName()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new ArrayList<>(dataService.getBookCovers().values());
    }

    public ArrayList<ImageView> getMostPopularBooks(){
        return dataService.getPopularBooksCovers();
    }

    public ArrayList<ImageView> getRecommendedBooksCovers(){
        return dataService.getRecommendedBooksCovers();
    }

    public ArrayList<Label> getCategoriesLabels(){
        return dataService.getCategories();
    }

    public void initializeData(){
        if(dataService.getBookCovers().isEmpty()){

            HashMap<Book, ImageView> bookCovers = (HashMap<Book, ImageView>) getBooks()
                    .stream()
                    .collect(Collectors.toMap(value -> value, this::createImageCover));

            dataService.setBookCovers(bookCovers);
        }

        if( dataService.getCategories().isEmpty()){

            ArrayList<Label> categoryLabels = getCategories()
                    .stream().map(this::createCategory)
                    .collect(Collectors.toCollection(ArrayList::new));

            dataService.setCategories(categoryLabels);
        }

        if (dataService.getPopularBooksCovers().isEmpty()){
            dataService.setPopularBooksCovers(
                    dataService.getBookCovers().entrySet().stream()
                    .filter(x-> recommender.getMostPopularBooks(20).contains(x.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }



        if (dataService.getRecommendedBooksCovers().isEmpty()){
            dataService.setRecommendedBooksCovers(
                    dataService.getBookCovers().entrySet().stream()
                            .filter(x-> recommender.getMostPopularBooks(10).contains(x.getKey()))
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    public void setController(BookListController controller){
        this.controller = controller;
    }

    public Book getSelectedBook() {
        return this.dataService.getSelectedBook();
    }

    public void selectBook(Book selectedBook) {
        this.dataService.selectBook(selectedBook);
    }

    public ArrayList<Book> getBooks(){
        return (ArrayList<Book>) this.service.getBooks();
    }

    public List<Category> getCategories(){
        return this.service.getCategories();
    }

    public void setDataService(DataService dataService){
        this.dataService = dataService;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void selectCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    private Label createCategory(Category c){
        Label label = new Label();
        label.setText(c.getName());
        label.setUnderline(true);
        label.setFont(Font.font("System", 15));
        VBox.setMargin(label, new Insets(0, 0, 0, 15));

        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            selectCategory(c);
            controller.reloadBooks();
        });

        return label;
    };

    private ImageView createImageCover(Book b) {
        ImageView newCover = new ImageView();
        newCover.setCache(true);
        newCover.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(getSelectedBook() == b) {
                try {
                    controller.switchScene(SceneType.BOOK_VIEW);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            selectBook(b);
        }
        );

        newCover.setFitHeight(220);
        newCover.setFitWidth(150);

        Image img;

        try {
            img = new Image("" + b.getCoverUrl() + "", true);
        } catch (Exception e){
            img = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("default_book_cover_2015.jpg")).toString(), true);
        }

        newCover.setImage(img);
        return newCover;
    }

}
