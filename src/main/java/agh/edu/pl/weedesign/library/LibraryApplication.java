package agh.edu.pl.weedesign.library;

import agh.edu.pl.weedesign.library.controllers.LibraryAppController;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class LibraryApplication extends Application {
	private Stage primaryStage;
	private static LibraryAppController libraryAppController;
	private static ConfigurableApplicationContext context;
	private static Reader reader;
	private static Book book;
	private static Employee employee;
	private static String theme = "Nord Dark";
	private static ArrayList<Object> filterStrategy = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Application.setUserAgentStylesheet(getClass().getResource("/themes/nord-dark.css").toExternalForm());

		SpringApplicationBuilder builder = new SpringApplicationBuilder(LibraryApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		context = builder.run();
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Library App");

		libraryAppController = new LibraryAppController(this.primaryStage, context);
		libraryAppController.initWelcomeLayout();
	}

	public static ConfigurableApplicationContext getAppContext(){
		return context;
	}

	public static void switchScene(SceneType sceneType){
		getAppController().switchScene(sceneType);
	}

	public static LibraryAppController getAppController(){
		return libraryAppController;
	}

	public static Reader getReader(){
		return reader;
	}

	public static void setReader(Reader r){
		reader = r;
	}

	public static Employee getEmployee() {
		return employee;
	}

	public static void setEmployee(Employee e) {
		employee = e;
	}

	public static void setBook(Book b){
		book = b; 
	}

	public static Book getBook(){
		return book;
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
