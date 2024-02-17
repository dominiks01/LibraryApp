package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RentalsAcceptanceController extends SubController {

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

    private List<Rental> rentals;

    private final SceneFactory factory = new SceneFactory();

    private final RentalModel rentalModel;

    @Autowired
    public RentalsAcceptanceController(RentalModel rentalModel, DataService dataService){
        super(dataService);
        this.rentalModel = rentalModel;
    }

    @FXML
    public void initialize(){
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
                if( getSelectedEntity() != null) {
                    try {
                        super.dataService.setRental(getSelectedEntity());
                        initialize_acceptance_view();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void initialize_acceptance_view() throws IOException {
        FXMLLoader loader = factory.createScene(SceneType.ACCEPTANCE);

        assert loader != null;
        Parent root = loader.load();

        SubController currentSceneController =loader.getController();
        currentSceneController.setMainController(super.getMainController());

        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root, 300, 300));

        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, x -> this.reload());
        stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, x -> this.reload());
        stage.show();
    }

    @Override
    public void reload(){
        this.rentals = rentalModel.getRentalsWithoutAcceptance();
        rentalsTable.getItems().clear();
        rentalsTable.setItems(FXCollections.observableList(this.rentals));
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

    public void settingsButtonAction() throws IOException {
        switchScene(SceneType.SETTINGS);
    }

    public void mainPageButtonHandler() throws IOException {
        super.switchScene(SceneType.EMPLOYEE_PANEL);
    }

    public void LogOutAction() throws IOException {
        super.logOutAction();
    }

}
