package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;

import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.ModelService;
import agh.edu.pl.weedesign.library.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@Controller
public class BookViewController extends SubController {
    private Book book;
    private ReviewService service;

    @Autowired
    public BookViewController(ReviewService service, DataService dataService){
        super(dataService);
        this.service = service;
    }

    @FXML
    private ImageView image_cover; 

    @FXML
    private Label book_title_label;

    @FXML
    private Label author_label;

    @FXML
    private Label rating_label;

    @FXML
    private Label description_label;


    @FXML
    public void initialize() throws IOException  {
        this.book = super.dataService.getSelectedBook();

        if(this.book == null){
            System.out.println("No book was selected");
            switchScene(SceneType.BOOK_VIEW);
        }

        description_label.setText(book.getDescription());
        book_title_label.setText(book.getTitle());
        author_label.setText("Author: " + book.getAuthorString());

        Image img;

        try {
            img = new Image(book.getCoverUrl());
            image_cover.setImage(img);
        } catch (Exception e){
            System.out.println("Cover not found");
        }

        Double rating = this.service.getBookRating(book);
        rating_label.setText(rating == null ? "No ratings" : rating.toString());
    }

    public void handleCancelAction(ActionEvent e) throws IOException {
        super.switchScene(SceneType.BOOK_LIST);
    }

    public void handleRentAction(ActionEvent e) throws IOException {
        super.switchScene(SceneType.COPIES_VIEW);
    }

    public void handleShowReviewsAction() throws IOException {
        super.switchScene(SceneType.REVIEWS);
    }

    public void goBackAction(){
        super.goBack();
    }

    public void goForwardAction(){
        super.goForward();
    }

    public void settingsButtonAction() throws IOException {
        switchScene(SceneType.SETTINGS);
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
