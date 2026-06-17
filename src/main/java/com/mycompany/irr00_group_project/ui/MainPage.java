package com.mycompany.irr00_group_project.ui;

import java.util.List;

import com.mycompany.irr00_group_project.controller.DashboardController;
import com.mycompany.irr00_group_project.model.ClothingItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main page / dashboard of the app. Dashboard cards are loaded from real CSV
 * data via {@link DashboardController}.
 *
 * @author Bhavini Kopparam Ajay
 * @author Alexandra Varapai
 * @author Tanusri Madhalam
 */
public class MainPage {

    private final DashboardController dashboardController = new DashboardController();
    private PieChart pieChart;

    /**
     * Builds and shows the main page scene.
     *
     * @param stage the primary stage
     */
    public void start(Stage stage) {

        Font titleFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 28);
        Font buttonFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);
        Font cardTitleFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 20);

        VBox topSection = buildTopSection(stage, titleFont, buttonFont);
        HBox cardsRow = buildDashboardCards(cardTitleFont, buttonFont);
        StackPane bottomBar = buildBottomBar(cardTitleFont, buttonFont);

        BorderPane root = new BorderPane();
        root.setCenter(cardsRow);
        root.setMargin(cardsRow, new Insets(20, 0, 0, 0));
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

    // Overlay + dim background are built once and reused by buildBottomBar(),
    // but must exist before start() assembles the StackPane root.
    private final AddItemsPage addPage = new AddItemsPage();
    private final VBox overlay = addPage.createAddItemsOverlay();
    private final Region dimBackground = new Region();

    /**
     * Builds the title row and the search/items/outfits navigation row.
     *
     * @param stage the primary stage, used for page navigation
     * @param titleFont font for the page title
     * @param buttonFont font for the nav buttons
     * @return the assembled top section
     */
    private VBox buildTopSection(Stage stage, Font titleFont, Font buttonFont) {

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

        return topSection;
    }

    /**
     * Builds the row of dashboard cards (favorites + stats, most worn, least worn).
     *
     * @param cardTitleFont font for card headings
     * @param buttonFont font for item label rows
     * @return the assembled cards row
     */
    private HBox buildDashboardCards(Font cardTitleFont, Font buttonFont) {

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

        VBox statsCard = createStatsCard(cardTitleFont);

        VBox leftColumn = new VBox(20);
        leftColumn.getChildren().addAll(favoritesCard, statsCard);

        HBox cardsRow = new HBox(20);
        cardsRow.setAlignment(Pos.CENTER);
        cardsRow.getChildren().addAll(leftColumn, mostWornCard, leastWornCard);

        return cardsRow;
    }

    /**
     * Builds the bottom bar: wardrobe usage progress section, the add-item
     * overlay wiring, and the plus button that triggers it.
     *
     * @param cardTitleFont font for the progress section title
     * @param buttonFont font for stats text and overlay controls
     * @return the assembled bottom bar
     */
    private StackPane buildBottomBar(Font cardTitleFont, Font buttonFont) {

        VBox progressSection = buildProgressSection(cardTitleFont, buttonFont);
        Button addButton = buildAddButton();

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

        addButton.setOnAction(e -> {
            dimBackground.setVisible(true);
            overlay.setVisible(true);
        });

        StackPane bottomBar = new StackPane();
        StackPane.setAlignment(progressSection, Pos.CENTER);
        StackPane.setAlignment(addButton, Pos.CENTER_RIGHT);
        bottomBar.getChildren().addAll(progressSection, addButton);

        return bottomBar;
    }

    /**
     * Builds the wardrobe usage progress bar and its labels.
     *
     * @param cardTitleFont font for the section title
     * @param buttonFont font for the stats label
     * @return the assembled progress section
     */
    private VBox buildProgressSection(Font cardTitleFont, Font buttonFont) {

        int totalItems = dashboardController.getTotalItems();
        int wornItems = dashboardController.getItemsWornMoreThanOnce();
        double progress = dashboardController.getWardrobeUsageProgress();
        int percentage = (int) (progress * 100);

        Label progressTitle = new Label("Wardrobe Usage Overview");
        progressTitle.setFont(cardTitleFont);

        Label progressStats = new Label(wornItems + "/" + totalItems + " items  —  "
            + percentage + "%");
        progressStats.setFont(buttonFont);

        javafx.scene.control.ProgressBar progressBar =
            new javafx.scene.control.ProgressBar(progress);
        progressBar.setPrefWidth(860);
        progressBar.setPrefHeight(10);
        progressBar.setStyle(
                "-fx-accent: #565448;"
                + "-fx-background-color: #D8CFBC;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");

        VBox progressSection = new VBox(10);
        progressSection.getChildren().addAll(progressTitle, progressBar, progressStats);
        HBox.setHgrow(progressSection, javafx.scene.layout.Priority.ALWAYS);
        progressSection.setAlignment(Pos.CENTER);

        return progressSection;
    }

    /**
     * Builds the circular plus button used to open the add-item overlay.
     *
     * @return the assembled add button (action handler attached by caller)
     */
    private Button buildAddButton() {

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

        return addButton;
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

        if (titleText.equals("Favorites")) {
            card.setPrefHeight(250);
        } else {
            card.setPrefHeight(330);
        }

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

    /**
     * Creates a card for the statistics pie chart.
     *
     * @param cardTitleFont for the title.
     * @return return the card used to display stats.
     */
    private VBox createStatsCard(Font cardTitleFont) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setPrefWidth(320);
        card.setPrefHeight(200);

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 12;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 12;");

        Label title = new Label("Item Stats");
        title.setFont(cardTitleFont);

        pieChart = new PieChart();
        pieChart.setLabelsVisible(true);

        updatePieChart();

        card.getChildren().addAll(title, pieChart);

        return card;

    }

    /**
     * Method to update pie chart in dashboard.
     */
    private void updatePieChart() {
        int tops = 0;
        int bottoms = 0;
        int accessories = 0;
        int onePiece = 0;

        List<ClothingItem> allItems = dashboardController.getAllItemsWithTags();

        for (ClothingItem item : allItems) {
            String category = item.getCategory().toString();

            if (category.equalsIgnoreCase("TOP")) {
                tops++;
            } else if (category.equalsIgnoreCase("BOTTOM")) {
                bottoms++;
            } else if (category.equalsIgnoreCase("ACCESSORY")) {
                accessories++;
            } else if (category.equalsIgnoreCase("ONE_PIECE")
                    || category.equalsIgnoreCase("ONEPIECE")
                    || category.equalsIgnoreCase("ONE PIECE")) {
                onePiece++;
            }
        }

        pieChart.getData().clear();

        PieChart.Data topsSlice = new PieChart.Data("Tops", tops);
        PieChart.Data bottomsSlice = new PieChart.Data("Bottoms", bottoms);
        PieChart.Data accessoriesSlice = new PieChart.Data("Accessories", accessories);
        PieChart.Data onePieceSlice = new PieChart.Data("One Piece", onePiece);

        pieChart.getData().addAll(topsSlice, bottomsSlice, accessoriesSlice, onePieceSlice);

        // Apply custom colors after chart renders
        applyPieChartColors();

    }

    /**
     * To change colors of the pie chart.
     */
    private void applyPieChartColors() {
        // Delay needed so nodes exist
        javafx.application.Platform.runLater(() -> {
            if (pieChart.getData().size() >= 4) {
                pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #565448;"); 
                pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #D8CFBC;"); 
                pieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #A9A48E;"); 
                pieChart.getData().get(3).getNode().setStyle("-fx-pie-color: #EDE6D6;"); 
            }
        });
    }
}