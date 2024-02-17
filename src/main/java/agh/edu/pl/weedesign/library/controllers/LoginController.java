package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;

import agh.edu.pl.weedesign.library.models.LoginModel;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import agh.edu.pl.weedesign.library.helpers.Encryptor;
import agh.edu.pl.weedesign.library.helpers.ValidCheck;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

@Component
public class LoginController extends SubController {

    // login view
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Label messageLabel;

    private Encryptor passwordEncryptor;

    private LoginModel loginModel;

    @Autowired
    public LoginController(LoginModel loginModel, Encryptor passwordEncryptor, DataService dataService){
        super(dataService);
        this.loginModel = loginModel;
        this.passwordEncryptor = passwordEncryptor;
    }

    @FXML
    public void initialize(){
        this.loginModel.setDataService(super.dataService);
    }

    @FXML
    public void handleLoginAction(ActionEvent event) throws IOException {
        String encryptedUserPassword = passwordEncryptor.encryptMessage(loginPasswordField.getText());

        ValidCheck status = loginModel.login( loginField.getText(), encryptedUserPassword, true);

        System.out.println(status);

        if(status.isValid()) {
            super.switchScene(SceneType.EMPLOYEE_PANEL);
            return;
        }

        status = loginModel.login( loginField.getText(), encryptedUserPassword);

        if(status.isValid()) {
            System.out.println(dataService.getReader().getEmail());
           super.switchScene(SceneType.BOOK_LIST);
        } else {
            messageLabel.setText("Bad login or password");
            messageLabel.setVisible(true);
        }
    }

    public void signUpButtonClicked() throws IOException {
        super.switchScene(SceneType.REGISTER);
    }
}