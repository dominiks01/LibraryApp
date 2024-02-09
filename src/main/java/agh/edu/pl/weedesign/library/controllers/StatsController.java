package agh.edu.pl.weedesign.library.controllers;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.helpers.Themes;
import agh.edu.pl.weedesign.library.sceneObjects.SceneType;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.*;



@Controller
public class StatsController {

    @FXML
    private Text readersText;
    @FXML
    private Text booksText;
    @FXML
    private Text employeesText;
    @FXML
    private PieChart sexPlot;
    @FXML
    private BarChart bookPlot;
    @FXML
    private BarChart rentedBooksPlot;
    @FXML
    private LineChart rentalPlot;
    @FXML
    private LineChart incomePlot;
    @FXML
    private BarChart freqBookPlot;
    @FXML
    private BarChart freqCategoryPlot;
    @FXML
    private BarChart freqReaderPlot;

    // Navbar controls 
    @FXML
    private Button mainPage; 

    @FXML
    private Button logOut; 

    @FXML 
    private ChoiceBox<String> themeChange;


    final private ModelService modelService;

    @Autowired
    public StatsController(ModelService service){
        this.modelService = service;
    }

    @FXML
    public void initialize(){
        themeChange.getItems().addAll(Themes.getAllThemes());
        themeChange.setOnAction(this::changeTheme);
        themeChange.setValue(LibraryApplication.getTheme());

        //get rental data (often used)
        List<Rental> rentals = this.modelService.getRentals();


        //text stats
        this.booksText.setText("Liczba książek w bibliotece: " + this.modelService.getBooksCopies().size());
        this.employeesText.setText("Liczba pracowników biblioteki: " + this.modelService.getEmployees().size());
        this.readersText.setText("Liczba czytelników biblioteki: " + this.modelService.getReaders().size());

        //sexy plot
        int male = 0;
        int female = 0;
        int other = 0;
        for(Reader reader : this.modelService.getReaders()){
            if(reader.getSex().equals("male"))
                male += 1;
            else if (reader.getSex().equals("female"))
                female += 1;
            else
                other += 1;
        }
        PieChart.Data maleData = new PieChart.Data("Mężczyźni", male);
        PieChart.Data femaleData = new PieChart.Data("Kobiety", female);
        PieChart.Data otherData = new PieChart.Data("Nie podano", other);
        this.sexPlot.legendVisibleProperty().set(false);
        this.sexPlot.getData().addAll(maleData, femaleData, otherData);

        //book categories
        XYChart.Series<String, Number> bookSeries = new XYChart.Series<>();
        for(Category category : this.modelService.getCategories()){
            int n = 0;
            for(Book book : this.modelService.getBooks())
                if(book.getCategory().getName().equals(category.getName()))
                    n += 1;
            bookSeries.getData().add(new XYChart.Data<>(category.getName(), n));
        }
        this.bookPlot.legendVisibleProperty().set(false);
        this.bookPlot.getData().add(bookSeries);

        //rented books
        XYChart.Series<String, Number> rentedSeries = new XYChart.Series<>();
        rentedSeries.getData().add(new XYChart.Data<>("Liczba wszystkich książek", this.modelService.getBooksCopies().size()));
        int rented = 0;
        for(Rental rental : rentals)
            if(rental.getEnd_date() == null)
                rented += 1;
        rentedSeries.getData().add(new XYChart.Data<>("Liczba aktualnie wypożyczonych książek", rented));
        this.rentedBooksPlot.legendVisibleProperty().set(false);
        this.rentedBooksPlot.getData().add(rentedSeries);

        //rental plot
        LocalDate start = LocalDate.now().minusYears(1);
        int[] tab = new int[12];
        for(Rental rental : rentals){
            if(rental.getEmployee() != null){
                for(int i=0;i<12;i++){
                    if(rental.getStart_date().toLocalDate().isBefore(start.plusMonths(i+1)) && (rental.getEnd_date() == null || rental.getEnd_date().toLocalDate().isAfter(start.plusMonths(i))))
                        tab[i] += 1;
                }
            }
        }
        boolean flag = true;
        XYChart.Series<String, Number> rentalSeries = new XYChart.Series<>();
        for(int i=0;i<12;i++){
            //if first months are equal to 0, then do not add them to the plot
            if(flag && tab[i] == 0)
                continue;
            flag = false;
            rentalSeries.getData().add(new XYChart.Data<>(start.plusMonths(i).toString(), tab[i]));
        }
        this.rentalPlot.legendVisibleProperty().set(false);
        this.rentalPlot.getData().add(rentalSeries);

        //income plot
        int[] itab = new int[12];
        for(Rental rental : rentals){
            if(rental.getEmployee() != null && rental.getEnd_date() != null){
                for(int i=0;i<12;i++){
                    if(rental.getEnd_date().toLocalDate().isAfter(start.plusMonths(i)) && rental.getEnd_date().toLocalDate().isBefore(start.plusMonths(i+1)))
                        itab[i] += rental.getPrice();
                }
            }
        }
        flag = true;
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        for(int i=0;i<12;i++){
            //if first months are equal to 0, then do not add them to the plot
            if(flag && itab[i] == 0)
                continue;
            flag = false;
            incomeSeries.getData().add(new XYChart.Data<>(start.plusMonths(i).toString(), itab[i]));
        }
        this.incomePlot.legendVisibleProperty().set(false);
        this.incomePlot.getData().add(incomeSeries);

        //freq book
        Map<Book, Integer> freqBooks = new HashMap<>();
        for(Book book : this.modelService.getBooks())
            freqBooks.put(book, this.modelService.getRentalsByBook(book).size());
        List<Map.Entry<Book, Integer>> freqBooksList = new ArrayList<>(freqBooks.entrySet());
        freqBooksList.sort(Map.Entry.comparingByValue());
        Collections.reverse(freqBooksList);
        XYChart.Series<String, Number> freqBooksSeries = new XYChart.Series<>();
        int counter = 0;
        for(Map.Entry<Book, Integer> entry : freqBooksList){
            freqBooksSeries.getData().add(new XYChart.Data<>(entry.getKey().getTitle(), entry.getValue()));
            counter += 1;
            if(counter == 5)
                break;
        }
        this.freqBookPlot.legendVisibleProperty().set(false);
        this.freqBookPlot.getData().add(freqBooksSeries);

        //freq category
        Map<Category, Integer> freqCategory = new HashMap<>();
        for(Category category : this.modelService.getCategories())
            freqCategory.put(category, this.modelService.getRentalsByCategory(category).size());
        List<Map.Entry<Category, Integer>> freqCategoryList = new ArrayList<>(freqCategory.entrySet());
        freqCategoryList.sort(Map.Entry.comparingByValue());
        Collections.reverse(freqCategoryList);
        XYChart.Series<String, Number> freqCategorySeries = new XYChart.Series<>();
        counter = 0;
        for(Map.Entry<Category, Integer> entry : freqCategoryList){
            freqCategorySeries.getData().add(new XYChart.Data<>(entry.getKey().getName(), entry.getValue()));
            counter += 1;
            if(counter == 5)
                break;
        }
        this.freqCategoryPlot.legendVisibleProperty().set(false);
        this.freqCategoryPlot.getData().add(freqCategorySeries);

        //freq readers
        Map<Reader, Integer> freqReader = new HashMap<>();
        for(Reader reader : this.modelService.getReaders())
            freqReader.put(reader, this.modelService.getRentalsByReader(reader).size());
        List<Map.Entry<Reader, Integer>> freqReaderList = new ArrayList<>(freqReader.entrySet());
        freqReaderList.sort(Map.Entry.comparingByValue());
        Collections.reverse(freqReaderList);
        XYChart.Series<String, Number> freqReaderSeries = new XYChart.Series<>();
        counter = 0;
        for(Map.Entry<Reader, Integer> entry : freqReaderList){
            freqReaderSeries.getData().add(new XYChart.Data<>(entry.getKey().getName() + " " + entry.getKey().getSurname(), entry.getValue()));
            counter += 1;
            if(counter == 5)
                break;
        }
        this.freqReaderPlot.legendVisibleProperty().set(false);
        this.freqReaderPlot.getData().add(freqReaderSeries);


    }

    public void goBackAction(){
        LibraryApplication.getAppController().back();
    }

    public void goForwardAction(){
        LibraryApplication.getAppController().forward();
    }

    public void mainPageButtonHandler(){
        LibraryApplication.getAppController().switchScene(SceneType.EMPLOYEE_PANEL); 
    }

    public void changeTheme(ActionEvent e){
        LibraryApplication.changeTheme(themeChange.getValue());
    }

    public void LogOutAction(){
        LibraryApplication.getAppController().logOut();
    }

}
