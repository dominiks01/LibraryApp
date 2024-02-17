package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.entities.employee.AccessLevel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class EmployeePanelController extends SubController {
    public Text errorMsg;
    @FXML
    private Button addEmployee;
    @FXML
    private Button addNewBook;
    @FXML
    private Button showStatsButton;


    // Navbar controls 
     @FXML
     private Button mainPage; 
 
     @FXML
     private Button myRentals; 
 
     @FXML
     private Button logOut; 
 
     @FXML 
     private ChoiceBox<String> themeChange;

     @Autowired
     public EmployeePanelController(DataService dataService, MainController mainController) {
        super(dataService);
     }

    @FXML
     public void initialize(){
    }

    public void handleAddNewBookAction(ActionEvent actionEvent) throws IOException {
        if (super.dataService.getEmployee().getAccessLevel().ordinal() >= AccessLevel.EMPLOYEE.ordinal()){
            super.switchScene(SceneType.NEW_BOOK_VIEW);
        }
        else{
            errorMsg.setText("No permission for adding new book");
        }
    }

    public void handleAddEmployeeAction(ActionEvent actionEvent) throws IOException {
        if (super.dataService.getEmployee().getAccessLevel().ordinal() >= AccessLevel.MANAGER.ordinal()){
            super.switchScene(SceneType.ADD_EMPLOYEE);
        }
        else{
            errorMsg.setText("No permission for adding new employee");
        }
    }

    public void showStats(ActionEvent actionEvent) throws IOException {
        super.switchScene(SceneType.STATS_VIEW);
    }

    public void acceptRental(ActionEvent actionEvent) throws IOException {
        super.switchScene(SceneType.RENTALS_ACCEPTANCE);
    }

    public void LogOutAction(ActionEvent e) throws IOException {
        super.dataService.setEmployee(null);
        super.switchScene(SceneType.LOGIN);
    }

    public void settingsButtonAction() throws IOException {
        switchScene(SceneType.SETTINGS);
    }

    public void goBackAction(){
        super.goBack();
    }

    public void goForwardAction(){
        super.goBack();
    }

    public void mainPageButtonHandler(){
        return;
    }

}
