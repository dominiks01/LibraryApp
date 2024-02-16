package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;

import agh.edu.pl.weedesign.library.models.BookListModel;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.fxml.FXML;

import javax.annotation.PostConstruct;

@Controller
public class BookListController extends SubController {

    // Main pane views
    @FXML
    private Label popularBooksLabel;

    @FXML
    public HBox popularBooksHBox;

    @FXML
    public VBox mainScrollPaneVBox;

    @FXML
    private Label recommendedBooksLabel;

    @FXML
    public HBox recommendedBooksHBox;

    @FXML
    public ScrollPane recommendedBooksScrollPane;

    @FXML
    public ScrollPane popularBooksScrollPane;

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
    public BookListController(BookListModel bookListModel, DataService dataService, MainController mainController){
        super(dataService);
        this.bookListModel = bookListModel;
        this.bookListModel.setController(this);
    }

    @FXML
    public void initialize(){
        this.bookListModel.setDataService(super.dataService);
        this.bookListModel.initializeData();

        mainPane.getChildren().addAll(this.bookListModel.getBookCovers());
        categoriesVBox.getChildren().addAll(this.bookListModel.getCategoriesLabels());
        popularBooksHBox.getChildren().addAll(this.bookListModel.getMostPopularBooks());
        recommendedBooksHBox.getChildren().addAll(this.bookListModel.getRecommendedBooksCovers());
    }

    public void availableOnlyCheckboxAction(){
        reloadBooks();
    }

    public void reloadBooks(){
        mainPane.getChildren().clear();
        mainPane.getChildren().addAll(bookListModel.getBookCovers());

        popularBooksLabel.setPrefHeight(0);
        popularBooksScrollPane.setPrefHeight(0);
        recommendedBooksLabel.setPrefHeight(0);
        recommendedBooksScrollPane.setPrefHeight(0);
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
        popularBooksScrollPane.setPrefHeight(-1);

        recommendedBooksLabel.setPrefHeight(30);
        recommendedBooksHBox.setPrefHeight(-1);
        recommendedBooksScrollPane.setPrefHeight(-1);

        mainPane.getChildren().clear();
        mainPane.getChildren().addAll(bookListModel.getBookCovers());
        popularBooksHBox.getChildren().addAll(this.bookListModel.getMostPopularBooks());
        recommendedBooksHBox.getChildren().addAll(this.bookListModel.getRecommendedBooksCovers());
    }
}
