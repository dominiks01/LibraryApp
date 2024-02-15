package agh.edu.pl.weedesign.library.controllers;


import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.models.NewBookModel;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


@Controller
public class NewBookController extends IController {

    private final NewBookModel model;

    @FXML
    private TextField book_number_text_field;

    @FXML
    private TextField title_text_field;

    @FXML
    private TextField author_first_name_text_field;

    @FXML
    private TextField author_surname_text_field;

    @FXML
    private TextField page_count_text_field;    
    
    @FXML
    public ComboBox<String> categories;

    @FXML
    private TextField no_of_copies_text_field; 
    
    @FXML 
    private TextField condition_textfield;

    @FXML
    private TextField book_cover_text_field;

    @FXML
    private TextArea book_description_text_area;

    @FXML
    private TextArea table_of_content_text_area;

    @FXML 
    private Button save_book_button;

    @FXML 
    private Button cancel_button;

    @FXML 
    private TextField message_label;

    private ModelService service;

    @Autowired
    public NewBookController(NewBookModel model, ModelService service){
        this.model = model;
        this.service = service;
    }

    public void handleCancelAction() throws IOException {
        super.switchScene(SceneType.EMPLOYEE_PANEL);
    }

    public void saveBookAction(){
        try {

            // TODO kategorie! 
            this.model.setTitle(title_text_field.getText());
            this.model.setDescription(book_description_text_area.getText());
            this.model.setPageCount(Integer.valueOf(page_count_text_field.getText()));
            this.model.setTableOfContent(table_of_content_text_area.getText());
            this.model.setCover( book_cover_text_field.getText());
            this.model.setAuthorFirstName(author_first_name_text_field.getText());
            this.model.setAuthorSecondName(author_surname_text_field.getText());
            this.model.setNoOfCopies(Integer.valueOf(no_of_copies_text_field.getText()));
            this.model.setCondition(condition_textfield.getText());

            this.model.addNewBook();

            System.out.println("Book added successfully");

            message_label.setText("");
            message_label.setVisible(false);
 
        } catch (Exception e){
            message_label.setText(String.valueOf(e.getMessage()));
            message_label.setVisible(true);
        }
    }

    public void goBackAction(){
        super.goBack();
    }

    public void goForwardAction(){
        super.goForward();
    }

    public void mainPageButtonHandler() throws IOException {
        handleCancelAction();
    }


    @Override
    public void consumeData(){
        ArrayList<Category> categoryList = (ArrayList<Category>) service.getCategories();
        for(Category i: categoryList)
            categories.getItems().add(i.getName());
    }

}
