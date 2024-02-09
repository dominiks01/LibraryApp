package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.helpers.Themes;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RentalsController {

    @FXML
    private TableView<Rental> rentalsTable;
    @FXML
    private CheckBox showAllBox;

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

    @Autowired
    public RentalsController(ModelService service, BookListProcessor bookListProcessor, RentalModel rentalModel){
        this.service = service;
        this.bookListProcessor = bookListProcessor;
        this.rentalModel = rentalModel;
    }

    @FXML
    public void initialize(){
        themeChange.getItems().addAll(Themes.getAllThemes());
        themeChange.setOnAction(this::changeTheme);
        themeChange.setValue(LibraryApplication.getTheme());


        fetchRentalsData();

        titleColumn.setCellValueFactory(rentalValue -> new SimpleStringProperty(rentalValue.getValue().getBookCopy().getBook().getTitle()));
        authorColumn.setCellValueFactory(rentalValue -> new SimpleStringProperty(rentalValue.getValue().getBookCopy().getBook().getAuthorString()));
        priceColumn.setCellValueFactory(rentalValue -> {
            if(rentalValue.getValue().getEnd_date() == null)
                return new SimpleStringProperty(rentalValue.getValue().countPrice(LocalDateTime.now()) + " zł");
            return new SimpleStringProperty(rentalValue.getValue().getPrice() + " zł");
        });
        startDateColumn.setCellValueFactory(rentalValue -> {
            if(rentalValue.getValue().getEmployee() != null)
                return new SimpleStringProperty(rentalValue.getValue().getStart_date().toLocalDate().toString());
            return new SimpleStringProperty("---------");
        });
        endDateColumn.setCellValueFactory(rentalValue -> {
            LocalDateTime end = rentalValue.getValue().getEnd_date();
            if(end == null)
                if(rentalValue.getValue().getEmployee() != null)
                    return new SimpleStringProperty("Aktualnie wypożyczona");
                else
                    return new SimpleStringProperty("---------");
            return new SimpleStringProperty(end.toLocalDate().toString());
        });

        rentalsTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                if( getSelectedEntity() != null) {
                    this.selectedRental = getSelectedEntity();
                    System.out.println(this.selectedRental);
                    LibraryApplication.getAppController().switchScene(SceneType.SINGLE_RENTAL);
                }
            }
        });
    }

    private void fetchRentalsData(){
        this.rentals = new ArrayList<>(this.service.getRentalsByReader(LibraryApplication.getReader()));
        //removing history if checkbox is not selected
        boolean history = this.showAllBox.isSelected();
        List<Rental> tmpRentals = new ArrayList<>();
        if(!history){
            for(Rental rental : this.rentals)
                if(rental.getEnd_date() == null)
                    tmpRentals.add(rental);
            this.rentals = tmpRentals;
        }

        rentalsTable.setItems(FXCollections.observableList(this.rentals));
    }

    @FXML
    private void toWelcomeView(ActionEvent actionEvent) {
        LibraryApplication.getAppController().switchScene(SceneType.START_VIEW);
    }

    private Rental getSelectedEntity(){
        return rentalsTable.getSelectionModel().getSelectedItem();
    }

    public Rental getSelectedRental(){
        return this.selectedRental;
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
    public void updateTable(){
        initialize();
    }

}
