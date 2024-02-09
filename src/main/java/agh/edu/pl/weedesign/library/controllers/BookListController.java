package agh.edu.pl.weedesign.library.controllers;

import java.util.*;

import agh.edu.pl.weedesign.library.helpers.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@Controller
public class BookListController {

    public Button prevPageButton;
    public Button nextPageButton;

    private int pageCounter = 0;
    private int maxPageCounter = 0;

    @FXML
    public HBox popularBooksHBox;
    @FXML
    public HBox recommendedBooksHBox;
    @FXML
    private ComboBox<SearchStrategy> searchStrategyMenu;
    @FXML
    public TextField findTextField;
    @FXML
    public ComboBox<FilterStrategy> filterStrategyMenu;
    @FXML
    public ComboBox<String> filterValueMenu;

    @FXML
    private ComboBox<SearchStrategy> sortStrategyMenu;

    @FXML
    private ComboBox<SortOrder> sortOrderMenu;

    @FXML
    private CheckBox onlyAvailableCheckbox;

    @FXML
    private CheckBox changeView;

    // Table view
    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Double> ratingColumn;

    @FXML
    private TableColumn<Book, String> availabilityColumn;

    // Tiles View
    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private VBox mainViewBox;

    @FXML
    private ScrollPane scrollPane;
    
    // Navbar controls 
    @FXML
    private Button mainPage; 

    @FXML
    private Button myRentals; 

    @FXML
    private Button logOut; 

   @FXML 
    private ChoiceBox<String> themeChange;

    private boolean isTableVisible = false;

    private List<Book> allBooks;
    private ObservableList<Book> visibleBooks;

    private Map<Book, Long> booksCount;

    private ModelService service;
    private BookListProcessor bookListProcessor;
    private ArrayList<HBox> rows = new ArrayList<>();
    private Recommender recommender;
    private final int popularBooksCount = 9;
    private final int recommendedBooksCount = 9;

    @Autowired
    public BookListController(ModelService service, BookListProcessor bookListProcessor, Recommender recommender){
        this.service = service;
        this.bookListProcessor = bookListProcessor;
        this.recommender = recommender;
    }

    @FXML
    public void initialize(){
        pageCounter = 0;
        themeChange.getItems().addAll(Themes.getAllThemes());
        themeChange.setOnAction(this::changeTheme);
        themeChange.setValue(LibraryApplication.getTheme());

        scrollPane.setVisible(!isTableVisible);
        bookTable.setVisible(isTableVisible);

        initializeStrategy();

        fetchAndProcessBooksData();
        reloadDisplayedBooks();
        updateButtonState();
    }

    private void initializeStrategy(){
        ArrayList<Object> defaultStrategy = getDefaultStrategy();

        if(defaultStrategy == null){
            clearSearchingOptions();
            return;
        }

        this.filterValueMenu.setValue((String)defaultStrategy.get(1));
        this.sortOrderMenu.setValue((SortOrder)defaultStrategy.get(2));
        this.searchStrategyMenu.setValue((SearchStrategy)defaultStrategy.get(3));
        this.findTextField.setText((String)defaultStrategy.get(4));
        this.onlyAvailableCheckbox.setSelected((boolean)defaultStrategy.get(6));
    }

    private void initializeTableDisplay(){
        titleColumn.setCellValueFactory(bookValue -> new SimpleStringProperty(bookValue.getValue().getTitle()));
        authorColumn.setCellValueFactory(bookValue -> new SimpleStringProperty(bookValue.getValue().getAuthorString()));
        ratingColumn.setCellValueFactory(bookValue -> new ReadOnlyObjectWrapper<>(service.getAverageRating(bookValue.getValue()))); // TODO - implement rating
        
        availabilityColumn.setCellValueFactory(bookValue -> {
            if (booksCount == null)
                return new SimpleStringProperty("Error");
            
            if (booksCount.get(bookValue.getValue()) == null)
                return new SimpleStringProperty("Brak książki!");
            
            return new SimpleStringProperty(booksCount.get(bookValue.getValue()).toString());
        });

        bookTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                if (getSelectedBook() == null){
                    return;
                }
                LibraryApplication.setBook(getSelectedBook());
                LibraryApplication.getAppController().switchScene(SceneType.BOOK_VIEW);
            }
        });

        bookTable.setItems(FXCollections.observableList(this.visibleBooks));
    }

    @FXML
    private void search(ActionEvent actionEvent) {
        pageCounter = 0;
        saveStrategy();

        this.recommender.setShowPopularBooks(false);
        this.recommender.setShowRecommendations(false);

        fetchAndProcessBooksData();
        reloadDisplayedBooks();
        updateButtonState();
    }

    @FXML
    public void back(ActionEvent actionEvent) {
//        LibraryApplication.getAppController().switchScene(SceneType.NEW_BOOK_VIEW);
        LibraryApplication.getAppController().switchScene(SceneType.START_VIEW);
    }

    @FXML
    public void clear(ActionEvent actionEvent) {
        LibraryApplication.saveStrategy(null);
        this.recommender.setShowPopularBooks(true);
        this.recommender.setShowRecommendations(true);

        clearSearchingOptions();
        setFilteringMenuContent();
    }


    public void setFilteringMenuContent() {
        // if (this.filterStrategyMenu.getValue() == null) {
            this.filterValueMenu.setItems(FXCollections.observableArrayList());
        //     return;
        // }

        if(this.filterStrategyMenu.getValue() == null)
            return;

        switch (this.filterStrategyMenu.getValue()){
            case AUTHOR -> this.filterValueMenu.setItems(FXCollections.observableList(
                    this.service.getAuthors().stream().map(Author::getSurname).toList()
            ));
            case CATEGORY -> this.filterValueMenu.setItems(FXCollections.observableList(
                    this.service.getCategories().stream().map(Category::getName).toList()
            ));
        }

    }

    private void initializeTilesDisplay(){
        int currRow= 0;

//      adding popular books and recommendations
        if(this.recommender.showPopularBooks()){
            this.showPopularBooks();
            currRow++;
        }
        if(this.recommender.showRecommendations()) {
            this.showRecommendations();
            currRow++;
        }
        Label label = new Label("Wszystkie książki");
        label.setFont(new Font("System Bold",21.0));
        label.setPadding(new Insets(5, 0, 0, 10));
        this.rows.add(new HBox(new VBox(label, new HBox())));
        currRow++;

        for(int i = 0 ; i <= this.visibleBooks.size()/5; i++)
            rows.add(new HBox());

        for(Book b: this.visibleBooks){
            ImageView newCover = this.createImageCover(b);
            rows.get(currRow).getChildren().add(newCover);
            if (rows.get(currRow).getChildren().size() >= 5) {
                currRow++;
            }
        }

        for(HBox x : rows)
            mainViewBox.getChildren().add(x);
    }

    private ImageView createImageCover(Book b) {
        ImageView newCover = new ImageView();
        newCover.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(LibraryApplication.getBook() == b)
                LibraryApplication.getAppController().switchScene(SceneType.BOOK_VIEW);

            LibraryApplication.setBook(b);
            System.out.println(LibraryApplication.getBook());
        }
        );

        newCover.setFitHeight(300);
        newCover.setFitWidth(194);
        newCover.maxHeight(300);
        newCover.maxWidth(194);

        Image img;

        try {
            img = new Image("" + b.getCoverUrl() + "", true);
        } catch (Exception e){
            img = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("default_book_cover_2015.jpg")).toString(), true);
        }

        newCover.setImage(img);
        return newCover;
    }

    private void showRecommendations() {
        Label label = new Label("Polecane dla ciebie");
        label.setFont(new Font("System Bold",21.0));
        label.setPadding(new Insets(5, 0, 0, 10));
        HBox recommendationsHBox = new HBox();

        for (Book book : recommender.getRecommendedBooks(this.recommendedBooksCount, LibraryApplication.getReader())){
            ImageView newCover = this.createImageCover(book);
            recommendationsHBox.getChildren().add(newCover);
        }

        this.rows.add(new HBox(new VBox(label, recommendationsHBox)));
    }

    private void showPopularBooks() {
        Label label = new Label("Popularne");
        label.setFont(new Font("System Bold",21.0));
        label.setPadding(new Insets(5, 0, 0, 10));
        HBox popularHBox = new HBox();

        for (Book book : this.recommender.getMostPopularBooks(this.popularBooksCount)){
            ImageView newCover = this.createImageCover(book);
            popularHBox.getChildren().add(newCover);
        }

        this.rows.add(new HBox(new VBox(label, popularHBox)));
    }

    private void fetchAndProcessBooksData(){
        this.booksCount = this.service.getAvailableBookCount(this.visibleBooks);

        Page<Book> tempBookPage = this.service.getBooksFilteredSorted(
                pageCounter,
                this.filterValueMenu.getValue(),
                this.sortOrderMenu.getValue()
        );
        maxPageCounter = tempBookPage.getTotalPages();
        System.out.println(tempBookPage.getTotalPages());
        System.out.println(this.pageCounter);

        List<Book> tempBookList = this.bookListProcessor.processList(
                tempBookPage.toList(),
                this.searchStrategyMenu.getValue(),
                this.findTextField.getText(),
                this.booksCount,
                this.onlyAvailableCheckbox.isSelected()
        );

        this.visibleBooks = FXCollections.observableList(tempBookList);
    }

    private Book getSelectedBook(){
        return bookTable.getSelectionModel().getSelectedItem();
    }

    private SceneType getBookDetailsScene(Book book) {
        return SceneType.BOOK_VIEW;
    }

    private void hopToNextScene(SceneType sceneType){
        if (sceneType == null){
            return;
        }
        
        LibraryApplication.getAppController().switchScene(SceneType.BOOK_VIEW);
    }

    public void changeView(){
        isTableVisible = !isTableVisible;
        if (isTableVisible) {
            bookTable.setItems(this.visibleBooks);
        }

        scrollPane.setVisible(!isTableVisible);
        bookTable.setVisible(isTableVisible);
        reloadDisplayedBooks();
    }

    public void goBackAction(){
        LibraryApplication.getAppController().back();
    }

    public void goForwardAction(){
        LibraryApplication.getAppController().forward();
    }

    public void mainPageButtonHandler(){
        //pass
    }

    public void myRentalsButtonHandler(){
        LibraryApplication.getAppController().switchScene(SceneType.RENTALS_VIEW); 
    }

    public void changeTheme(ActionEvent e){
        LibraryApplication.changeTheme(themeChange.getValue());
    }

    public void LogOutAction() {
        LibraryApplication.getAppController().logOut();
    }


    private void clearSearchingOptions() {
        // Ni chuja nie wiem jak tu zrobić żeby znowu wyświetlił się prompt text, nienawidzę javafx <3
        this.pageCounter = 0;
        this.maxPageCounter = 0;

        this.searchStrategyMenu.getSelectionModel().clearSelection();
        this.searchStrategyMenu.setValue(null);
        this.searchStrategyMenu.setPromptText("Wyszukaj po");

        this.findTextField.setText("");

        this.sortOrderMenu.getSelectionModel().clearSelection();
        this.sortOrderMenu.setValue(null);
        this.sortOrderMenu.setPromptText("Sortuj");

        this.sortStrategyMenu.getSelectionModel().clearSelection();
        this.sortStrategyMenu.setValue(null);
        this.sortStrategyMenu.setPromptText("Sortuj po");

        this.filterValueMenu.getSelectionModel().clearSelection();
        this.filterValueMenu.setValue(null);
        this.filterStrategyMenu.getSelectionModel().clearSelection();

        this.onlyAvailableCheckbox.setSelected(false);

        fetchAndProcessBooksData();
        reloadDisplayedBooks();
        updateButtonState();
    }

    public void prevPage(ActionEvent actionEvent) {
        pageCounter--;
        fetchAndProcessBooksData();
        reloadDisplayedBooks();
        updateButtonState();
    }
    public void nextPage(ActionEvent actionEvent) {
        pageCounter++;
        fetchAndProcessBooksData();
        reloadDisplayedBooks();
        updateButtonState();
    }

    public void updateButtonState() {
        if (pageCounter <= 0){
            prevPageButton.setDisable(true);
            nextPageButton.setDisable(false);
        } else if (pageCounter >= maxPageCounter-1){
            prevPageButton.setDisable(false);
            nextPageButton.setDisable(true);
        } else {
            prevPageButton.setDisable(false);
            nextPageButton.setDisable(false);
        }
    }

    public void saveStrategy(){
        LibraryApplication.saveStrategy(
            new ArrayList<>(){
                {
                    add(pageCounter);
                    add(filterValueMenu.getValue());
                    add(sortOrderMenu.getValue());
                    add(searchStrategyMenu.getValue());
                    add(findTextField.getText());
                    add(booksCount);
                    add(onlyAvailableCheckbox.isSelected());
                }
            }
        );
    }

    public ArrayList<Object> getDefaultStrategy(){
        return LibraryApplication.getStrategy();
    }

    void reloadDisplayedBooks() {
        mainViewBox.getChildren().clear();
        rows.clear();

        if (isTableVisible){
            initializeTableDisplay();
        }
        else {
            initializeTilesDisplay();
        }
    }
}
