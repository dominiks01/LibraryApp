package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.helpers.Themes;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import agh.edu.pl.weedesign.library.services.RentalService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RentalsAcceptanceController {

    @FXML
    private TableView<Rental> rentalsTable;

    @FXML
    private TableColumn<Rental, String> titleColumn;

    @FXML
    private TableColumn<Rental, String> authorColumn;

    @FXML
    private TableColumn<Rental, String> priceColumn;

    @FXML
    private TableColumn<Rental, String> startDateColumn;

    @FXML
    private TableColumn<Rental, String> endDateColumn;

     // Navbar controls 
    @FXML
    private Button mainPage; 

    @FXML
    private Button myRentals; 

    @FXML
    private Button logOut; 

    @FXML 
    private ChoiceBox<String> themeChange;



    private List<Rental> rentals;
    private Rental selectedRental;

    private ModelService service;
    private BookListProcessor bookListProcessor;

    private RentalModel rentalModel;
    private RentalService rentalService;

    @Autowired
    public RentalsAcceptanceController(ModelService service, BookListProcessor bookListProcessor, RentalModel rentalModel, RentalService rentalService){
        this.service = service;
        this.bookListProcessor = bookListProcessor;
        this.rentalModel = rentalModel;
        this.rentalService = rentalService;
    }

    @FXML
    public void initialize(){
        fetchRentalsData();

        themeChange.getItems().addAll(Themes.getAllThemes());
        themeChange.setOnAction(this::changeTheme);
        themeChange.setValue(LibraryApplication.getTheme());

        LibraryApplication.getAppController().resize(760, 440);

        titleColumn.setCellValueFactory(rentalValue -> new SimpleStringProperty(rentalValue.getValue().getBookCopy().getBook().getTitle()));
        authorColumn.setCellValueFactory(rentalValue -> {
                String authors = "";
                for(Author author : rentalValue.getValue().getBookCopy().getBook().getAuthors())
                    authors += author.getName() + " " + author.getSurname() + "   ";
                return new SimpleStringProperty(authors);
        });
        priceColumn.setCellValueFactory(rentalValue -> {
            if(rentalValue.getValue().getEnd_date() == null)
                return new SimpleStringProperty(rentalValue.getValue().countPrice(LocalDateTime.now()) + " zł");
            return new SimpleStringProperty(rentalValue.getValue().getPrice() + " zł");
        });
        startDateColumn.setCellValueFactory(rentalValue -> new SimpleStringProperty(rentalValue.getValue().getStart_date().toLocalDate().toString()));
        endDateColumn.setCellValueFactory(rentalValue -> {
            LocalDateTime end = rentalValue.getValue().getEnd_date();
            if(end == null)
                return new SimpleStringProperty("Aktualnie wypożyczona");
            return new SimpleStringProperty(end.toLocalDate().toString());
        });

        rentalsTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                if( getSelectedEntity() != null) {
                    LibraryApplication.getAppController().saveData(getSelectedEntity());
                    LibraryApplication.getAppController().switchScene(SceneType.ACCEPTANCE);
                }
            }
        });
    }

    private void fetchRentalsData(){
        this.rentals = new ArrayList<>(this.service.getRentalsWithoutAcceptance());
        rentalsTable.setItems(FXCollections.observableList(this.rentals));
    }

    @FXML
    private void toPanelView(ActionEvent actionEvent) {
        LibraryApplication.getAppController().switchScene(SceneType.EMPLOYEE_PANEL);
    }

    private Rental getSelectedEntity(){
        return rentalsTable.getSelectionModel().getSelectedItem();
    }

    public void goBackAction(){
        LibraryApplication.getAppController().back();
    }

    public void goForwardAction(){
        LibraryApplication.getAppController().forward();
    }

    public void mainPageButtonHandler(){
        LibraryApplication.getAppController().switchScene(SceneType.EMPLOYEE_PANEL); 
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

}
