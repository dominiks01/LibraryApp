package agh.edu.pl.weedesign.library.controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.springframework.context.ConfigurableApplicationContext;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainController {
    private Stage primaryStage;
    private Scene currentScene;
    public SceneFactory factory;

    private ArrayList<Parent> undo_stack; 
    private ArrayList<Parent> next_stack; 

    ConfigurableApplicationContext springContext;
    private IController currentSceneController;

    private DataService dataService;

    public MainController(Stage stage, ConfigurableApplicationContext springContext){
        this.primaryStage = stage;
        this.springContext = springContext;
        factory = new SceneFactory();

        dataService = new DataService();

        undo_stack = new ArrayList<>(10);
        next_stack = new ArrayList<>(10);
        this.resize(1300, 950);
    }

    public void setCurrentSceneController(IController currentSceneController){
       this.currentSceneController = currentSceneController;
    }

    public void switchScene(SceneType sceneType) throws IOException {
        FXMLLoader loader = factory.createScene(sceneType);
        Parent root = loader.load();

        currentSceneController = loader.getController();
        currentSceneController.setDataService(this.dataService);
        currentSceneController.consumeData();
        currentSceneController.setMainController(this);
        setCurrentSceneController(currentSceneController);

        primaryStage.setScene(new Scene(root));
        this.resize(1300, 950);
    }

    public void saveData(Object obj){
        this.currentScene.setUserData(obj);
    }

    public Object getData(){
        return this.currentScene.getUserData();
    }

    public void resize(int width, int height){
        this.primaryStage.setWidth(width);
        this.primaryStage.setHeight(height);
    }

    public void back(){
        if(next_stack.size() == 10)
            next_stack.remove(0);

        next_stack.add(currentScene.getRoot());
        currentScene.setRoot((Parent)undo_stack.get(undo_stack.size() - 1));
    }

    public void forward(){
        if(next_stack.size() == 0)
            return; 
        
        undo_stack.add(currentScene.getRoot());
        currentScene.setRoot((Parent)next_stack.get(next_stack.size() - 1));
    }

    public void reload() throws IOException {
        this.currentSceneController.reload();
        this.currentSceneController.consumeData();
    }


    public void logOut() throws IOException {
        switchScene(SceneType.LOGIN);
        dataService.setEmployee(null);
        dataService.setReader(null);
    }

}
