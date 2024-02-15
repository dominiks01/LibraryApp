package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;

import agh.edu.pl.weedesign.library.services.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


@Controller
public class BookViewController extends IController {
    private Book book;
    private ModelService service;

    @Autowired
    public BookViewController(RentalModel rental_model, ModelService service){
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
    private Button rent_button;

    @FXML
    private Button cancel_button;

    // navbar 
    @FXML 
    private Button mainPage;

    @FXML
    private Button  myRentals;

    @FXML 
    private Button logOut; 
    
    @FXML
    public void initialize() throws IOException  {
    }

    @Override
    public void consumeData() throws IOException {
        this.book = super.dataService.getSelectedBook();

        if(this.book == null){
            System.out.println("No book was selected");
            LibraryApplication.getAppController().switchScene(SceneType.BOOK_LIST);
        }

        description_label.setText(book.getDescription());
        book_title_label.setText(book.getTitle());

        author_label.setText(
                "Autor: " + book.getAuthorString()
        );

        Image img;

        try {
            img = new Image("" + book.getCoverUrl() + "");
            image_cover.setImage(img);
        } catch (Exception e){
            System.out.println("Cover not found");
        }
        Double rating = this.service.getAverageRating(book);
        rating_label.setText(rating == null ? "Brak ocen" : rating.toString());
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
