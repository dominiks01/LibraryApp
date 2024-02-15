package agh.edu.pl.weedesign.library.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import agh.edu.pl.weedesign.library.models.LoginModel;
import agh.edu.pl.weedesign.library.models.RegisterModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.services.ReaderService;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.helpers.Encryptor;
import agh.edu.pl.weedesign.library.helpers.ValidCheck;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

@Component
public class LoginController extends IController {

    // login view
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Label signUp;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;

    private Encryptor passwordEncryptor;

    private LoginModel loginModel;

    @Autowired
    public void LoginController(LoginModel loginModel, Encryptor passwordEncryptor){
        this.loginModel = loginModel;
        this.passwordEncryptor = passwordEncryptor;
    }

    @FXML
    public void handleLoginAction(ActionEvent event) throws IOException {
        String encryptedUserPassword = passwordEncryptor.encryptMessage(loginPasswordField.getText());

        ValidCheck status = loginModel.login( loginField.getText(), encryptedUserPassword, true);

        if(status.isValid()) {
            super.switchScene(SceneType.EMPLOYEE_PANEL);
            return;
        }

        status = loginModel.login( loginField.getText(), encryptedUserPassword);

        if(status.isValid()) {
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