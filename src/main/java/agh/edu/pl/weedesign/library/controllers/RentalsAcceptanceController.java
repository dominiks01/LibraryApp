package agh.edu.pl.weedesign.library.controllers;


import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.BookListProcessor;
import agh.edu.pl.weedesign.library.helpers.Themes;
import agh.edu.pl.weedesign.library.models.RentalModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import agh.edu.pl.weedesign.library.services.RentalService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.crypto.Mac;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RentalsAcceptanceController extends IController {

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

    private ModelService service;
    private BookListProcessor bookListProcessor;

    private final SceneFactory factory = new SceneFactory();

    @Autowired
    public RentalsAcceptanceController(ModelService service){
        this.service = service;
    }

    @Override
    public void consumeData(){
        fetchRentalsData();

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

        IController currentSceneController =loader.getController();
        currentSceneController.setDataService(this.dataService);
        currentSceneController.consumeData();
        currentSceneController.setMainController(super.getMainController());

        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root, 300, 300));
        stage.show();

    }

    @FXML
    public void initialize(){

    }

    private void fetchRentalsData(){
        this.rentals = new ArrayList<>(this.service.getRentalsWithoutAcceptance());
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

    public void mainPageButtonHandler() throws IOException {
        super.switchScene(SceneType.EMPLOYEE_PANEL);
    }


    public void LogOutAction() throws IOException {
        super.logOutAction();
    }

    @Override
    public void reload(){
        rentals.clear();
        consumeData();
    }

}
