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
 * Page where all the outfits saved are displayed. Appears when outfits button
 * click on homepage. 
 * @author Bhavini Kopparam Ajay
 */
public class OutfitsPage extends BorderPane {
    
    // COLOR PALETTE
    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN = "#565448";
    private static final String CREAM = "#FFFAF4";

    //CLASS FIELDS
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

    private Font outfitFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/YesevaOne-Regular.ttf"
            ),
            20
    );

    private Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/MuktaVaani-SemiBold.ttf"
            ),
            19
    );
    
    public OutfitsPage(Stage stage) {

        this.stage = stage;

        setStyle(
                "-fx-background-color: " + CREAM + ";"
        );

        VBox root = new VBox(25);

        root.setPadding(
                new Insets(25)
        );

        root.getChildren().addAll(
                createTopSection(),
                createOutfitsSection()
        );

        setCenter(root);
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
        );

        backButton.setOnAction(e -> {

            MainPage mainPage = new MainPage();

            mainPage.start(stage);

        });

        Label title = new Label("My Outfits");

        title.setFont(titleFont);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox topRow = new HBox();

        topRow.setAlignment(Pos.CENTER);

        topRow.getChildren().addAll(
                backButton,
                leftSpacer,
                title,
                rightSpacer
        );

        VBox topSection = new VBox();

        topSection.getChildren().add(
                topRow
        );

        return topSection;
    }
    
    //right now hardcoded outfit. But to link it with data base add outfit as
    //param.
    private HBox createOutfitsSection() {

        VBox leftColumn = new VBox(25);

        leftColumn.getChildren().addAll(
                createOutfitCard(
                        "Outfit 1",
                        "White Linen Top",
                        "top",
                        "Blue Jeans",
                        "bottom"
                ),
                createOutfitCard(
                        "Outfit 2",
                        "Beige Trench Coat",
                        "outerwear",
                        "White Tee",
                        "top"
                )
        );

        VBox rightColumn = new VBox(25);

        rightColumn.getChildren().addAll(
                createOutfitCard(
                        "Outfit 3",
                        "Red Party Dress",
                        "one piece",
                        "Black Bag",
                        "accessory"
                ),
                createOutfitCard(
                        "Outfit 4",
                        "Oversized Blazer",
                        "outerwear",
                        "Silk Slip Skirt",
                        "bottom"
                )
        );

        Region divider = new Region();

        divider.setPrefWidth(2);

        divider.setStyle(
                "-fx-background-color: #EAD7BC;"
        );

        HBox outfitsRow = new HBox(40);

        outfitsRow.getChildren().addAll(
                leftColumn,
                divider,
                rightColumn
        );

        return outfitsRow;
    }

    private VBox createOutfitCard(
            String outfitName,
            String item1,
            String tag1,
            String item2,
            String tag2
    ) {

        VBox card = new VBox(20);

        card.setPadding(
                new Insets(25)
        );

        card.setPrefSize(650, 260);

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAD7BC;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;"
        );

        Label title = new Label(outfitName);

        title.setFont(outfitFont);

        HBox itemsRow = new HBox(30);

        itemsRow.setAlignment(Pos.CENTER);

        VBox itemCard1 = createMiniItemCard(
                item1,
                tag1
        );

        VBox itemCard2 = createMiniItemCard(
                item2,
                tag2
        );

        Image plusImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/plus.png"
                )
        );

        ImageView plusIcon = new ImageView(plusImg);

        plusIcon.setFitWidth(40);
        plusIcon.setFitHeight(40);

        itemsRow.getChildren().addAll(
                itemCard1,
                plusIcon,
                itemCard2
        );

        card.getChildren().addAll(
                title,
                itemsRow
        );

        return card;
    }

    private VBox createMiniItemCard(
            String itemName,
            String tagText
    ) {

        VBox card = new VBox(15);

        card.setPadding(
                new Insets(20)
        );

        card.setPrefSize(260, 160);

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAD7BC;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;"
        );

        Label name = new Label(itemName);

        name.setFont(buttonFont);

        Label tag = new Label(tagText);

        tag.setFont(normalFont);

        tag.setStyle(
                "-fx-background-color: #565448;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 12 6 12;"
                + "-fx-text-fill: white;"
        );

        card.getChildren().addAll(
                name,
                tag
        );

        return card;
    }
    
   
    
}
