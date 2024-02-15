package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.reservation.Reservation;
import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;
import agh.edu.pl.weedesign.library.helpers.Themes;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.EmailService;
import agh.edu.pl.weedesign.library.services.ModelService;
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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SingleRentalController extends IController {
    private final RentalsController rentalsController;

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

    private final ReviewService reviewService;
    private Rental rental;
    private final RentalService rentalService;
    private final ReviewRepository reviewRepository;
    private ModelService service;


    @Autowired
    public SingleRentalController(ReviewRepository reviewRepository, ReviewService reviewService, RentalsController rentalsController, RentalService rentalService, ModelService service){
        this.reviewService = reviewService;
        this.rentalService = rentalService;
        this.rentalsController = rentalsController;
        this.reviewRepository = reviewRepository;
        this.service = service;
    }

    @Override
    public void consumeData(){
        this.rental = rentalsController.getSelectedRental();

        Image img;

        try {
            img = new Image("" + this.rental.getBookCopy().getBook().getCoverUrl() + "");
            image_cover.setImage(img);
        } catch (Exception e){
            System.out.println("Cover not found");
        }

        this.setValues();
    }

    @FXML
    public void initialize(){
    }

    private void setValues(){
        this.titleText.setText(this.rental.getBookCopy().getBook().getTitle());
        String authors = "";
        for(Author author : this.rental.getBookCopy().getBook().getAuthors())
            authors += author.getName() + " " + author.getSurname() + "   ";
        this.authorText.setText(authors);
        this.weekPriceText.setText("Cena tygodniowa: " + this.rental.getBookCopy().getWeek_unit_price() + "zł");
        this.priceText.setText("Należność: " + this.rental.getPrice() + "zł");
        if(this.rental.getEmployee() != null)
            this.employeeText.setText("Bibliotekarz: " + this.rental.getEmployee().getName() + " " + this.rental.getEmployee().getSurname());
        else
            this.employeeText.setText("Wypożyczenie jeszcze nie zostało zaakceptowane");

        if(this.rental.getEnd_date() != null)
            this.cancelButton.setVisible(false);
        if(this.rental.getReview() != null || this.rental.getEmployee() == null)
            this.addButton.setVisible(false);

    }

    @FXML
    private void cancelRental(ActionEvent event) throws IOException {
        informAboutReturnedBook();

        if(this.rental.getEmployee() == null){
            //jezeli nie bylo akceptacji, to po prostu usuwam to wypozyczenie
            this.rentalService.removeRental(this.rental);
            return;
        }
        this.rental.setEnd_date(LocalDateTime.now());
        this.rentalService.updateRental(this.rental);
        this.cancelButton.setVisible(false);

        super.getMainController().reload();
        ((Stage)cancelButton.getScene().getWindow()).close();

    }

    private void informAboutReturnedBook(){
        List<Reservation> reservations = this.service.getReservationsByBook(this.rental.getBookCopy().getBook());
        this.service.deleteAllReservationsByBook(reservations);
        if (reservations == null) return;
        for (Reservation r: reservations){
            EmailService emailService = new EmailService();
            emailService.sendSimpleMessage(
                    r.getReader().getEmail(),
                    "Dostępna książka",
                    "Książka: \"" + r.getBook().getTitle() + "\" jest już dostępna w bibliotece!"
            );
        }
    }

    @FXML
    private void addReviewAction(ActionEvent event) throws IOException {
        LibraryApplication.getAppController().switchScene(SceneType.ADD_REVIEW);
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
