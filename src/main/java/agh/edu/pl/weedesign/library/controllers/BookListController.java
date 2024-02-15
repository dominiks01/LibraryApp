package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;
import java.util.*;

import agh.edu.pl.weedesign.library.helpers.*;
import agh.edu.pl.weedesign.library.models.BookListModel;
import agh.edu.pl.weedesign.library.services.BookService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.category.Category;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

@Controller
public class BookListController extends IController {

    // Main pane views
    @FXML
    private Label popularBooksLabel;

    @FXML
    public ScrollPane popularBooksHBox;

    @FXML
    public VBox mainScrollPaneVBox;

    @FXML
    private Label recommendedBooksLabel;

    @FXML
    public ScrollPane recommendedBooksHBox;

    @FXML
    public ScrollPane mainPaneScroll;

    @FXML
    private Label allBooksLabel;

    @FXML
    private CheckBox onlyAvailableCheckbox;

    // Tiles View
    @FXML
    private TilePane mainPane;

    // Navbar controls 
    @FXML
    private Button mainPage; 

    @FXML
    private Button myRentals; 

    @FXML
    private Button logOut;

    @FXML
    private VBox categoriesVBox;

    private final BookListModel bookListModel;


    @Autowired
    public BookListController(BookListModel bookListModel){
        this.bookListModel = bookListModel;
        this.bookListModel.setController(this);
    }

    @Override
    public void consumeData(){
        this.bookListModel.setDataService(super.dataService);
        this.bookListModel.initializeData();

        mainPane.getChildren().addAll(this.bookListModel.getBookCovers());
        categoriesVBox.getChildren().addAll(this.bookListModel.getCategoriesLabels());
    }

    @FXML
    public void initialize(){
    }

    public void availableOnlyCheckboxAction(){
        reloadBooks();
    }

    public void reloadBooks(){
        mainPane.getChildren().clear();
        mainPane.getChildren().addAll(bookListModel.getBookCovers());

        popularBooksLabel.setPrefHeight(0);
        popularBooksHBox.setPrefHeight(0);
        recommendedBooksLabel.setPrefHeight(0);
        recommendedBooksHBox.setPrefHeight(0);
    }

    public void switchScene(SceneType sceneType) throws IOException {
        super.switchScene(sceneType);
    }

    public void goBackAction(){
        super.goBack();
    }

    public void goForwardAction(){
        super.goForward();
    }

    public void mainPageButtonHandler() throws IOException {
        clearFilters();
        super.switchScene(SceneType.BOOK_LIST);
    }

    public void myRentalsButtonHandler() throws IOException {
        clearFilters();
        super.switchScene(SceneType.RENTALS_VIEW);
    }

    public void LogOutAction() throws IOException {
        clearFilters();
        super.logOutAction();
    }

    public void clearFilters(){
        bookListModel.selectCategory(null);

        popularBooksLabel.setPrefHeight(30);
        popularBooksHBox.setPrefHeight(-1);
        recommendedBooksLabel.setPrefHeight(30);
        recommendedBooksHBox.setPrefHeight(-1);

        mainPane.getChildren().clear();
        mainPane.getChildren().addAll(bookListModel.getBookCovers());
    }
}
