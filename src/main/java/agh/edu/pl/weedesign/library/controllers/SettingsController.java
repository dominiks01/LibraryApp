package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.Themes;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.RentalService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class SettingsController extends SubController {


    @FXML
    private GridPane settingGridPane;


    @Autowired
    public SettingsController(DataService dataService){
        super(dataService);
    }

    @FXML
    public void initialize(){
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

    public void userConfigurationAction(){
        return;
    }

    public void generalSettingsAction(){
        ChoiceBox<String> themeChange = new ChoiceBox<>();
        themeChange.getItems().addAll(Themes.getAllThemes());
        themeChange.setOnAction(x ->  LibraryApplication.changeTheme(themeChange.getValue()));
        themeChange.setValue(LibraryApplication.getTheme());

        settingGridPane.add(themeChange, 0 , 0);

    }

}
