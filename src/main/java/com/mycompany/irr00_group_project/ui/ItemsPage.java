/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.irr00_group_project.ui;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Items page of the closet tracker app.
 *
 * @author Bhavini Kopparam Ajay
 */
public class ItemsPage extends BorderPane {

    // COLOR PALETTE
    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN = "#565448";
    private static final String CREAM = "#FFFAF4";

    //CLASS FIELDS
    private FlowPane itemsGrid;
    private Stage stage;

    //FONTS
    private Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/MuktaVaani-SemiBold.ttf"
            ),
            14
    );

    private Font normalFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/MuktaVaani-Regular.ttf"
            ),
            13
    );

    private Font titleFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/YesevaOne-Regular.ttf"
            ),
            24
    );

    private Font subTitleFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/YesevaOne-Regular.ttf"
            ),
            15
    );

    private Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/MuktaVaani-SemiBold.ttf"
            ),
            19
    );

    public ItemsPage(Stage stage) {
        this.stage = stage;

        setStyle(
                "-fx-background-color: "
                + CREAM + ";"
        );

        VBox root = new VBox(20);

        root.setPadding(
                new Insets(25)
        );

        root.getChildren().addAll(
                createTopSection(),
                createItemsSection()
        );

        setCenter(root);

    }

    private ScrollPane createItemsSection() {
        itemsGrid = new FlowPane();
        
        itemsGrid.setAlignment(Pos.TOP_CENTER);

        itemsGrid.setHgap(25);
        itemsGrid.setVgap(25);

        itemsGrid.setPadding(
                new Insets(10)
        );

        ScrollPane scrollPane
                = new ScrollPane(itemsGrid);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: transparent;"
                + "-fx-background-color: transparent;"
        );

        loadDummyData(); //Only replace this for data base integration

        return scrollPane;
    }

    private VBox createItemCard(
            String itemName,
            String... tags
    ) {

        VBox card = new VBox(15);

        card.setPrefSize(470, 160);

        card.setPadding(
                new Insets(20)
        );

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAE4D8;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
        );

        // TOP ROW
        HBox topRow = new HBox();

        topRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label name
                = new Label(itemName);

        name.setFont(buttonFont);

        name.setStyle(
                "-fx-text-fill: black;"
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Image heartImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/heart.png"
                )
        );

        ImageView heart
                = new ImageView(heartImg);

        heart.setFitWidth(30);
        heart.setFitHeight(30);

        topRow.getChildren().addAll(
                name,
                spacer,
                heart
        );

        // TAGS
        FlowPane tagsPane
                = new FlowPane();

        tagsPane.setHgap(10);
        tagsPane.setVgap(10);

        for (String tag : tags) {

            tagsPane.getChildren().add(
                    createTag(tag)
            );
        }

        card.getChildren().addAll(
                topRow,
                tagsPane
        );

        return card;
    }

    private VBox createTopSection() {

        Image backImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/arrow.png"
                )
        );

        ImageView backIcon = new ImageView(backImg);

        backIcon.setFitWidth(35);
        backIcon.setFitHeight(35);

        Button backButton = new Button();

        backButton.setGraphic(backIcon);

        backButton.setPrefSize(40, 42);

        backButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
        );

        backButton.setOnAction(e -> {

            MainPage mainPage = new MainPage();

            mainPage.start(stage);

        });

        Label title = new Label("All Items");

        title.setFont(titleFont);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(
                leftSpacer,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                rightSpacer,
                Priority.ALWAYS
        );

        HBox topRow = new HBox();

        topRow.setAlignment(Pos.CENTER);

        topRow.getChildren().addAll(
                backButton,
                leftSpacer,
                title,
                rightSpacer
        );

        VBox topSection = new VBox(20);

        topSection.getChildren().add(
                topRow
        );

        return topSection;
        
    }

    private Label createTag(
            String text
    ) {

        Label tag
                = new Label(text);

        tag.setFont(normalFont);

        tag.setStyle(
                "-fx-background-color: #565448;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 12 6 12;"
                + "-fx-text-fill: white;"
        );

        return tag;
    }

    private void loadDummyData() {

        itemsGrid.getChildren().add(
                createItemCard(
                        "White Linen Top",
                        "top",
                        "white",
                        "summer",
                        "casual"
                )
        );

        itemsGrid.getChildren().add(
                createItemCard(
                        "Blue Straight Jeans",
                        "bottom",
                        "blue",
                        "denim",
                        "casual"
                )
        );

        itemsGrid.getChildren().add(
                createItemCard(
                        "Beige Trench Coat",
                        "outerwear",
                        "beige",
                        "fall",
                        "classic"
                )
        );
    }
}
