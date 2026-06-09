package com.mycompany.irr00_group_project;

import com.mycompany.irr00_group_project.database.DatabaseManager;
import com.mycompany.irr00_group_project.ui.MainPage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class of the project.
 *
 * @author Priyanshu Guha
 * @author Arshiya Shaik
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Initialise CSV files before any UI is shown
        DatabaseManager.initialiseFiles();

        MainPage mainPage = new MainPage();
        mainPage.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}