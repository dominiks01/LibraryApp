package agh.edu.pl.weedesign.library.controllers;
import java.util.ArrayList;

import org.springframework.context.ConfigurableApplicationContext;
import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LibraryAppController {
    private Stage primaryStage;
    private Scene currentScene;
    private SceneFactory factory;

    private ArrayList<Parent> undo_stack; 
    private ArrayList<Parent> next_stack; 


    ConfigurableApplicationContext springContext;

    public LibraryAppController(Stage stage, ConfigurableApplicationContext springContext){
        this.primaryStage = stage;
        this.springContext = springContext;
        factory = new SceneFactory();

        undo_stack = new ArrayList<>(10);
        next_stack = new ArrayList<>(10);
    }

    public void initWelcomeLayout() {
        this.primaryStage.setTitle("Biblioteka");
        currentScene = new Scene(factory.createScene(SceneType.LOGIN));

        LibraryApplication.getAppController().resize(1000, 800);
        primaryStage.setScene(currentScene);
        primaryStage.show();
    }

    public void switchScene(SceneType sceneType){

        if(undo_stack.size() == 10)
            undo_stack.remove(0);

        undo_stack.add(currentScene.getRoot());
//         currentScene.setUserData(currentScene.getRoot());

        currentScene.setRoot(factory.createScene(sceneType));
        LibraryApplication.getAppController().resize(1000, 800);
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

    public void logOut(){
        switchScene(SceneType.LOGIN);
        LibraryApplication.setEmployee(null);
        LibraryApplication.setReader(null);
    }

}
