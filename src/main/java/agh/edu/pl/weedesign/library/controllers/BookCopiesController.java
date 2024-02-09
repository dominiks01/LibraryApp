package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.reservation.Reservation;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.helpers.Themes;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookCopiesController {

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
    private BookListProcessor bookListProcessor;

    private RentalModel rentalModel;

    @Autowired
    public BookCopiesController(ModelService service, BookListProcessor bookListProcessor, RentalModel rentalModel){
        this.service = service;
        this.bookListProcessor = bookListProcessor;
        this.rentalModel = rentalModel;
    }

    @FXML
    public void initialize(){

        themeChange.getItems().addAll(Themes.getAllThemes());
        themeChange.setOnAction(this::changeTheme);
        themeChange.setValue(LibraryApplication.getTheme());

        fetchCopiesData();

        idColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(String.valueOf(copyValue.getValue().getId())));
        conditionColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(copyValue.getValue().getCondition()));
        priceColumn.setCellValueFactory(copyValue -> new SimpleStringProperty(copyValue.getValue().getWeek_unit_price() + " zł"));
        availabilityColumn.setCellValueFactory(copyValue -> {

            for(Rental rental : service.getRentalsByBookCopy(copyValue.getValue()))
                if(rental.getEnd_date() == null)
                    return new SimpleStringProperty("Aktualnie wypożyczona");
            return new SimpleStringProperty("Dostępna");
        });


        bookTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                BookCopy toRent = bookTable.getSelectionModel().getSelectedItem();
                boolean canBeRented = true;
                for(Rental rental : service.getRentalsByBookCopy(toRent))
                    if(rental.getEnd_date() == null){
                        canBeRented = false;
                        break;
                    }
                if(canBeRented){
                    this.rentalModel.rentBook(toRent);
                    LibraryApplication.getAppController().switchScene(SceneType.RENTALS_VIEW);
                }
            }
        });
    }

    private void fetchCopiesData(){
        this.book = LibraryApplication.getBook();
        this.bookCopies = new ArrayList<>(this.service.getCopies(this.book));
        bookTable.setItems(FXCollections.observableList(this.bookCopies));
    }

    public void goBackAction(){
        LibraryApplication.getAppController().back();
    }

    public void goForwardAction(){
        LibraryApplication.getAppController().forward();
    }

    public void mainPageButtonHandler(){
        LibraryApplication.getAppController().switchScene(SceneType.BOOK_LIST); 
    }

    public void myRentalsButtonHandler(){
        LibraryApplication.getAppController().switchScene(SceneType.RENTALS_VIEW); 
    }

    public void changeTheme(ActionEvent e){
        LibraryApplication.changeTheme(themeChange.getValue());
    }

    public void LogOutAction(){
        LibraryApplication.getAppController().logOut();
    }

    public void reserveBook(ActionEvent actionEvent) {
        if (this.service.getReservationByBookAndReader(this.book, LibraryApplication.getReader()) != null){
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
        Reservation r = new Reservation(LibraryApplication.getReader(), LocalDateTime.now(), this.book);
        this.service.addNewReservation(r);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Książka zarezerwowana");
        alert.setContentText("Książka została zarezerwowana i dostaniesz powiadomienie jeśli będzie dostępna");
        alert.show();
    }
}
