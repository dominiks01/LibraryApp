package agh.edu.pl.weedesign.library;

import agh.edu.pl.weedesign.library.controllers.SubController;
import agh.edu.pl.weedesign.library.controllers.MainController;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.helpers.BookFilterStrategy;
import agh.edu.pl.weedesign.library.sceneObjects.SceneFactory;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.DataService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Service
public class LibraryApplication extends Application {
	private DataService dataService;

	private static ConfigurableApplicationContext context;
	private static String theme = "Nord Dark";
	private static ArrayList<Object> filterStrategy = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		Application.setUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/themes/nord-dark.css")).toExternalForm());
		SpringApplicationBuilder builder = new SpringApplicationBuilder(LibraryApplication.class);
//		builder.application().setWebApplicationType(WebApplicationType.NONE);

		context = builder.run();

        MainController mainController = new MainController(dataService);
		mainController.loadNewScene(SceneType.LOGIN);
	}

	public static ConfigurableApplicationContext getAppContext(){
		return context;
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

		Application.setUserAgentStylesheet(Objects.requireNonNull(LibraryApplication.class.getResource(pathToCss)).toExternalForm());
	}
}
