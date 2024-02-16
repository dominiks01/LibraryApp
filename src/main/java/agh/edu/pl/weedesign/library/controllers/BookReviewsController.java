package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
@Controller
@Lazy
public class BookReviewsController extends SubController {
    private List<Review> reviewList;
    private Book book;
    private ReviewRepository reviewRepository;
    @FXML
    private TableView<Review> reviewsTable;
    @FXML
    private TableColumn<Review, Integer> idColumn;
    @FXML
    private TableColumn<Review, Integer> rateColumn;
    @FXML
    private TableColumn<Review, String> commentColumn;
    @FXML
    private TableColumn<Review, String> dateColumn;


    public BookReviewsController(ReviewRepository reviewRepository, DataService dataService) {
        super(dataService);
        this.reviewRepository = reviewRepository;
    }


    @FXML
    public void initialize() throws IOException {
        this.book = super.dataService.getSelectedBook();

        if(this.book == null){
            System.out.println("No book was selected");
            super.switchScene(SceneType.BOOK_VIEW);
        }

        this.reviewList = reviewRepository.findReviewsOfBook(book.getId());

        reviewsTable.setItems(FXCollections.observableList(this.reviewList));

        idColumn.setCellValueFactory(reviewValue ->
                new ReadOnlyObjectWrapper<>(reviewValue.getValue().getId()));

        rateColumn.setCellValueFactory(reviewValue ->
                new ReadOnlyObjectWrapper<>(reviewValue.getValue().getStars()));

        commentColumn.setCellValueFactory(reviewValue ->
                new SimpleStringProperty(reviewValue.getValue().getComment()));

        dateColumn.setCellValueFactory(reviewValue ->
                new SimpleStringProperty(reviewValue.getValue().getDateTime().toLocalDate().toString())
        );
    }

    public void goBackAction(){
        super.goBack();
    }

    public void goForwardAction(){
        super.goForward();
    }

    public void mainPageButtonHandler() throws IOException {
        super.switchScene(SceneType.BOOK_LIST);
    }

    public void myRentalsButtonHandler() throws IOException {
        super.switchScene(SceneType.RENTALS_VIEW);
    }

    public void LogOutAction() throws IOException {
        super.logOutAction();
    }
}
