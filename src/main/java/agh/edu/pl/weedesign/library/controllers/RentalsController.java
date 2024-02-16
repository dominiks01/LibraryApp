package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RentalsController extends SubController {

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


    private List<Rental> rentals;
    private Rental selectedRental;

    private ModelService service;
    private BookListProcessor bookListProcessor;

    private RentalModel rentalModel;

    @Autowired
    public RentalsController(ModelService service, BookListProcessor bookListProcessor, RentalModel rentalModel, DataService dataService, MainController mainController){
        super(dataService);
        this.service = service;
        this.bookListProcessor = bookListProcessor;
        this.rentalModel = rentalModel;
    }

    @FXML
    public void initialize(){
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
            return new SimpleStringProperty("Waiting for Acceptance");
        });
        endDateColumn.setCellValueFactory(rentalValue -> {
            LocalDateTime end = rentalValue.getValue().getEnd_date();
            if(end == null)
                if(rentalValue.getValue().getEmployee() != null)
                    return new SimpleStringProperty("Already rented!");
                else
                    return new SimpleStringProperty("Waiting for Acceptance");
            return new SimpleStringProperty(end.toLocalDate().toString());
        });

        rentalsTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                if( getSelectedEntity() != null) {
                    this.selectedRental = getSelectedEntity();
                    this.dataService.setRental(this.selectedRental);
                    try {
                        super.switchScene(SceneType.SINGLE_RENTAL);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void fetchRentalsData(){
        this.rentals = new ArrayList<>(this.service.getRentalsByReader(super.dataService.getReader()));
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

    private Rental getSelectedEntity(){
        return rentalsTable.getSelectionModel().getSelectedItem();
    }

    public Rental getSelectedRental(){
        return this.selectedRental;
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

    public void myRentalsButtonHandler(){
        return;
    }

    public void LogOutAction() throws IOException {
        super.logOutAction();
    }
    public void updateTable(){
        initialize();
    }

}
