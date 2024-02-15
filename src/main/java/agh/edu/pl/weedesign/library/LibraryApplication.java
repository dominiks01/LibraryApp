package agh.edu.pl.weedesign.library;

import agh.edu.pl.weedesign.library.controllers.IController;
import agh.edu.pl.weedesign.library.controllers.MainController;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
@SpringBootApplication
public class LibraryApplication extends Application {
	private Stage primaryStage;
	private static MainController mainController;
	private static IController currentSceneController;
	private static ConfigurableApplicationContext context;
	private static Reader reader;
	private static Book book;
	private static Employee employee;
	private static String theme = "Nord Dark";
	private static ArrayList<Object> filterStrategy = null;
	private static final SceneFactory factory = new SceneFactory();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		Application.setUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/themes/nord-dark.css")).toExternalForm());
		SpringApplicationBuilder builder = new SpringApplicationBuilder(LibraryApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);

		context = builder.run();
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Library App");

		mainController = new MainController(this.primaryStage, context);

		FXMLLoader loader = factory.createScene(SceneType.LOGIN);
        Parent root = Objects.requireNonNull(loader).load();

		currentSceneController = loader.getController();
		currentSceneController.setMainController(mainController);
		mainController.setCurrentSceneController(currentSceneController);

		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static ConfigurableApplicationContext getAppContext(){
		return context;
	}

	public static MainController getAppController(){
		return mainController;
	}

	public static String getTheme(){
		return theme;
	}

	public static void changeTheme(String newTheme){

		theme = newTheme;

		String pathToCss = "/themes/" + switch(theme){
			case "Cupertino Dark" -> "cupertino-dark.css";
			case "Cupertino Light" -> "cupertino-light.css";
			case "Dracula" -> "dracula.css";
			case "Nord Dark" -> "nord-dark.css";
			case "Nord Light" -> "nord-light.css";
			case "Primer Dark" -> "primer-dark.css";
			case "Primer Light" -> "primer-light.css";
			default -> "nord_dark.css";
		};

		Application.setUserAgentStylesheet(LibraryApplication.class.getResource(pathToCss).toExternalForm());
	}

	public static void saveStrategy(ArrayList<Object> strategy){
		filterStrategy = strategy;
	}

	public static ArrayList<Object> getStrategy(){
		return filterStrategy;
	}
}
