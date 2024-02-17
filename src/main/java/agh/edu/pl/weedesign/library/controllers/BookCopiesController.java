package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.reservation.Reservation;
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

    private List<BookCopy> bookCopies;

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
        loadData();

        idColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(String.valueOf(copyValue.getValue().getId())));
        conditionColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(copyValue.getValue().getCondition()));
        priceColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(copyValue.getValue().getWeekUnitPrice() + " zł"));
        availabilityColumn.setCellValueFactory(copyValue -> new SimpleStringProperty("Available"));

        bookTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    this.rentalModel.rentBook(bookTable.getSelectionModel().getSelectedItem(), dataService.getReader());
                    try {
                        super.switchScene(SceneType.RENTALS_VIEW);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        });
    }

    private void loadData(){
        bookTable.setItems(FXCollections.observableList(this.bookCopiesModel.getBookCopies(super.dataService.getSelectedBook())));
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


    public void reserveBook(ActionEvent actionEvent) {

        if (this.service.getReservationByBookAndReader(dataService.getSelectedBook(), super.dataService.getReader()) != null){
            System.out.println("Reservation exist");
            return;
        }

        Reservation r = new Reservation( super.dataService.getReader(), LocalDateTime.now(), dataService.getSelectedBook());
        this.service.addNewReservation(r);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Książka zarezerwowana");
        alert.setContentText("Książka została zarezerwowana i dostaniesz powiadomienie jeśli będzie dostępna");
        alert.show();
    }
}
