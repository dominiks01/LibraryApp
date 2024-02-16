package agh.edu.pl.weedesign.library.sceneObjects;

import agh.edu.pl.weedesign.library.LibraryApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.springframework.security.core.parameters.P;


import java.io.IOException;


public final class SceneFactory {

    private static SceneFactory instance;

    public static SceneFactory getInstance(){
        if(instance == null)
            instance = new SceneFactory();

        return instance;
    }

    private final String loginViewPath = "/views/loginView.fxml";
    private final String mainViewPath = "/views/mainView.fxml";
    private final String registrationViewPath = "/views/registerView.fxml";
    private final String bookListViewPath = "/views/bookList.fxml";
    private final String bookViewPath = "/views/bookView.fxml";
    private final String newBookViewPath = "/views/newBookView.fxml";
    private final String bookCopiesViewPath = "/views/bookCopiesView.fxml";
    private final String rentalsViewPath = "/views/rentalsView.fxml";
    private final String addReviewViewPath = "/views/addReviewView.fxml";
    private final String reviewsViewPath = "/views/bookReviewsView.fxml";
    private final String employeePanelPath = "/views/employeePanelView.fxml";
    private final String addEmployeeViewPath = "/views/addEmployeeView.fxml";
    private final String statsViewPath = "/views/statsView.fxml";
    private final String rentalsAcceptanceViewPath = "/views/rentalsAcceptanceView.fxml";
    private final String singleRentalViewPath = "/views/singleRentalView.fxml";
    private final String acceptanceView = "/views/acceptanceView.fxml";
    private final String settingsViewPath = "/views/settingsView.fxml";

    public FXMLLoader createScene(SceneType sceneType) {
        try {
            switch (sceneType) {
                case LOGIN -> {
                    return loadScene(loginViewPath);
                }
                case MAIN -> {
                    return loadScene(mainViewPath);
                }
                case REGISTER -> {
                    return loadScene(registrationViewPath);
                }
                case BOOK_LIST -> {
                    return loadScene(bookListViewPath);
                }
                case BOOK_VIEW -> {
                    return loadScene(bookViewPath);
                }
                case NEW_BOOK_VIEW -> {
                    return loadScene(newBookViewPath);
                }
                case COPIES_VIEW -> {
                    return loadScene(bookCopiesViewPath);
                }
                case RENTALS_VIEW -> {
                    return loadScene(rentalsViewPath);
                }
                case REVIEWS -> {
                    return loadScene(reviewsViewPath);
                }
                case ADD_REVIEW -> {
                    return loadScene(addReviewViewPath);
                }
                case EMPLOYEE_PANEL -> {
                    return loadScene(employeePanelPath);
                }
                case ADD_EMPLOYEE -> {
                    return loadScene(addEmployeeViewPath);
                }
                case STATS_VIEW -> {
                    return loadScene(statsViewPath);
                }
                case RENTALS_ACCEPTANCE -> {
                    return loadScene(rentalsAcceptanceViewPath);
                }
                case SINGLE_RENTAL -> {
                    return loadScene(singleRentalViewPath);
                }
                case SETTINGS -> {
                    return loadScene(settingsViewPath);
                }
                case ACCEPTANCE -> {
                    return loadScene(acceptanceView);
                }

                default -> {
                    return null;
                }
            }
        }
        catch (IOException i){
            i.printStackTrace();
        }
        return null;
    }

    private FXMLLoader loadScene(String path) throws IOException {
        // load layout from FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(LibraryApplication.class.getResource(path));
        loader.setControllerFactory(LibraryApplication.getAppContext()::getBean);
        return loader;
    }

    
}
