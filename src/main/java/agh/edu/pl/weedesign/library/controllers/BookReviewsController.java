package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.util.List;
@Controller
@Lazy
public class BookReviewsController extends IController {
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

 // Navbar controls 
    @FXML
    private Button mainPage; 

    @FXML
    private Button myRentals; 

    @FXML
    private Button logOut; 

   @FXML 
    private ChoiceBox<String> themeChange;


    @FXML
    private void handleBackAction(){
//        LibraryApplication.getAppController().switchScene(SceneType.BOOK_VIEW);
    }

    public BookReviewsController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void consumeData(){
        this.book = super.dataService.getSelectedBook();

        if(this.book == null){
            System.out.println("No book was selected");
//            LibraryApplication.getAppController().switchScene(SceneType.BOOK_LIST);
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

    @FXML
    public void initialize(){


    }

    public void goBackAction(){
        LibraryApplication.getAppController().back();
    }

    public void goForwardAction(){
        LibraryApplication.getAppController().forward();
    }

    public void mainPageButtonHandler(){
//        LibraryApplication.getAppController().switchScene(SceneType.BOOK_LIST);
    }

    public void myRentalsButtonHandler(){
//        LibraryApplication.getAppController().switchScene(SceneType.RENTALS_VIEW);
    }

    public void changeTheme(ActionEvent e){
        LibraryApplication.changeTheme(themeChange.getValue());
    }

    public void LogOutAction(){
//        LibraryApplication.getAppController().logOut();
    }
}
