package agh.edu.pl.weedesign.library.controllers;
import java.io.IOException;
import java.util.Stack;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class MainController {
    private Stage primaryStage;
    public SceneFactory factory;

    private final Stack<SceneType> undoStack = new Stack<>();
    private final Stack<SceneType> nextStack= new Stack<>();

    private DataService dataService;

    public MainController(DataService dataService) throws IOException {
        this.dataService = dataService;
    }

    @PostConstruct
    public void init() throws IOException {
    }

    public void switchScene(SceneType sceneType) throws IOException {
        loadNewScene(sceneType);
        undoStack.add(sceneType);
        nextStack.clear();
    }

    public void loadNewScene(SceneType sceneType) throws IOException {
        FXMLLoader loader = SceneFactory.getInstance().createScene(sceneType);

        assert loader != null;
        Parent root = loader.load();

        SubController currentSceneController = loader.getController();
        currentSceneController.setMainController(this);

        if(this.primaryStage == null){
            this.primaryStage = new Stage();
            this.primaryStage.setScene(new Scene(root));
            this.resize(1320, 950);
            this.primaryStage.show();
        }
        else {
            this.primaryStage.setScene(new Scene(root));
            this.resize(1320, 950);
        }

    }

    public void resize(int width, int height){
        this.primaryStage.setWidth(width);
        this.primaryStage.setHeight(height);
    }


    public void back() {
        try {
            SceneType last_scene = undoStack.pop();

            if(nextStack.empty())
                last_scene = undoStack.pop();

            nextStack.add(last_scene);
            loadNewScene(last_scene);

        } catch (Exception e){
            return;
        }
    }

    public void forward(){
        try {
            switchScene(undoStack.pop());
        } catch (Exception e){
            return;
        }
    }


    public void logOut() throws IOException {
        switchScene(SceneType.LOGIN);
    }

}
