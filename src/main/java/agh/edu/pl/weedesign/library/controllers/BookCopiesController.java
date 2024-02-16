package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.reservation.Reservation;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.models.BookCopiesModel;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookCopiesController extends SubController {

    public Button reserve;

    @FXML
    private TableView<BookCopy> bookTable;

    @FXML
    private TableColumn<BookCopy, String> idColumn;

    @FXML
    private TableColumn<BookCopy, String> conditionColumn;

    @FXML
    private TableColumn<BookCopy, String> priceColumn;

    @FXML
    private TableColumn<BookCopy, String> availabilityColumn;

    // Navbar controls
    @FXML
    private Button mainPage;

    @FXML
    private Button myRentals;

    @FXML
    private Button logOut;

    @FXML
    private ChoiceBox<String> themeChange;

    private List<BookCopy> bookCopies;

    private Book book;

    private ModelService service;

    private final RentalModel rentalModel;

    private final BookCopiesModel bookCopiesModel;


    @Autowired
    public BookCopiesController(RentalModel rentalModel, DataService dataService, BookCopiesModel bookCopiesModel){
        super(dataService);
        this.bookCopiesModel = bookCopiesModel;
        this.rentalModel = rentalModel;
    }

    @FXML
    public void initialize(){
        fetchCopiesData();

        idColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(String.valueOf(copyValue.getValue().getId())));
        conditionColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(copyValue.getValue().getCondition()));
        priceColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(copyValue.getValue().getWeekUnitPrice() + " zł"));

        availabilityColumn.setCellValueFactory(copyValue -> {

            for(Rental rental : bookCopiesModel.getRentalsByBook(copyValue.getValue().getBook()))
                if(rental.getEndDate() == null)
                    return new SimpleStringProperty("Not Available");

            return new SimpleStringProperty("Available");
        });

        bookTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                BookCopy toRent = bookTable.getSelectionModel().getSelectedItem();
                boolean canBeRented = true;

                for(Rental rental : bookCopiesModel.getRentalsByBookCopy(toRent))
                    if(rental.getEndDate() == null){
                        canBeRented = false;
                        break;
                    }

                if(canBeRented){
                    this.rentalModel.rentBook(toRent);
                    try {
                        super.switchScene(SceneType.RENTALS_VIEW);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void fetchCopiesData(){
        this.book = super.dataService.getSelectedBook();
        this.bookCopies = new ArrayList<>(this.bookCopiesModel.getBookCopies(this.book));
        bookTable.setItems(FXCollections.observableList(this.bookCopies));
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

    public void reserveBook(ActionEvent actionEvent) {
        if (this.service.getReservationByBookAndReader(this.book, super.dataService.getReader()) != null){
            System.out.println("Reservation exist");
            return;
        }
        for (BookCopy cpy: this.bookCopies){
            if (this.availabilityColumn.getCellObservableValue(cpy).getValue().equals("Dostępna")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Książka dostępna");
                alert.setContentText("Książka jest dostępna do wypożyczenia");
                alert.show();
                return;
            }
        }
        Reservation r = new Reservation( super.dataService.getReader(), LocalDateTime.now(), this.book);
        this.service.addNewReservation(r);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Książka zarezerwowana");
        alert.setContentText("Książka została zarezerwowana i dostaniesz powiadomienie jeśli będzie dostępna");
        alert.show();
    }
}
