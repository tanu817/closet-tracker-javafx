/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.irr00_group_project.ui;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;

/**
 *
 * @author Bhavini Kopparam Ajay
 */
public class MainPage {
    
    /**
     *
     * @param stage Create the Main page background, sets the title and the
     * background
     */
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        
        //FONTS
        Font titleFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/YesevaOne-Regular.ttf"
                ),
                28
        );
        
        Font buttonFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/MuktaVaani-SemiBold.ttf"
                ),
                14
        );

        Label title = new Label("Home");

        title.setFont(titleFont);
        

        // SEARCH BUTTON
        Button searchButton = new Button("Search by tags...");
        
        Image searchImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/Search.png"
                )
        );

        ImageView searchIcon = new ImageView(searchImg);

        searchIcon.setFitWidth(30);
        searchIcon.setFitHeight(30);

        searchButton.setGraphic(searchIcon);
        searchButton.setText("     Search by tags...");

        searchButton.setPrefWidth(850);
        searchButton.setPrefHeight(60);
        
        searchButton.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 10;"
                + "-fx-alignment: CENTER-LEFT;"
        );
        
        searchButton.setOnAction(e -> {
            SearchPage searchPage = new SearchPage();

            Scene searchScene = new Scene(searchPage, 1200, 700);

            stage.setScene(searchScene);
        });
        
        searchButton.setFont(buttonFont);
        
        //ITEMS BUTTON

        Button itemsButton = new Button("Items");
        itemsButton.setPrefSize(150, 58);
        
        itemsButton.setFont(buttonFont);
        
        itemsButton.setStyle(
                "-fx-background-color: #565448;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 10;"
        );
        
        //Change for items 
        //itemsButton.setOnAction(e -> {
        //    SearchPage searchPage = new SearchPage();

        //    Scene searchScene = new Scene(searchPage, 1200, 700);

        //    stage.setScene(searchScene);
        //});
        
        //OUTFITS BUTTON
        Button outfitsButton = new Button("Outfits");
        outfitsButton.setPrefSize(150, 58);

        outfitsButton.setStyle(
                "-fx-background-color: #D8CFBC;"
                + "-fx-background-radius: 10;"
        );

        outfitsButton.setFont(buttonFont);
        
        //Change for outfits 
        //outfitsButton.setOnAction(e -> {
        //    OutfitsPage outfitPage = new OutfitsPage();

        //    Scene outfitScene = new Scene(outfitPage, 1200, 700);

        //    stage.setScene(outfitScene);
        //});
        
        //WORKING WITH THE LAYOUT OF BUTTONS
        VBox topSection = new VBox(15);

        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        HBox searchRow = new HBox(15);

        searchRow.getChildren().addAll(
                searchButton,
                itemsButton,
                outfitsButton
        );

        topSection.getChildren().addAll(
                title,
                searchRow
        );

        //FAVOURITES CARD
        VBox favoritesCard = createCard(
                "Favorites",
                "/icons/heart.png",
                new String[]{
                    "White Linen Top",
                    "Blue Straight Jeans",
                    "Beige Trench Coat",
                    "White Sneakers",
                    "Black Handbag"
                },
                titleFont,
                buttonFont,
                25,
                25
        );

        //MOST WORN CARD
        
        VBox mostWornCard = createCard(
                "Most Worn",
                "/icons/hanger.png",
                new String[]{
                    "1.  White Linen Top",
                    "2.  Blue Straight Jeans",
                    "3.  Beige Trench Coat",
                    "4.  White Sneakers",
                    "5.  Black Handbag"
                },
                titleFont,
                buttonFont,
                30, 
                30
        );
        
        //LEAST WORN CARD
        VBox leastWornCard = createCard(
                "Least Worn",
                "/icons/hanger.png",
                new String[]{
                    "1.  Red Party Dress",
                    "2.  Silk Slip skirt",
                    "3.  Black Blazer",
                    "4.  Strappy Heels",
                    "5.  Knit Sweater"
                },
                titleFont,
                buttonFont,
                30,
                30
        );

        //LAYOUT OF CARDS
        HBox cardsRow = new HBox(20);
        cardsRow.setAlignment(Pos.CENTER);

        cardsRow.getChildren().addAll(
                favoritesCard,
                mostWornCard,
                leastWornCard
        );

        root.setCenter(cardsRow);
        root.setMargin(cardsRow, new Insets(20, 0, 0, 0));
        
        //OVERLAY
        AddItemsPage addPage = new AddItemsPage();

        VBox overlay = addPage.createAddItemsOverlay();

        Region dimBackground = new Region();

        dimBackground.setStyle(
                "-fx-background-color: rgba(0,0,0,0.35);"
        );

        dimBackground.setVisible(false);

        dimBackground.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        addPage.getBackButton().setOnAction(e -> {
            overlay.setVisible(false);
            dimBackground.setVisible(false);
        });

        overlay.setVisible(false);

        overlay.setMaxWidth(800);
        overlay.setMaxHeight(500);

        StackPane.setAlignment(
                overlay,
                Pos.CENTER
        );
        

        //PLUS BUTTON
        Image plusImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/plus.png"
                )
        );

        ImageView plusIcon = new ImageView(plusImg);

        plusIcon.setFitWidth(25);
        plusIcon.setFitHeight(25);

        Button addButton = new Button();

        addButton.setGraphic(plusIcon);

        addButton.setPrefSize(60, 60);

        addButton.setStyle(
                "-fx-background-color: #D8CFBC;"
                + "-fx-background-radius: 30;"
                + "-fx-border-radius: 30;"
        );
        
        addButton.setOnAction(e -> {
            dimBackground.setVisible(true);
            overlay.setVisible(true);
        });

        HBox bottomBar = new HBox();

        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);

        bottomBar.getChildren().add(addButton);

        root.setBottom(bottomBar);

        BorderPane.setMargin(
                bottomBar,
                new Insets(20)
        );

        //GENERAL LAYOUT AND BACKGROUND

        root.setTop(topSection);
        
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFAF4;");

        StackPane stackRoot = new StackPane();

        stackRoot.getChildren().add(root);
        stackRoot.getChildren().add(dimBackground);
        stackRoot.getChildren().add(overlay);
        
        Scene scene = new Scene(stackRoot, 1200, 700);

        stage.setTitle("Closet App");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * simplifies creating cards
     * @param titleText what you want title of card to be
     * @param iconText icon of the card
     * @param items what items will be in it
     * @return 
     */
    private VBox createCard(
            String titleText,
            String iconPath,
            String[] items,
            Font titleFont,
            Font buttonFont,
            int width,
            int height
    ) {
        VBox card = new VBox(12);

        card.setPadding(new Insets(15));
        card.setPrefWidth(320);
        card.setPrefHeight(330);

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 12;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 12;"
        );

        HBox titleRow = new HBox(10);

        Image iconImage = new Image(
                getClass().getResourceAsStream(iconPath)
        );

        ImageView icon = new ImageView(iconImage);

        icon.setFitWidth(width);
        icon.setFitHeight(height);
        
        Font cardTitleFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/YesevaOne-Regular.ttf"
                ),
                20
        );
        
        Label titleLabel = new Label(titleText);
        titleLabel.setFont(cardTitleFont);

        titleRow.getChildren().addAll(
                icon,
                titleLabel
        );

        card.getChildren().add(titleRow);

        for (String item : items) {

            Label itemLabel = new Label(item);

            itemLabel.setFont(buttonFont);

            card.getChildren().add(itemLabel);
        }

        return card;
    }

}
