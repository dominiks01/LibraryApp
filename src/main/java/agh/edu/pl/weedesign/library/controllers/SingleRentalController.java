package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.reservation.Reservation;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SingleRentalController extends SubController {

    @FXML
    private Label titleText;
    @FXML
    private Label authorText;
    @FXML
    private Label weekPriceText;
    @FXML
    private Label priceText;
    @FXML
    private Label employeeText;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;

    @FXML
    private ImageView image_cover;

    private Rental rental;
    private Book rentalBook;

    private final RentalModel rentalModel;


    @Autowired
    public SingleRentalController(RentalModel rentalModel, DataService dataService){
        super(dataService);
        this.rentalModel = rentalModel;
    }

    @FXML
    public void initialize(){
        this.rental = dataService.getRental();
        this.rentalBook = rental.getBookCopy().getBook();

        Image img;

        try {
            img = new Image(this.rental.getBookCopy().getBook().getCoverUrl());
            image_cover.setImage(img);
        } catch (Exception e){
            System.out.println("Cover not found");
        }

        this.setValues();
    }

    private void setValues(){
        this.titleText.setText(rentalBook.getTitle());

        this.authorText.setText(rentalBook.getAuthorString());

        this.weekPriceText.setText("Price: " + this.rental.getBookCopy().getWeekUnitPrice() + "zł");
        this.priceText.setText("Total Price: " + this.rental.getPrice() + "zł");

        if(this.rental.getEmployee() != null)
            this.employeeText.setText("Manager: " + this.rental.getEmployee().getName() + " " + this.rental.getEmployee().getSurname());
        else
            this.employeeText.setText("Waiting for acceptance");

        if(this.rental.getEndDate() != null)
            this.cancelButton.setVisible(false);

        if(this.rental.getReview() != null || this.rental.getEmployee() == null)
            this.addButton.setVisible(false);

    }

    @FXML
    private void cancelRental(ActionEvent event) throws IOException {
        rentalModel.finishRental(this.rental);
        super.switchScene(SceneType.RENTALS_VIEW);
    }

    @FXML
    private void addReviewAction(ActionEvent event) throws IOException {
        super.switchScene(SceneType.ADD_REVIEW);
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
