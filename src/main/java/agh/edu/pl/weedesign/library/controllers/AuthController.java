package agh.edu.pl.weedesign.library.controllers;

import java.util.HashMap;

import agh.edu.pl.weedesign.library.models.LoginModel;
import agh.edu.pl.weedesign.library.models.RegisterModel;
import agh.edu.pl.weedesign.library.services.ReaderService;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.helpers.Encryptor;
import agh.edu.pl.weedesign.library.helpers.ValidCheck;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


@Component
public class AuthController {

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

    // login view
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private CheckBox employeeLoginCheckBox;

    @FXML 
    private Label signUp;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private Encryptor passwordEncryptor;

    @Autowired
    private RegisterModel registerModel;

    @Autowired
    private LoginModel loginModel;

    @FXML
    private void handleRegisterAction(ActionEvent event){

        // disabling button for security reasons
        registerButton.setDisable(true);
        cancelButton.setDisable(true);
        try {
            registerModel.register(
                new HashMap<>() {
                    {
                        put("nameField", nameField.getText());
                        put("surnameField", surnameField.getText());
                        put("cityField", cityField.getText());
                        put("voivodeshipField", voivodeshipField.getText());
                        put("postcodeField", postcodeField.getText());
                        put("countryField", countryField.getText());
                        put("emailField", emailField.getText());
                        put("passwordField", passwordField.getText());
                        put("phoneField", phoneField.getText());
                        put("birthDateField", String.valueOf(birthDateField.getValue()));
                        put("sexField", sexField.getText());
                    }
                }
            );
        } catch (Exception e ){
            System.out.println(e.getMessage());
            MessageLabel.setText(e.getMessage());
            MessageLabel.setVisible(true);
            return;
        } finally {
            registerButton.setDisable(false);
            cancelButton.setDisable(false);
        }

        Reader newReader = new Reader(nameField.getText(), surnameField.getText(), cityField.getText(),
                voivodeshipField.getText(),postcodeField.getText(),countryField.getText(),emailField.getText(),
                passwordEncryptor.encryptMessage(passwordField.getText()),phoneField.getText(), birthDateField.getValue(),sexField.getText());

        readerService.addNewReader(newReader);
        LibraryApplication.switchScene(SceneType.LOGIN);

    }

    public void handleLoginAction(ActionEvent event) {
        String login = loginField.getText();
        String encryptedUserPassword = passwordEncryptor.encryptMessage(loginPasswordField.getText());
        ValidCheck status = loginModel.login(login, encryptedUserPassword, true);

        if(status.isValid())
            LibraryApplication.switchScene(SceneType.EMPLOYEE_PANEL);

        status = loginModel.login(login, encryptedUserPassword);

        if(status.isValid())
            LibraryApplication.switchScene(SceneType.LOGIN);
        else {
            MessageLabel.setText("Bad login or password");
            MessageLabel.setVisible(true);
        }
    }


    public void signUpButtonClicked(){
        LibraryApplication.switchScene(SceneType.REGISTER);
    }

    @FXML
    private void handleCancelAction(ActionEvent event){
        LibraryApplication.switchScene(SceneType.LOGIN);
    }

    public void signInActionHandle(){
        LibraryApplication.switchScene(SceneType.LOGIN);
    }

    private void printFields(){
        System.out.println("---------------------FORM----------------------");
        System.out.println("Name: " + nameField.getText());
        System.out.println("Surname: " + surnameField.getText());
        System.out.println("Password: " + passwordField.getText());
        System.out.println("Country: " + countryField.getText());
        System.out.println("City: " + cityField.getText());
        System.out.println("Voivodeship: " + voivodeshipField.getText());
        System.out.println("Postcode: " + postcodeField.getText());
        System.out.println("Email: " + emailField.getText());
        System.out.println("Phone: " + phoneField.getText());
        System.out.println("Birth date: " + birthDateField.getValue());
        System.out.println("Sex: " + sexField.getText());
    }
}
