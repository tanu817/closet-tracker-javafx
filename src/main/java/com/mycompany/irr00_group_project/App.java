/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 */

package com.mycompany.irr00_group_project;

import com.mycompany.irr00_group_project.ui.SearchPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the project.
 * 
 * @author Priyanshu Guha
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        SearchPage page = new SearchPage();

        Scene scene = new Scene(page, 1000, 700);

        stage.setTitle("Closet Tracker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
