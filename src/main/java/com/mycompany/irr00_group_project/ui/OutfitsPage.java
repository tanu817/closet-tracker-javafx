package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.OutfitController;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Outfits page — shows all outfits loaded from CSV storage.
 *
 * @author Bhavini Kopparam Ajay
 */
public class OutfitsPage extends BorderPane {

    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN  = "#565448";
    private static final String CREAM       = "#FFFAF4";

    private final Stage stage;

    private final Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);
    private final Font normalFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"), 13);
    private final Font titleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 24);
    private final Font outfitFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 20);
    private final Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 19);

    private final OutfitController outfitController = new OutfitController();

    public OutfitsPage(Stage stage) {
        this.stage = stage;
        setStyle("-fx-background-color: " + CREAM + ";");

        VBox root = new VBox(25);
        root.setPadding(new Insets(25));
        root.getChildren().addAll(createTopSection(), createOutfitsSection());
        setCenter(root);
    }

    // ==================
    // Top section
    // ==================

    private VBox createTopSection() {
        Image backImg = new Image(
                getClass().getResourceAsStream("/icons/arrow.png"));
        ImageView backIcon = new ImageView(backImg);
        backIcon.setFitWidth(35);
        backIcon.setFitHeight(35);

        Button backButton = new Button();
        backButton.setGraphic(backIcon);
        backButton.setPrefSize(40, 42);
        backButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;");
        backButton.setOnAction(e -> {
            MainPage mainPage = new MainPage();
            mainPage.start(stage);
        });

        Label title = new Label("My Outfits");
        title.setFont(titleFont);

        Region leftSpacer  = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer,  Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER);
        topRow.getChildren().addAll(backButton, leftSpacer, title, rightSpacer);

        VBox topSection = new VBox();
        topSection.getChildren().add(topRow);
        return topSection;
    }

    // ==================
    // Outfits grid (loaded from DB)
    // ==================

    private ScrollPane createOutfitsSection() {
        VBox leftColumn  = new VBox(25);
        VBox rightColumn = new VBox(25);

        List<Outfit> outfits = outfitController.getAllOutfits();

        if (outfits.isEmpty()) {
            Label empty = new Label("No outfits yet. Create your first outfit!");
            empty.setFont(normalFont);
            leftColumn.getChildren().add(empty);
        } else {
            for (int i = 0; i < outfits.size(); i++) {
                Outfit outfit = outfits.get(i);
                List<ClothingItem> items = outfitController.getItemsForOutfit(outfit.getId());

                ClothingItem item1 = items.size() > 0 ? items.get(0) : null;
                ClothingItem item2 = items.size() > 1 ? items.get(1) : null;

                VBox card = createOutfitCard(outfit.getName(), item1, item2);

                if (i % 2 == 0) {
                    leftColumn.getChildren().add(card);
                } else {
                    rightColumn.getChildren().add(card);
                }
            }
        }

        Region divider = new Region();
        divider.setPrefWidth(2);
        divider.setStyle("-fx-background-color: #EAD7BC;");

        HBox outfitsRow = new HBox(40);
        outfitsRow.getChildren().addAll(leftColumn, divider, rightColumn);

        ScrollPane scrollPane = new ScrollPane(outfitsRow);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: transparent;"
                + "-fx-background-color: transparent;");
        return scrollPane;
    }

    // ==================
    // Card builder
    // ==================

    private VBox createOutfitCard(String outfitName,
                                  ClothingItem item1,
                                  ClothingItem item2) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setPrefSize(580, 260);
        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAD7BC;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;");

        Label title = new Label(outfitName);
        title.setFont(outfitFont);

        Image plusImg = new Image(
                getClass().getResourceAsStream("/icons/plus.png"));
        ImageView plusIcon = new ImageView(plusImg);
        plusIcon.setFitWidth(40);
        plusIcon.setFitHeight(40);

        VBox miniCard1 = createMiniItemCard(item1);
        VBox miniCard2 = createMiniItemCard(item2);

        HBox itemsRow = new HBox(30);
        itemsRow.setAlignment(Pos.CENTER);
        itemsRow.getChildren().addAll(miniCard1, plusIcon, miniCard2);

        card.getChildren().addAll(title, itemsRow);
        return card;
    }

    private VBox createMiniItemCard(ClothingItem item) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefSize(220, 150);
        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAD7BC;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;");

        if (item == null) {
            Label placeholder = new Label("—");
            placeholder.setFont(buttonFont);
            card.getChildren().add(placeholder);
            return card;
        }

        Label name = new Label(item.getName());
        name.setFont(buttonFont);

        Label catLabel = new Label(item.getCategory().name());
        catLabel.setFont(normalFont);
        catLabel.setStyle(
                "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 12 6 12;"
                + "-fx-text-fill: white;");

        card.getChildren().addAll(name, catLabel);

        // Show first tag if available
        if (!item.getTags().isEmpty()) {
            String tagName = item.getTags().iterator().next().getName();
            Label tagChip = new Label(tagName);
            tagChip.setFont(normalFont);
            tagChip.setStyle(
                    "-fx-background-color: #F3EEE6;"
                    + "-fx-background-radius: 8;"
                    + "-fx-padding: 4 8 4 8;");
            card.getChildren().add(tagChip);
        }

        return card;
    }
}