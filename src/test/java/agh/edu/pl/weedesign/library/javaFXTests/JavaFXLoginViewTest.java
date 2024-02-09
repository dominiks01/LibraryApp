package agh.edu.pl.weedesign.library.javaFXTests;

import agh.edu.pl.weedesign.library.LibraryApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.api.FxAssert.verifyThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ContextConfiguration(classes = LibraryApplication.class)
public class JavaFXLoginViewTest extends ApplicationTest {

    LibraryApplication libraryApplication;

    @Start
    public void start(Stage stage) throws InterruptedException {
        libraryApplication = new LibraryApplication();
        libraryApplication.start(stage);
    }

    @Test
    void testLoginOfInvalidUser() {
        clickOn("#loginButton");

        clickOn("#loginField");
        write("Dominik");

        clickOn("#loginPasswordField");
        write("Dominik");

        clickOn("#loginButton");

        var label = lookup("#messageField");
        verifyThat(label, hasText("Wrong password or username"));
    }

    @Test
    void testLoginOfValudUser() {
        clickOn("#loginButton");

        clickOn("#loginField");
        write("example1@email.com");

        clickOn("#loginPasswordField");
        write("pass1");

        clickOn("#loginButton");
    }
}