package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.SearchController;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Search page — filters are loaded from the database (seeded defaults + any
 * user-created tags). Selection is tracked by real DB tag IDs.
 *
 * @author Bhavini Kopparam Ajay
 * @author Arshiya Tasneem Shaik
 */
public class SearchPage extends BorderPane {

    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN  = "#565448";
    private static final String CREAM       = "#FFFAF4";

    private final Stage stage;
    private FlowPane searchField;
    private VBox resultsBox;

    private final SearchController searchController = new SearchController();

    /** Real DB tag IDs currently selected by the user. */
    private final List<Integer> selectedTagIds = new ArrayList<>();

    private final Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);
    private final Font normalFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"), 13);
    private final Font subTitleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 15);
    private final Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 19);

    public SearchPage(Stage stage) {
        this.stage = stage;
        setStyle("-fx-background-color: " + CREAM + ";");

        resultsBox = new VBox(10);

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.getChildren().addAll(
                createTopSection(),
                createFiltersSection(),
                resultsBox
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        setCenter(scroll);
    }

    // ==================
    // Top section
    // ==================

    private VBox createTopSection() {
        Image backImg = new Image(getClass().getResourceAsStream("/icons/arrow.png"));
        ImageView backIcon = new ImageView(backImg);
        backIcon.setFitWidth(35);
        backIcon.setFitHeight(35);

        Button backButton = new Button();
        backButton.setGraphic(backIcon);
        backButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        backButton.setPrefSize(40, 42);
        backButton.setOnAction(e -> new MainPage().start(stage));

        // Search bar — shows chips for selected tags
        searchField = new FlowPane();
        searchField.setHgap(8);
        searchField.setVgap(8);
        searchField.setPrefHeight(45);
        searchField.setPadding(new Insets(5));
        searchField.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        HBox.setHgrow(searchField, javafx.scene.layout.Priority.ALWAYS);

        // Show All — returns everything (empty filter)
        Button showAllButton = new Button("Show All");
        showAllButton.setFont(normalBoldFont);
        showAllButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        showAllButton.setPrefSize(110, 40);
        showAllButton.setOnAction(e -> showResults(searchController.searchByTags(new ArrayList<>())));

        // Search — AND-filter with whatever tags are ticked
        Button searchButton = new Button("Search");
        searchButton.setFont(buttonFont);
        searchButton.setStyle(
                "-fx-background-color:" + DARK_GREEN + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-text-fill: white;");
        searchButton.setPrefSize(110, 40);
        searchButton.setOnAction(e -> showResults(searchController.searchByTags(selectedTagIds)));

        HBox searchRow = new HBox(10);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        searchRow.getChildren().addAll(backButton, searchField, showAllButton, searchButton);

        return new VBox(15, searchRow);
    }

    // ==================
    // Filters section — one row per TagCategory that has tags in the DB
    // ==================

    private VBox createFiltersSection() {
        VBox filters = new VBox(15);

        List<Tag> allTags = searchController.getAllTags();

        // Group tags by category, preserving enum declaration order
        Map<TagCategory, List<Tag>> grouped = new EnumMap<>(TagCategory.class);
        for (TagCategory cat : TagCategory.values()) {
            grouped.put(cat, new ArrayList<>());
        }
        for (Tag tag : allTags) {
            grouped.get(tag.getCategory()).add(tag);
        }

        // Pretty display names for each category row
        Map<TagCategory, String> labels = new LinkedHashMap<>();
        labels.put(TagCategory.OTHER,   "Type");
        labels.put(TagCategory.COLOR,   "Colour");
        labels.put(TagCategory.WEATHER, "Weather");
        labels.put(TagCategory.EVENT,   "Event");
        labels.put(TagCategory.DETAIL,  "Details");

        // Render in this preferred order: Type first, then the rest
        TagCategory[] order = {
            TagCategory.OTHER,
            TagCategory.COLOR,
            TagCategory.WEATHER,
            TagCategory.EVENT,
            TagCategory.DETAIL
        };

        for (TagCategory cat : order) {
            List<Tag> catTags = grouped.get(cat);
            if (catTags != null && !catTags.isEmpty()) {
                String label = labels.getOrDefault(cat, cap(cat.name()));
                filters.getChildren().add(createFilterRow(label, catTags));
            }
        }

        if (filters.getChildren().isEmpty()) {
            Label empty = new Label(
                    "No tags available. Add items via the + button on the home screen.");
            empty.setFont(normalFont);
            filters.getChildren().add(empty);
        }

        return filters;
    }

    /** One labelled row of toggle-buttons, one per tag. */
    private HBox createFilterRow(String categoryLabel, List<Tag> tags) {
        Label label = new Label(categoryLabel);
        label.setFont(subTitleFont);
        label.setPrefWidth(80);

        FlowPane tagPane = new FlowPane();
        tagPane.setHgap(10);
        tagPane.setVgap(8);

        for (Tag tag : tags) {
            ToggleButton btn = new ToggleButton(tag.getName());
            btn.setFont(normalBoldFont);
            styleTagButton(btn);

            final int tagId   = tag.getId();
            final String name = tag.getName();

            btn.selectedProperty().addListener((obs, oldVal, selected) -> {
                if (selected) {
                    if (!selectedTagIds.contains(tagId)) {
                        selectedTagIds.add(tagId);
                    }
                    addChip(name, tagId);
                } else {
                    selectedTagIds.remove(Integer.valueOf(tagId));
                    removeChip(tagId);
                }
            });

            tagPane.getChildren().add(btn);
        }

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(label, tagPane);
        return row;
    }

    // ==================
    // Chip management in the search bar
    // ==================

    private void addChip(String tagName, int tagId) {
        Label chip = new Label(tagName);
        chip.setFont(normalBoldFont);
        chip.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 4 10 4 10;");
        chip.setUserData(tagId);
        searchField.getChildren().add(chip);
    }

    private void removeChip(int tagId) {
        searchField.getChildren().removeIf(
                node -> Integer.valueOf(tagId).equals(node.getUserData()));
    }

    // ==================
    // Results display
    // ==================

    private void showResults(List<ClothingItem> items) {
        resultsBox.getChildren().clear();

        if (items == null || items.isEmpty()) {
            Label empty = new Label("No items match your selection.");
            empty.setFont(normalFont);
            resultsBox.getChildren().add(empty);
            return;
        }

        for (ClothingItem item : items) {
            VBox card = new VBox(6);
            card.setPadding(new Insets(12));
            card.setStyle(
                    "-fx-background-color: white;"
                    + "-fx-background-radius: 10;"
                    + "-fx-border-color: " + LIGHT_GREEN + ";"
                    + "-fx-border-radius: 10;");

            Label name = new Label(item.getName());
            name.setFont(normalBoldFont);

            Label cat = new Label(cap(item.getCategory().name()));
            cat.setFont(normalFont);
            cat.setStyle("-fx-text-fill: #888;");

            FlowPane chips = new FlowPane();
            chips.setHgap(8);
            chips.setVgap(6);
            for (Tag t : item.getTags()) {
                Label chip = new Label(t.getName());
                chip.setFont(normalFont);
                chip.setStyle(
                        "-fx-background-color: " + DARK_GREEN + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 4 10 4 10;"
                        + "-fx-text-fill: white;");
                chips.getChildren().add(chip);
            }

            card.getChildren().addAll(name, cat, chips);
            resultsBox.getChildren().add(card);
        }
    }

    // ==================
    // Helpers
    // ==================

    /** Capitalises the first letter and lowercases the rest of a string. */
    private String cap(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase()
                + s.substring(1).toLowerCase();
    }

    private void styleTagButton(ToggleButton btn) {
        String normal =
                "-fx-background-color: white;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;";
        String selected =
                "-fx-background-color:" + DARK_GREEN + ";"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;";

        btn.setStyle(normal);
        btn.selectedProperty().addListener((obs, oldVal, isSelected) ->
                btn.setStyle(isSelected ? selected : normal));
    }
}