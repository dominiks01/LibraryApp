package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;

import javax.xml.crypto.Data;
import java.io.IOException;

public class IController {
    private MainController mainController;
    protected DataService dataService;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return this.mainController;
    }

    protected void switchScene(SceneType sceneType) throws IOException {
        this.mainController.switchScene(sceneType);
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void consumeData() throws IOException {
    }

    public void reload() throws IOException {
    }

    public void logOutAction() throws IOException {
        this.mainController.logOut();
    }

    public void goBack(){
        this.mainController.back();
    }

    public void goForward(){
        this.mainController.forward();
    }
}
