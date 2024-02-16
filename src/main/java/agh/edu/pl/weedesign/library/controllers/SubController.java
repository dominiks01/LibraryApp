package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.io.IOException;

public class SubController {
    private MainController mainController;
    protected DataService dataService;

    @Autowired
    public SubController(DataService dataService){
        this.dataService = dataService;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return this.mainController;
    }

    protected void switchScene(SceneType sceneType) throws IOException {
        this.mainController.switchScene(sceneType);
    }

    public void reinitialize(){

    }


    public void consumeData() throws IOException {
    }

    protected void reload() throws IOException {
        this.mainController.reload();
    }

    protected void logOutAction() throws IOException {
        this.mainController.logOut();
    }

    protected void goBack(){
        this.mainController.back();
    }

    protected void goForward(){
        this.mainController.forward();
    }
}
