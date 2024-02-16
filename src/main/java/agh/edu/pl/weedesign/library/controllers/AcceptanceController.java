package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.services.DataService;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.services.RentalService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Controller
public class AcceptanceController extends SubController {

    @FXML
    private Text titleText;

    @FXML
    private Text authorText;

    @FXML
    private Button acceptRentalButton;

    @FXML
    private Button rejectRentalButton;

    @FXML
    private Button cancelButton;

    private Rental rental;

    private final RentalService rentalService;

    @Autowired
    public AcceptanceController(RentalService rentalService, DataService dataService){
        super(dataService);
        this.rentalService = rentalService;
    }

    @FXML
    public void initialize(){
        this.rental = super.dataService.getRental();
        this.setValues();
    }

    private void setValues(){
        this.titleText.setText(this.rental.getBookCopy().getBook().getTitle());

        StringBuilder authors = new StringBuilder();
        for(Author author : this.rental.getBookCopy().getBook().getAuthors())
            authors.append(author.getName()).append(" ").append(author.getSurname()).append(", ");

        this.authorText.setText(authors.toString());
    }

    public void acceptRental(ActionEvent e) throws IOException {
        this.rental.setEmployee(super.dataService.getEmployee());
        this.rentalService.updateRental(this.rental);

        ((Stage) acceptRentalButton.getScene().getWindow()).close();
    }

    public void rejectRental(ActionEvent e){
        this.rentalService.removeRental(this.rental);
        ((Stage) rejectRentalButton.getScene().getWindow()).close();
    }

    public void backAction(ActionEvent e){
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

}
