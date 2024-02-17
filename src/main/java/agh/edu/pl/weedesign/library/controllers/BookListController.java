package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;

import agh.edu.pl.weedesign.library.models.BookListModel;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.fxml.FXML;


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
    private CheckBox firstBookRating;

    @FXML
    private CheckBox secondBookRating;

    @FXML
    private CheckBox thirdBookRating;

    @FXML
    private CheckBox fourthBookRating;

    @FXML
    private TextField searchBookField;


    @FXML
    private CheckBox onlyAvailableCheckbox;

    // Tiles View
    @FXML
    private TilePane mainPane;

    @FXML
    private VBox categoriesVBox;

    private final BookListModel bookListModel;


    @Autowired
    public BookListController(BookListModel bookListModel, DataService dataService){
        super(dataService);
        this.bookListModel = bookListModel;
        this.bookListModel.setController(this);
    }

    @FXML
    public void initialize(){
        this.bookListModel.setDataService(super.dataService);
        this.bookListModel.initializeData();

        searchBookField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    bookListModel.setSearchedStringData(searchBookField.getText());
                    reloadBooks();
                }
            }
        });

        mainPane.getChildren().addAll(this.bookListModel.getBookCovers());
        categoriesVBox.getChildren().addAll(this.bookListModel.getCategoriesLabels());
        popularBooksHBox.getChildren().addAll(this.bookListModel.getMostPopularBooks());
        recommendedBooksHBox.getChildren().addAll(this.bookListModel.getRecommendedBooksCovers());

        if(!this.dataService.getMainBookView())
            reloadBooks();
    }

    public void availableOnlyCheckboxAction(){
        this.bookListModel.setAvailableOnly(true);
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

    public void settingsButtonAction() throws IOException {
        switchScene(SceneType.SETTINGS);
    }

    public void clearFilters(){
        bookListModel.clearStrategy();
        onlyAvailableCheckbox.setSelected(false);

        popularBooksLabel.setPrefHeight(30);
        popularBooksHBox.setPrefHeight(-1);
        popularBooksScrollPane.setPrefHeight(-1);

        recommendedBooksLabel.setPrefHeight(30);
        recommendedBooksHBox.setPrefHeight(-1);
        recommendedBooksScrollPane.setPrefHeight(-1);

        mainPane.getChildren().clear();
        mainPane.getChildren().addAll(bookListModel.getBookCovers());

        popularBooksHBox.getChildren().clear();
        popularBooksHBox.getChildren().addAll(bookListModel.getMostPopularBooks());

        recommendedBooksHBox.getChildren().clear();
        recommendedBooksHBox.getChildren().addAll(bookListModel.getRecommendedBooksCovers());
    }

    private void selectRating(Integer value){
        firstBookRating.setSelected(false);
        secondBookRating.setSelected(false);
        thirdBookRating.setSelected(false);
        fourthBookRating.setSelected(false);

        switch (value){
            case 1: {firstBookRating.setSelected(true); bookListModel.setRatingRange(0, 4); break;}
            case 2: {secondBookRating.setSelected(true); bookListModel.setRatingRange(4, 6); break;}
            case 3: {thirdBookRating.setSelected(true); bookListModel.setRatingRange(6, 8);break;}
            case 4: {fourthBookRating.setSelected(true);bookListModel.setRatingRange(8, 10);break;}
            default: bookListModel.setRatingRange(null, null);
        }
    }

    public void firstBookRatingActionHandler(){
        selectRating(1);
    }

    public void secondBookRatingActionHandler(){
        selectRating(2);
    }

    public void thirdBookRatingActionHandler(){
        selectRating(3);
    }

    public void fourthBookRatingActionHandler(){
        selectRating(4);
    }


}
