package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;

import agh.edu.pl.weedesign.library.models.RegisterModel;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


@Component
public class RegisterController extends SubController {

    // registration view
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField voivodeshipField;

    @FXML
    private TextField postcodeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private DatePicker birthDateField;

    @FXML
    private TextField sexField;

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label signInLabel;

    @FXML
    private Label MessageLabel;


    private final RegisterModel registerModel;

    @Autowired
    public RegisterController(RegisterModel registerModel, DataService dataService, MainController mainController){
        super(dataService);
        this.registerModel = registerModel;
    }

    @FXML
    private void handleRegisterAction(ActionEvent event) throws IOException {

        // disabling button for security reasons
        registerButton.setDisable(true);
        cancelButton.setDisable(true);
        
        try {
            registerModel.setName(nameField.getText());
            registerModel.setSurname(surnameField.getText());
            registerModel.setCity(cityField.getText());
            registerModel.setVoivodeship(voivodeshipField.getText());
            registerModel.setPostcode(postcodeField.getText());
            registerModel.setCountry(countryField.getText());
            registerModel.setEmail(emailField.getText());
            registerModel.setPassword(passwordField.getText());
            registerModel.setPhone(phoneField.getText());
            registerModel.setBirthDate(birthDateField.getValue());
            registerModel.setSex( sexField.getText());

            registerModel.register();

        } catch (Exception e ){
            System.out.println(e.getMessage());
            MessageLabel.setText(e.getMessage());
            MessageLabel.setVisible(true);
            return;
        } finally {
            registerButton.setDisable(false);
            cancelButton.setDisable(false);
        }

        super.switchScene(SceneType.LOGIN);

    }

    @FXML
    private void handleCancelAction(ActionEvent event) throws IOException {
        super.switchScene(SceneType.LOGIN);
    }

    public void signInActionHandle() throws IOException {
        super.switchScene(SceneType.LOGIN);
    }

}
