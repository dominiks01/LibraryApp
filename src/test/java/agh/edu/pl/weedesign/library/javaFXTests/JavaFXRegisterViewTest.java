package agh.edu.pl.weedesign.library.javaFXTests;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.*;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.services.ModelService;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@SpringBootTest
@ContextConfiguration(classes = LibraryApplication.class)
public class JavaFXRegisterViewTest extends ApplicationTest {

    LibraryApplication libraryApplication;

    @Autowired
    private ModelService service;

    @Start
    public void start(Stage stage) {
        libraryApplication = new LibraryApplication();
        libraryApplication.start(stage);
    }

    @Test
    void testRegistrationOfValidUser() {
        clickOn("#registerButton");

        clickOn("#nameField");
        write("Jan");

        clickOn("#surnameField");
        write("Kowalski");

        clickOn("#passwordField");
        write("pass4");

        clickOn("#countryField");
        write("Polska");

        clickOn("#cityField");
        write("Krakow");

        clickOn("#voivodeshipField");
        write("Malopolska");

        clickOn("#postcodeField");
        write("31-503");

        clickOn("#emailField");
        write("example10@email.com");

        clickOn("#phoneField");
        write("999 888 777");

        clickOn("#birthDateField");
        write("10.12.2023");

        clickOn("#sexField");
        write("male");

        clickOn("#registerButton");

        Reader reader = service.getReaderByEmail("example10@email.com");
        assertEquals("999 888 777", reader.getPhone_number());
    }

    @Test
    void testRegistrationOfInvalidUser() {
        clickOn("#registerButton");

        clickOn("#nameField");
        write("Jan");

        clickOn("#surnameField");
        write("Kowalski");

        clickOn("#passwordField");
        write("p");

        clickOn("#countryField");
        write("Polska");

        clickOn("#cityField");
        write("Krakow");

        clickOn("#voivodeshipField");
        write("Malopolska");

        clickOn("#postcodeField");
        write("31-503");

        clickOn("#emailField");
        write("example10@email.com");

        clickOn("#phoneField");
        write("999 888 777");

        clickOn("#birthDateField");
        write("10.12.2023");

        clickOn("#sexField");
        write("male");

        clickOn("#registerButton");
        
        Reader reader = service.getReaderByEmail("example10@email.com");
        assertNull(reader);
        var label = lookup("#messageField");
        verifyThat(label, hasText("Password to short"));
    }
}