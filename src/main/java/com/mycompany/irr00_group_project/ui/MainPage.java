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
 * @author Alexandra Varapai
 */
public class MainPage {

    private final DashboardController dashboardController = new DashboardController();

    private Stage stage;
    private Font titleFont;
    private Font buttonFont;
    private Font cardTitleFont;

    /**
     * Builds and shows the main page scene.
     *
     * @param stage the primary stage
     */
    public void start(Stage stage) {
        this.stage = stage;
        loadFonts();

        BorderPane root = new BorderPane();
        VBox topSection = buildTopSection();
        HBox cardsRow = buildCardsRow();

        root.setCenter(cardsRow);
        root.setMargin(cardsRow, new Insets(20, 0, 0, 0));
        root.setTop(topSection);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFAF4;");

        StackPane bottomBar = buildBottomBar(root);
        root.setBottom(bottomBar);
        BorderPane.setMargin(bottomBar, new Insets(20));

        StackPane stackRoot = buildStackRoot(root);

        Scene scene = new Scene(stackRoot, 1200, 700);
        stage.setTitle("Closet App");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the fonts used throughout the page.
     */
    private void loadFonts() {
        titleFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 28);
        buttonFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);
        cardTitleFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 20);
    }

    /**
     * Builds the top section containing title and navigation buttons.
     *
     * @return VBox with title and search/navigation row
     */
    private VBox buildTopSection() {
        Label title = new Label("Home");
        title.setFont(titleFont);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Button searchButton = buildSearchButton();
        Button itemsButton = buildNavButton("Items", "#565448", "white");
        itemsButton.setOnAction(e -> {
            ItemsPage itemsPage = new ItemsPage(stage);
            stage.setScene(new Scene(itemsPage, 1200, 700));
        });

        Button outfitsButton = buildNavButton("Outfits", "#D8CFBC", "black");
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
        topSection.getChildren().addAll(title, searchRow);
        return topSection;
    }

    /**
     * Builds the search-bar button that navigates to the search page.
     *
     * @return styled search Button
     */
    private Button buildSearchButton() {
        Image searchImg = new Image(
                getClass().getResourceAsStream("/icons/Search.png"));
        ImageView searchIcon = new ImageView(searchImg);
        searchIcon.setFitWidth(30);
        searchIcon.setFitHeight(30);

        Button btn = new Button("     Search by tags...");
        btn.setGraphic(searchIcon);
        btn.setPrefWidth(850);
        btn.setPrefHeight(60);
        btn.setFont(buttonFont);
        btn.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 10;"
                + "-fx-alignment: CENTER-LEFT;");
        btn.setOnAction(e -> {
            SearchPage searchPage = new SearchPage(stage);
            stage.setScene(new Scene(searchPage, 1200, 700));
        });
        return btn;
    }

    /**
     * Builds a generic navigation button.
     *
     * @param label button text
     * @param bg background color hex string
     * @param fg text fill color string
     * @return styled Button
     */
    private Button buildNavButton(String label, String bg, String fg) {
        Button btn = new Button(label);
        btn.setPrefSize(150, 58);
        btn.setFont(buttonFont);
        btn.setStyle(
                "-fx-background-color: " + bg + ";"
                + "-fx-text-fill: " + fg + ";"
                + "-fx-background-radius: 10;");
        return btn;
    }

    /**
     * Builds the dashboard cards row (Favorites, Most Worn, Least Worn).
     *
     * @return HBox containing the three cards
     */
    private HBox buildCardsRow() {
        VBox favoritesCard = buildCard(
                "Favorites", "/icons/heart.png",
                dashboardController.getTop5Favourites(),
                25, 25, false);

        VBox mostWornCard = buildCard(
                "Most Worn", "/icons/hanger.png",
                dashboardController.getTop5Worn(),
                30, 30, true);

        VBox leastWornCard = buildCard(
                "Least Worn", "/icons/hanger.png",
                dashboardController.getLeast5Worn(),
                30, 30, true);

        HBox cardsRow = new HBox(20);
        cardsRow.setAlignment(Pos.CENTER);
        cardsRow.getChildren().addAll(favoritesCard, mostWornCard, leastWornCard);
        return cardsRow;
    }

    /**
     * Builds the progress bar section showing wardrobe usage.
     *
     * @return VBox containing title, bar, and stats label
     */
    private VBox buildProgressSection() {
        int totalItems = dashboardController.getTotalItems();
        int wornItems = dashboardController.getItemsWornMoreThanOnce();
        double progress = dashboardController.getWardrobeUsageProgress();
        int percentage = (int) (progress * 100);

        Label progressTitle = new Label("Wardrobe Usage Overview");
        progressTitle.setFont(cardTitleFont);

        Label progressStats = new Label(
                wornItems + "/" + totalItems + " items  —  " + percentage + "%");
        progressStats.setFont(buttonFont);

        javafx.scene.control.ProgressBar progressBar
                = new javafx.scene.control.ProgressBar(progress);
        progressBar.setPrefWidth(860);
        progressBar.setPrefHeight(10);
        progressBar.setStyle(
                "-fx-accent: #565448;"
                + "-fx-background-color: #D8CFBC;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");

        VBox section = new VBox(10);
        section.getChildren().addAll(progressTitle, progressBar, progressStats);
        javafx.scene.layout.HBox.setHgrow(section, javafx.scene.layout.Priority.ALWAYS);
        section.setAlignment(Pos.CENTER);
        return section;
    }

    /**
     * Builds the bottom bar containing the progress section and add-item
     * button.
     *
     * @param root the root BorderPane (used for overlay wiring)
     * @return StackPane bottom bar
     */
    private StackPane buildBottomBar(BorderPane root) {
        VBox progressSection = buildProgressSection();

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

        StackPane bar = new StackPane();
        StackPane.setAlignment(progressSection, Pos.CENTER);
        StackPane.setAlignment(addButton, Pos.CENTER_RIGHT);
        bar.getChildren().addAll(progressSection, addButton);

        // Store addButton for use by buildStackRoot
        addButton.setUserData("addButton");
        return bar;
    }

    /**
     * Builds the root StackPane with the overlay for adding items.
     *
     * @param root the border pane already populated with main content
     * @return StackPane wrapping root, dim layer, and add-item overlay
     */
    private StackPane buildStackRoot(BorderPane root) {
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

        // Wire the add button found in the bottom bar
        StackPane bottomBar = (StackPane) root.getBottom();
        Button addButton = (Button) bottomBar.getChildren().stream()
                .filter(n -> "addButton".equals(n.getUserData()))
                .findFirst()
                .orElse(null);

        if (addButton != null) {
            addButton.setOnAction(e -> {
                dimBackground.setVisible(true);
                overlay.setVisible(true);
            });
        }
        StackPane stackRoot = new StackPane();
        stackRoot.getChildren().addAll(root, dimBackground, overlay);
        return stackRoot;
    }

    /**
     * Builds a dashboard card showing up to 5 clothing items.
     *
     * @param titleText card heading
     * @param iconPath resource path for the card icon
     * @param items list of items to display (up to 5)
     * @param iconW icon width in pixels
     * @param iconH icon height in pixels
     * @param numbered whether to prefix each row with a rank number
     * @return styled card VBox
     */
    private VBox buildCard(
            String titleText,
            String iconPath,
            List<ClothingItem> items,
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
            empty.setFont(buttonFont);
            card.getChildren().add(empty);
        } else {
            int rank = 1;
            for (ClothingItem item : items) {
                String text = numbered ? rank + ".  " + item.getName() : item.getName();
                Label row = new Label(text);
                row.setFont(buttonFont);
                card.getChildren().add(row);
                rank++;
            }
        }

        return card;
    }
}
