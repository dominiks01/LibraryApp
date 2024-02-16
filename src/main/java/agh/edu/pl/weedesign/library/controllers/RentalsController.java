package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
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

    private final RentalModel rentalModel;

    @Autowired
    public RentalsController(RentalModel rentalModel, DataService dataService){
        super(dataService);
        this.rentalModel = rentalModel;
    }


    @FXML
    public void initialize() {
        reload();

        titleColumn.setCellValueFactory(rentalValue -> new SimpleStringProperty(rentalValue.getValue().getBookCopy().getBook().getTitle()));
        authorColumn.setCellValueFactory(rentalValue -> new SimpleStringProperty(rentalValue.getValue().getBookCopy().getBook().getAuthorString()));

        priceColumn.setCellValueFactory(rentalValue -> {
            if(rentalValue.getValue().getEndDate() == null)
                return new SimpleStringProperty(rentalValue.getValue().countPrice(LocalDateTime.now()) + " zł");
            return new SimpleStringProperty(rentalValue.getValue().getPrice() + " zł");
        });

        startDateColumn.setCellValueFactory(rentalValue -> {
            if(rentalValue.getValue().getEmployee() != null)
                return new SimpleStringProperty(rentalValue.getValue().getStartDate().toLocalDate().toString());
            return new SimpleStringProperty("Waiting for Acceptance");
        });

        endDateColumn.setCellValueFactory(rentalValue -> {
            LocalDateTime end = rentalValue.getValue().getEndDate();
            if(end == null)
                if(rentalValue.getValue().getEmployee() != null)
                    return new SimpleStringProperty("Already rented!");
                else
                    return new SimpleStringProperty("Waiting for Acceptance");
            return new SimpleStringProperty(end.toLocalDate().toString());
        });


        rentalsTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Rental selectedRental = getSelectedEntity();

                if( selectedRental != null) {
                    this.dataService.setRental(selectedRental);
                    try {
                        super.switchScene(SceneType.SINGLE_RENTAL);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @FXML
    private void showAllRentals(){
        reload();
    }

    private Rental getSelectedEntity(){
        return rentalsTable.getSelectionModel().getSelectedItem();
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

    @Override
    public void reload(){
        this.rentals = rentalModel.getReaderRentals(dataService.getReader())
                .stream()
                .filter(x -> x.getEndDate() == null ||  !showAllBox.isSelected())
                .toList();

        rentalsTable.setItems(FXCollections.observableList(this.rentals));
    }
}
