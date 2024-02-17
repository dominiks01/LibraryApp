package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.RentalService;
import agh.edu.pl.weedesign.library.services.ReviewService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class AddReviewController extends SubController {
    @FXML
    private TextArea commentArea;

    @FXML
    private ChoiceBox<Integer> choiceBox;

    @FXML
    private Label bookTitle;

    @FXML
    private Text errorMsg;

    private final ReviewService reviewService;
    private Rental rental;
    private final RentalService rentalService;

    @FXML
    private ImageView image_cover;

    @Autowired
    public AddReviewController(ReviewService reviewService, RentalService rentalService, DataService dataService){
        super(dataService);
        this.reviewService = reviewService;
        this.rentalService = rentalService;
    }

    @FXML
    public void initialize(){
        this.rental = super.dataService.getRental();
        Image img;

        try {
            img = new Image("" + super.dataService.getSelectedBook().getCoverUrl() + "");
            image_cover.setImage(img);
        } catch (Exception e){
            System.out.println("Cover not found");
        }
    }
    @FXML
    private void handleAddReviewAction(ActionEvent event) throws IOException {
        if (this.choiceBox.getValue() == null){
            this.errorMsg.setText("You must pick rating!");
            System.out.println("rate is null");
            return;
        }
        int rating = choiceBox.getValue();
        String comment = commentArea.getText();
        Review review = new Review(rating, comment, LocalDateTime.now(), rental);
        saveReview(review);
    }

    public void handleCancelAction(ActionEvent event) throws IOException {
        super.switchScene(SceneType.SINGLE_RENTAL);
    }

    @Transactional
    public void saveReview(Review review) throws IOException {
        rental.setReview(review);
        reviewService.addNewReview(review);
        rental = rentalService.updateRental(rental);
        super.switchScene(SceneType.RENTALS_VIEW);
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
        super.switchScene(SceneType.BOOK_LIST);
    }

    public void myRentalsButtonHandler() throws IOException {
        super.switchScene(SceneType.RENTALS_VIEW);
    }

    public void LogOutAction() throws IOException {
        super.logOutAction();
    }

    public void settingsButtonAction() throws IOException {
        switchScene(SceneType.SETTINGS);
    }


}
