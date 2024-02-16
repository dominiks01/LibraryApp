package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;
import java.util.List;

import agh.edu.pl.weedesign.library.models.NewEmployeeModel;
import agh.edu.pl.weedesign.library.services.DataService;
import agh.edu.pl.weedesign.library.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agh.edu.pl.weedesign.library.entities.employee.AccessLevel;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.helpers.Encryptor;
import agh.edu.pl.weedesign.library.helpers.ValidCheck;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

@Component
public class AddEmployeeController extends SubController {

    @FXML
    public TextField emailInput;

    @FXML
    public TextField salaryInput;

    @FXML
    public TextField surnameInput;

    @FXML
    public TextField nameInput;

    @FXML
    public TextField passwordInput;

    @FXML
    public Button addButton;

    @FXML
    public Button cancelButton;

    @FXML
    private ChoiceBox<Employee> reportsToChoiceBox;

    @FXML
    private ChoiceBox<AccessLevel> accessLevelChoiceBox;

    private final NewEmployeeModel modelService;

    @Autowired
    public AddEmployeeController(NewEmployeeModel modelService, DataService dataService) {
        super(dataService);
        this.modelService = modelService;
    }

    @FXML
    private void initialize() {
        List<Employee> employees = this.modelService.getEmployees();
        reportsToChoiceBox.setItems(FXCollections.observableArrayList(employees));
        accessLevelChoiceBox.setItems(FXCollections.observableArrayList(AccessLevel.values()));
    }

    @FXML
    private void handleAddAction() throws IOException {
        try {
            this.modelService.setName(nameInput.getText());
            this.modelService.setSurname(surnameInput.getText());
            this.modelService.setSalary(salaryInput.getText());
            this.modelService.setEmail(emailInput.getText());
            this.modelService.setPassword(passwordInput.getText());
            this.modelService.setSupervisor(reportsToChoiceBox.getValue());
            this.modelService.setPermissions(accessLevelChoiceBox.getValue());

            this.modelService.register();


        } catch (IllegalArgumentException e ){
            System.out.println(e.getMessage());
            return;
        }

        super.switchScene(SceneType.EMPLOYEE_PANEL);
    }


    @FXML
    private void handleCancelAction() throws IOException {
        super.switchScene(SceneType.EMPLOYEE_PANEL);
    }

    public void goBackAction() throws IOException {
        super.goBack();
    }

    public void goForwardAction() throws IOException {
        super.goForward();
    }

    public void mainPageButtonHandler() throws IOException {
        super.switchScene(SceneType.EMPLOYEE_PANEL);
    }

    public void LogOutAction() throws IOException {
        super.logOutAction();
    }

}
