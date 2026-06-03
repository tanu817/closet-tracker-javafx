package com.mycompany.irr00_group_project.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

//to run: mvn clean javafx:run
/**
 * Search page of the closet tracker app.
 *
 * @author Arshiya Shaik
 */
public class SearchPage extends BorderPane {

    // COLOR PALETTE
    private static final String GREEN = "#606c38";
    private static final String DARK_GREEN = "#283618";
    private static final String CREAM = "#fefae0";
    private static final String GOLD = "#dda15e";
    private static final String BROWN = "#bc6c25";

    // UI COMPONENTS
    private TextField searchField;
    private FlowPane tagPane;
    private FlowPane masonry;
    private Label emptyLabel;

    private ImageView imagePreview;

    // DATA
    private final List<Item> allItems = new ArrayList<>();

    /**
     * Set up the search page.
     */
    public SearchPage() {
        setStyle("-fx-background-color: " + CREAM + ";");

        setTop(createStickySearchBar());
        setCenter(createResultsArea());

        loadMockData();
        renderItems(allItems);
    }

    // TOP SEARCH BAR 
    private VBox createStickySearchBar() {

        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: " + CREAM + ";");

        // SEARCH BAR
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search items...");
        searchField.setPrefWidth(350);
        searchField.setStyle(inputStyle());

        Button searchBtn = new Button("Search");
        searchBtn.setStyle(buttonStyle());

        searchBtn.setOnAction(e -> applySearch());

        searchBar.getChildren().addAll(searchField, searchBtn);

        // TAG CHIPS
        tagPane = new FlowPane();
        tagPane.setHgap(8);
        tagPane.setVgap(8);
        tagPane.setPadding(new Insets(5));

        addTag("Sunny");
        addTag("Rainy");
        addTag("Top");
        addTag("Bottom");
        addTag("One Piece");
        addTag("Accessory");
        addTag("Casual");
        addTag("Party");
        addTag("Minimal");
        addTag("Streetwear");

        container.getChildren().addAll(searchBar, tagPane);

        return container;
    }

    // RESULTS AREA (Pinterest masonry)
    private VBox createResultsArea() {

        VBox container = new VBox(10);
        container.setPadding(new Insets(15));

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        masonry = new FlowPane();
        masonry.setHgap(15);
        masonry.setVgap(15);
        masonry.setPrefWrapLength(900);

        scroll.setContent(masonry);

        emptyLabel = new Label("No items match your filters");
        emptyLabel.setStyle("-fx-text-fill: " + DARK_GREEN + "; -fx-font-size: 16px;");
        emptyLabel.setVisible(false);

        container.getChildren().addAll(scroll, emptyLabel);

        return container;
    }

    // TAG PILLS
    private void addTag(String name) {
        ToggleButton pill = new ToggleButton(name);

        pill.setStyle(pillStyle(false));

        pill.selectedProperty().addListener((obs, oldVal, newVal) -> {
            pill.setStyle(pillStyle(newVal));
            applySearch();
        });

        tagPane.getChildren().add(pill);
    }

    private String pillStyle(boolean selected) {
        if (selected) {
            return """
                -fx-background-color: #606c38;
                -fx-text-fill: white;
                -fx-background-radius: 18;
                -fx-padding: 6 12;
                """;
        }

        return """
            -fx-background-color: #dda15e;
            -fx-text-fill: #283618;
            -fx-background-radius: 18;
            -fx-padding: 6 12;
            """;
    }

    // SEARCH LOGIC
    private void applySearch() {

        String query = searchField.getText().toLowerCase();

        List<Item> filtered = new ArrayList<>();

        for (Item item : allItems) {

            boolean matchesText = item.name.toLowerCase().contains(query);

            boolean matchesTags = tagPane.getChildren().stream()
                    .filter(n -> ((ToggleButton) n).isSelected())
                    .map(n -> ((ToggleButton) n).getText().toLowerCase())
                    .allMatch(item.tags::contains);

            if (matchesText && matchesTags) {
                filtered.add(item);
            }
        }

        renderItems(filtered);
    }

    // RENDER (Pinterest Masonry)
    private void renderItems(List<Item> items) {

        masonry.getChildren().clear();

        if (items.isEmpty()) {
            emptyLabel.setVisible(true);
            return;
        }

        emptyLabel.setVisible(false);

        for (Item item : items) {
            VBox card = createCard(item);

            card.setOpacity(0);
            masonry.getChildren().add(card);

            fadeIn(card);
        }
    }

    // ITEM CARD
    private VBox createCard(Item item) {

        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(160);

        card.setStyle("""
            -fx-background-color: #fefae0;
            -fx-border-color: #dda15e;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            """);

        ImageView img = new ImageView(item.image);
        img.setFitWidth(120);
        img.setFitHeight(120);
        img.setPreserveRatio(true);

        Label name = new Label(item.name);
        name.setStyle("-fx-text-fill: #283618; -fx-font-weight: bold;");

        card.getChildren().addAll(img, name);

        // HOVER EFFECT
        card.setOnMouseEntered(e -> card.setScaleX(1.05));
        card.setOnMouseEntered(e -> card.setScaleY(1.05));
        card.setOnMouseExited(e -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });

        // CLICK → OUTFIT BUILDER
        card.setOnMouseClicked(e -> openOutfitBuilder(item));

        return card;
    }

    // FADE ANIMATION
    private void fadeIn(javafx.scene.Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(250), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    // OUTFIT BUILDER POPUP
    private void openOutfitBuilder(Item item) {

        Stage stage = new Stage();

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: " + CREAM + ";");

        Label title = new Label("Outfit Builder");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: " + DARK_GREEN + ";");

        ImageView img = new ImageView(item.image);
        img.setFitWidth(150);
        img.setFitHeight(150);

        Button top = new Button("Add as Top");
        Button bottom = new Button("Add as Bottom");
        Button save = new Button("Save Outfit");

        top.setStyle(buttonStyle());
        bottom.setStyle(buttonStyle());
        save.setStyle(buttonStyle());

        root.getChildren().addAll(title, img, top, bottom, save);

        stage.setScene(new Scene(root, 300, 400));
        stage.show();
    }

    // MOCK DATA - ned to sync with the tags in the UI
    private void loadMockData() {

        allItems.add(new Item("Blue Shirt", List.of("top", "casual"),
                new Image("https://via.placeholder.com/120")));

        allItems.add(new Item("Black Jeans", List.of("bottom", "streetwear"),
                new Image("https://via.placeholder.com/120")));

        allItems.add(new Item("Red Dress", List.of("one piece", "party"),
                new Image("https://via.placeholder.com/120")));
    }

    // ITEM CLASS
    private static class Item {

        String name;
        List<String> tags;
        Image image;

        Item(String name, List<String> tags, Image image) {
            this.name = name;
            this.tags = tags;
            this.image = image;
        }
    }

    // STYLES
    private String buttonStyle() {
        return """
            -fx-background-color: #606c38;
            -fx-text-fill: white;
            -fx-background-radius: 12;
            -fx-padding: 8 12;
            """;
    }

    private String inputStyle() {
        return """
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: #283618;
            -fx-padding: 6 10;
            """;
    }
}
