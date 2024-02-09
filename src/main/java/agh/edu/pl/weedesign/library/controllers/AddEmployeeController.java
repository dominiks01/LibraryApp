package agh.edu.pl.weedesign.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agh.edu.pl.weedesign.library.LibraryApplication;
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
public class AddEmployeeController {
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

    @Autowired
    private Encryptor passwordEncryptor;
    
    @Autowired
    private ModelService service;

    private final ModelService modelService;
    private final AuthController authController;

    public AddEmployeeController(ModelService modelService, AuthController authController) {
        this.modelService = modelService;
        this.authController = authController;
    }
    @FXML
    private void initialize() {
        List<Employee> employees = this.modelService.getEmployees();
        reportsToChoiceBox.setItems(FXCollections.observableArrayList(employees));
        accessLevelChoiceBox.setItems(FXCollections.observableArrayList(AccessLevel.values()));
    }
    @FXML
    private void handleAddAction(){
        addButton.setDisable(true);
        cancelButton.setDisable(true);
        try {
//            checkerGuard(checker.isRegisterNameValid(nameInput.getText()));
//            checkerGuard(checker.isRegisterSurnameValid(nameInput.getText()));
//            checkerGuard(checker.isRegisterEmailValid(emailInput.getText()));
//            checkerGuard(checker.isRegisterPasswordValid(passwordInput.getText()));
//            checkerGuard(checker.isSalaryValid(salaryInput.getText()));
        } catch (IllegalArgumentException e ){
            System.out.println(e.getMessage());
            return;
        }
        finally {
            addButton.setDisable(false);
            cancelButton.setDisable(false);
        }

        Employee newEmployee = new Employee(nameInput.getText(), surnameInput.getText(), Integer.parseInt(salaryInput.getText()),
                emailInput.getText(), passwordEncryptor.encryptMessage(passwordInput.getText()), accessLevelChoiceBox.getValue());

        service.addNewEmployee(newEmployee);
        hopToNextScene(SceneType.EMPLOYEE_PANEL);
    }

    public void hopToNextScene(SceneType sceneType){
        LibraryApplication.getAppController().switchScene(sceneType);
    }

    public void checkerGuard(ValidCheck check){
        if (!check.isValid()){
            throw new IllegalArgumentException(check.message());
        }
    }
    @FXML
    private void handleCancelAction(){
        LibraryApplication.getAppController().switchScene(SceneType.EMPLOYEE_PANEL);
    }
}
