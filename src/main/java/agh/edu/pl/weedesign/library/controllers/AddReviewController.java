package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.RentalService;
import agh.edu.pl.weedesign.library.services.ReviewService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Controller
public class AddReviewController {
    private final RentalsController rentalsController;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addReviewButton;
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
    private final ReviewRepository reviewRepository;
    @Autowired
    public AddReviewController(ReviewRepository reviewRepository, ReviewService reviewService, RentalsController rentalsController, RentalService rentalService){
        this.reviewService = reviewService;
        this.rentalService = rentalService;
        this.rentalsController = rentalsController;
        this.reviewRepository = reviewRepository;
    }
    @FXML
    public void initialize(){
        this.rental = rentalsController.getSelectedRental();
    }
    @FXML
    private void handleAddReviewAction(ActionEvent event){
        if (this.choiceBox.getValue() == null){
            this.errorMsg.setText("Należy wybrać ocenę!");
            System.out.println("rate is null");
            return;
        }
        int rating = choiceBox.getValue();
        String comment = commentArea.getText();
        Review review = new Review(rating, comment, LocalDateTime.now(), rental);
        saveReview(review);
    }
    @FXML
    private void handleCancelAction(ActionEvent event){
        LibraryApplication.getAppController().switchScene(SceneType.SINGLE_RENTAL);
    }

    @Transactional
    public void saveReview(Review review){
        rental.setReview(review);
        reviewService.addNewReview(review);
        rental = rentalService.updateRental(rental);
        LibraryApplication.getAppController().switchScene(SceneType.RENTALS_VIEW);
    }
}
