package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.DashboardController;
import com.mycompany.irr00_group_project.model.ClothingItem;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main page / dashboard of the app. Dashboard cards are loaded from real CSV
 * data via {@link DashboardController}.
 *
 * @author Bhavini Kopparam Ajay
 */
public class MainPage {

    private final DashboardController dashboardController = new DashboardController();

    /**
     * Builds and shows the main page scene.
     *
     * @param stage the primary stage
     */
    public void start(Stage stage) {
        //BorderPane root = new BorderPane();

        Font titleFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 28);
        Font buttonFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);

        Label title = new Label("Home");
        title.setFont(titleFont);

        // SEARCH BUTTON
        Image searchImg = new Image(
                getClass().getResourceAsStream("/icons/Search.png"));
        ImageView searchIcon = new ImageView(searchImg);
        searchIcon.setFitWidth(30);
        searchIcon.setFitHeight(30);

        Button searchButton = new Button("     Search by tags...");
        searchButton.setGraphic(searchIcon);
        searchButton.setPrefWidth(850);
        searchButton.setPrefHeight(60);
        searchButton.setFont(buttonFont);
        searchButton.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 10;"
                + "-fx-alignment: CENTER-LEFT;");
        searchButton.setOnAction(e -> {
            SearchPage searchPage = new SearchPage(stage);
            stage.setScene(new Scene(searchPage, 1200, 700));
        });

        // ITEMS BUTTON
        Button itemsButton = new Button("Items");
        itemsButton.setPrefSize(150, 58);
        itemsButton.setFont(buttonFont);
        itemsButton.setStyle(
                "-fx-background-color: #565448;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 10;");
        itemsButton.setOnAction(e -> {
            ItemsPage itemsPage = new ItemsPage(stage);
            stage.setScene(new Scene(itemsPage, 1200, 700));
        });

        // OUTFITS BUTTON
        Button outfitsButton = new Button("Outfits");
        outfitsButton.setPrefSize(150, 58);
        outfitsButton.setFont(buttonFont);
        outfitsButton.setStyle(
                "-fx-background-color: #D8CFBC;"
                + "-fx-background-radius: 10;");
        outfitsButton.setOnAction(e -> {
            OutfitsPage outfitsPage = new OutfitsPage(stage);
            stage.setScene(new Scene(outfitsPage, 1200, 700));
        });

        HBox searchRow = new HBox(15);
        searchRow.getChildren().addAll(searchButton, itemsButton, outfitsButton);

        VBox topSection = new VBox(15);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        topSection.getChildren().addAll(title, searchRow);

        // DASHBOARD CARDS — loaded from real data
        Font cardTitleFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 20);

        VBox favoritesCard = buildCard(
                "Favorites", "/icons/heart.png",
                dashboardController.getTop5Favourites(),
                cardTitleFont, buttonFont, 25, 25, false);

        VBox mostWornCard = buildCard(
                "Most Worn", "/icons/hanger.png",
                dashboardController.getTop5Worn(),
                cardTitleFont, buttonFont, 30, 30, true);

        VBox leastWornCard = buildCard(
                "Least Worn", "/icons/hanger.png",
                dashboardController.getLeast5Worn(),
                cardTitleFont, buttonFont, 30, 30, true);

        HBox cardsRow = new HBox(20);
        cardsRow.setAlignment(Pos.CENTER);
        cardsRow.getChildren().addAll(favoritesCard, mostWornCard, leastWornCard);

        BorderPane root = new BorderPane();

        root.setCenter(cardsRow);
        root.setMargin(cardsRow, new Insets(20, 0, 0, 0));

        // ADD ITEM OVERLAY
        AddItemsPage addPage = new AddItemsPage();
        VBox overlay = addPage.createAddItemsOverlay();

        Region dimBackground = new Region();
        dimBackground.setStyle("-fx-background-color: rgba(0,0,0,0.35);");
        dimBackground.setVisible(false);
        dimBackground.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        addPage.getBackButton().setOnAction(e -> {
            overlay.setVisible(false);
            dimBackground.setVisible(false);
        });

        overlay.setVisible(false);
        overlay.setMaxWidth(850);
        overlay.setMaxHeight(560);
        StackPane.setAlignment(overlay, Pos.CENTER);

        // PLUS BUTTON
        Image plusImg = new Image(
                getClass().getResourceAsStream("/icons/plus.png"));
        ImageView plusIcon = new ImageView(plusImg);
        plusIcon.setFitWidth(25);
        plusIcon.setFitHeight(25);

        Button addButton = new Button();
        addButton.setGraphic(plusIcon);
        addButton.setPrefSize(60, 60);
        addButton.setStyle(
                "-fx-background-color: #D8CFBC;"
                + "-fx-background-radius: 30;"
                + "-fx-border-radius: 30;");
        addButton.setOnAction(e -> {
            dimBackground.setVisible(true);
            overlay.setVisible(true);
        });

        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBar.getChildren().add(addButton);

        root.setBottom(bottomBar);
        BorderPane.setMargin(bottomBar, new Insets(20));
        root.setTop(topSection);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFAF4;");

        StackPane stackRoot = new StackPane();
        stackRoot.getChildren().addAll(root, dimBackground, overlay);

        Scene scene = new Scene(stackRoot, 1200, 700);
        stage.setTitle("Closet App");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Builds a dashboard card showing up to 5 clothing items.
     *
     * @param titleText card heading
     * @param iconPath resource path for the card icon
     * @param items list of items to display (up to 5)
     * @param cardTitleFont font for the card heading
     * @param itemFont font for item label rows
     * @param iconW icon width in pixels
     * @param iconH icon height in pixels
     * @param numbered whether to prefix each row with a rank number
     * @return styled card VBox
     */
    private VBox buildCard(
            String titleText,
            String iconPath,
            List<ClothingItem> items,
            Font cardTitleFont,
            Font itemFont,
            int iconW,
            int iconH,
            boolean numbered) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setPrefWidth(320);
        card.setPrefHeight(330);
        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 12;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 12;");

        Image iconImage = new Image(getClass().getResourceAsStream(iconPath));
        ImageView icon = new ImageView(iconImage);
        icon.setFitWidth(iconW);
        icon.setFitHeight(iconH);

        Label titleLabel = new Label(titleText);
        titleLabel.setFont(cardTitleFont);

        HBox titleRow = new HBox(10);
        titleRow.getChildren().addAll(icon, titleLabel);
        card.getChildren().add(titleRow);

        if (items == null || items.isEmpty()) {
            Label empty = new Label("No items yet.");
            empty.setFont(itemFont);
            card.getChildren().add(empty);
        } else {
            int rank = 1;
            for (ClothingItem item : items) {
                String text = numbered ? rank + ".  " + item.getName() : item.getName();
                Label row = new Label(text);
                row.setFont(itemFont);
                card.getChildren().add(row);
                rank++;
            }
        }

        return card;
    }
}
