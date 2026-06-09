package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.DashboardController;
import com.mycompany.irr00_group_project.controller.ViewItemController;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
 * Items page — shows all clothing items loaded from CSV storage.
 * Supports toggling favourites, marking items as worn, and viewing
 * matching items.
 *
 * @author Bhavini Kopparam Ajay
 */
public class ItemsPage extends BorderPane {

    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN  = "#565448";
    private static final String CREAM       = "#FFFAF4";

    private FlowPane itemsGrid;
    private final Stage stage;

    private final Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);
    private final Font normalFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"), 13);
    private final Font titleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 24);
    private final Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 19);
    private final Font smallFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"), 11);

    private final DashboardController dashboardController = new DashboardController();
    private final ViewItemController viewItemController = new ViewItemController();

    public ItemsPage(Stage stage) {
        this.stage = stage;
        setStyle("-fx-background-color: " + CREAM + ";");

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.getChildren().addAll(createTopSection(), createItemsSection());
        setCenter(root);
    }

    // ==================
    // Items grid (loaded from DB)
    // ==================

    private ScrollPane createItemsSection() {
        itemsGrid = new FlowPane();
        itemsGrid.setAlignment(Pos.TOP_LEFT);
        itemsGrid.setHgap(20);
        itemsGrid.setVgap(20);
        itemsGrid.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(itemsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: transparent;"
                + "-fx-background-color: transparent;");

        loadItemsFromDb();

        return scrollPane;
    }

    /**
     * Loads all clothing items with their tags from the repository and
     * renders them as cards.
     */
    private void loadItemsFromDb() {
        itemsGrid.getChildren().clear();
        List<ClothingItem> items = dashboardController.getAllItemsWithTags();

        if (items.isEmpty()) {
            Label empty = new Label("No items in your closet yet. Add some!");
            empty.setFont(normalFont);
            itemsGrid.getChildren().add(empty);
            return;
        }

        for (ClothingItem item : items) {
            itemsGrid.getChildren().add(createItemCard(item));
        }
    }

    // ==================
    // Card builder
    // ==================

    private VBox createItemCard(ClothingItem item) {
        VBox card = new VBox(12);
        card.setPrefWidth(460);
        card.setPadding(new Insets(18));
        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAE4D8;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");

        // TOP ROW: name + favourite heart
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(item.getName());
        nameLabel.setFont(buttonFont);
        nameLabel.setStyle("-fx-text-fill: black;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Favourite heart button
        Image heartImg = new Image(
                getClass().getResourceAsStream("/icons/heart.png"));
        ImageView heart = new ImageView(heartImg);
        heart.setFitWidth(26);
        heart.setFitHeight(26);
        heart.setOpacity(item.isFavorite() ? 1.0 : 0.3);

        Button heartBtn = new Button();
        heartBtn.setGraphic(heart);
        heartBtn.setStyle(
                "-fx-background-color: transparent;"
                + "-fx-cursor: hand;");
        heartBtn.setOnAction(e -> {
            viewItemController.toggleFavourite(item);
            heart.setOpacity(item.isFavorite() ? 1.0 : 0.3);
        });

        topRow.getChildren().addAll(nameLabel, spacer, heartBtn);

        // CATEGORY label
        Label catLabel = new Label(cap(item.getCategory().name()));
        catLabel.setFont(smallFont);
        catLabel.setStyle(
                "-fx-text-fill: white;"
                + "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-background-radius: 6;"
                + "-fx-padding: 3 8 3 8;");

        // TAGS row
        FlowPane tagsPane = new FlowPane();
        tagsPane.setHgap(8);
        tagsPane.setVgap(6);
        for (Tag tag : item.getTags()) {
            tagsPane.getChildren().add(createTagChip(tag.getName()));
        }

        // WORN COUNT label
        Label wornLabel = new Label("Worn: " + item.getWornCount() + " time(s)");
        wornLabel.setFont(smallFont);
        wornLabel.setStyle("-fx-text-fill: #888;");

        // ACTION BUTTONS ROW
        Button wornBtn = new Button("Mark as Worn");
        wornBtn.setFont(normalBoldFont);
        wornBtn.setStyle(
                "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 14 6 14;");
        wornBtn.setOnAction(e -> {
            viewItemController.markAsWorn(item.getId());
            item.wear(); // update in-memory count
            wornLabel.setText("Worn: " + item.getWornCount() + " time(s)");
        });

        Button matchBtn = new Button("Show Matches");
        matchBtn.setFont(normalBoldFont);
        matchBtn.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 14 6 14;");
        matchBtn.setOnAction(e -> showMatchingItems(item));

        // FIX Issue 4: Delete button — removes item from CSV and refreshes grid
        Button deleteBtn = new Button("Remove");
        deleteBtn.setFont(normalBoldFont);
        deleteBtn.setStyle(
                "-fx-background-color: #c0392b;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 14 6 14;");
        deleteBtn.setOnAction(e -> {
            viewItemController.deleteItem(item.getId());
            loadItemsFromDb(); // refresh grid after deletion
        });

        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        actionRow.getChildren().addAll(wornBtn, matchBtn, deleteBtn, wornLabel);

        card.getChildren().addAll(topRow, catLabel, tagsPane, actionRow);
        return card;
    }

    // ==================
    // Matching items popup
    // ==================

    private void showMatchingItems(ClothingItem item) {
        List<ClothingItem> matches = viewItemController.getMatchingItems(item);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Matching Items");
        alert.setHeaderText("Items that match: " + item.getName());

        if (matches.isEmpty()) {
            alert.setContentText("No matching items found. Add some tops or bottoms to see suggestions.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (ClothingItem match : matches) {
                sb.append("• ").append(match.getName())
                  .append(" (").append(cap(match.getCategory().name())).append(")\n");
            }
            alert.setContentText(sb.toString());
        }

        alert.showAndWait();
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
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        backButton.setOnAction(e -> {
            MainPage mainPage = new MainPage();
            mainPage.start(stage);
        });

        Label title = new Label("All Items");
        title.setFont(titleFont);

        Region leftSpacer  = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer,  Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER);
        topRow.getChildren().addAll(backButton, leftSpacer, title, rightSpacer);

        VBox topSection = new VBox(20);
        topSection.getChildren().add(topRow);
        return topSection;
    }

    // ==================
    // Helpers
    // ==================

    private Label createTagChip(String text) {
        Label chip = new Label(text);
        chip.setFont(normalFont);
        chip.setStyle(
                "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 4 10 4 10;"
                + "-fx-text-fill: white;");
        return chip;
    }

    /** Capitalises first letter, lowercases rest. */
    private String cap(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
