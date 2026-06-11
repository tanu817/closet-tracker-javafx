package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.SearchController;
import com.mycompany.irr00_group_project.model.ClothingCategory;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Search page: filters are loaded from the database (seeded defaults + any
 * user-created tags). A Type row filters by {@link ClothingCategory}; all
 * other rows filter by tag ID. Both filters combine with AND logic.
 *
 * @author Bhavini Kopparam Ajay
 * @author Arshiya Tasneem Shaik
 */
public class SearchPage extends BorderPane {

    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN  = "#565448";
    private static final String CREAM       = "#FFFAF4";
    private static final String TEXT_COLOR  = "-fx-text-fill: #1a1a1a;";

    private final Stage stage;
    private FlowPane searchField;
    private VBox resultsBox;
    private Label noFilterWarning;

    private final SearchController searchController = new SearchController();

    /** Tag IDs selected via the tag filter rows. */
    private final List<Integer> selectedTagIds = new ArrayList<>();

    /**
     * The single ClothingCategory selected via the Type filter row,
     * or {@code null} when no type is selected.
     */
    private ClothingCategory selectedCategory = null;

    // Chip sentinel value used to identify the category chip in searchField
    private static final int CATEGORY_CHIP_SENTINEL = -999;

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

        noFilterWarning = new Label("Please select at least one tag.");
        noFilterWarning.setFont(normalFont);
        noFilterWarning.setStyle("-fx-text-fill: #c0392b;");
        noFilterWarning.setVisible(false);
        noFilterWarning.setManaged(false);

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.getChildren().addAll(
                createTopSection(),
                createFiltersSection(),
                noFilterWarning,
                resultsBox
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        setCenter(scroll);
    }


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
        backButton.setOnAction(e -> {
            MainPage mainPage = new MainPage();
            mainPage.start(stage, stage.getScene().getWidth(), stage.getScene().getHeight());
        });

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
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button showAllButton = new Button("Show All");
        showAllButton.setFont(normalBoldFont);
        showAllButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + TEXT_COLOR);
        showAllButton.setPrefSize(110, 40);
        showAllButton.setOnAction(e ->
                showResults(searchController.searchByCategoryAndTags(null, new ArrayList<>())));

        Button searchButton = new Button("Search");
        searchButton.setFont(buttonFont);
        searchButton.setStyle(
                "-fx-background-color:" + DARK_GREEN + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-text-fill: white;");
        searchButton.setPrefSize(110, 40);
        searchButton.setOnAction(e -> {
            if (selectedCategory == null && selectedTagIds.isEmpty()) {
                noFilterWarning.setVisible(true);
                noFilterWarning.setManaged(true);
                resultsBox.getChildren().clear();
                return;
            }
            noFilterWarning.setVisible(false);
            noFilterWarning.setManaged(false);
            showResults(searchController.searchByCategoryAndTags(
                    selectedCategory, new ArrayList<>(selectedTagIds)));
        });

        HBox searchRow = new HBox(10);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        searchRow.getChildren().addAll(backButton, searchField, showAllButton, searchButton);

        return new VBox(15, searchRow);
    }

    private VBox createFiltersSection() {
        VBox filters = new VBox(15);

        // Row 1: Type filter (ClothingCategory-based, not tag IDs) ──
        filters.getChildren().add(createTypeFilterRow());

        // Rows 2-N: Tag-based filters (Color, Weather, Event, Detail, Other) ──
        List<Tag> allTags = searchController.getAllTags();

        Map<TagCategory, List<Tag>> grouped = new EnumMap<>(TagCategory.class);
        for (TagCategory cat : TagCategory.values()) {
            grouped.put(cat, new ArrayList<>());
        }
        for (Tag tag : allTags) {
            // Skip the four seeded clothing-type tags (ids 1-4, category=OTHER).
            // Clothing type is handled via the Type row above using ClothingCategory.
            if (tag.getCategory() == TagCategory.OTHER && tag.getId() <= 4) {
                continue;
            }
            grouped.get(tag.getCategory()).add(tag);
        }

        Map<TagCategory, String> labels = new LinkedHashMap<>();
        labels.put(TagCategory.COLOR,   "Colour");
        labels.put(TagCategory.WEATHER, "Weather");
        labels.put(TagCategory.EVENT,   "Event");
        labels.put(TagCategory.DETAIL,  "Details");
        labels.put(TagCategory.OTHER,   "Other");

        TagCategory[] order = {
            TagCategory.COLOR,
            TagCategory.WEATHER,
            TagCategory.EVENT,
            TagCategory.DETAIL,
            TagCategory.OTHER
        };

        for (TagCategory cat : order) {
            List<Tag> catTags = grouped.get(cat);
            if (catTags != null && !catTags.isEmpty()) {
                String label = labels.getOrDefault(cat, cap(cat.name()));
                filters.getChildren().add(createTagFilterRow(label, catTags));
            }
        }

        return filters;
    }

    /**
     * Builds the "Type" row using {@link ClothingCategory} enum values.
     * Only TOP, BOTTOM, ONE_PIECE and ACCESSORY are shown (the four types
     * that were previously seeded as tags).
     *
     * <p>Buttons behave as a single-select toggle: clicking a selected
     * button deselects it (clears the category filter).</p>
     */
    private HBox createTypeFilterRow() {
        Label label = new Label("Type");
        label.setFont(subTitleFont);
        label.setPrefWidth(80);
        label.setStyle(TEXT_COLOR);

        FlowPane tagPane = new FlowPane();
        tagPane.setHgap(10);
        tagPane.setVgap(8);

        // The four clothing types shown in the Type filter
        ClothingCategory[] types = {
            ClothingCategory.TOP,
            ClothingCategory.BOTTOM,
            ClothingCategory.ONE_PIECE,
            ClothingCategory.ACCESSORY
        };
        String[] typeLabels = {"Top", "Bottom", "One Piece", "Accessory"};

        for (int i = 0; i < types.length; i++) {
            final ClothingCategory category = types[i];
            final String displayName = typeLabels[i];

            ToggleButton btn = new ToggleButton(displayName);
            btn.setFont(normalBoldFont);
            styleTagButton(btn);

            btn.selectedProperty().addListener((obs, oldVal, selected) -> {
                if (selected) {
                    selectedCategory = category;
                    // Remove old category chip (only one at a time)
                    removeCategoryChip();
                    addCategoryChip(displayName);
                } else {
                    // Only clear if this button was the one setting the category
                    if (selectedCategory == category) {
                        selectedCategory = null;
                        removeCategoryChip();
                    }
                }
            });

            tagPane.getChildren().add(btn);
        }

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(label, tagPane);
        return row;
    }

    /** One labelled row of toggle-buttons, one per DB tag. */
    private HBox createTagFilterRow(String categoryLabel, List<Tag> tags) {
        Label label = new Label(categoryLabel);
        label.setFont(subTitleFont);
        label.setPrefWidth(80);
        label.setStyle(TEXT_COLOR);

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
                    addTagChip(name, tagId);
                } else {
                    selectedTagIds.remove(Integer.valueOf(tagId));
                    removeTagChip(tagId);
                }
            });

            tagPane.getChildren().add(btn);
        }

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(label, tagPane);
        return row;
    }

    private void addTagChip(String tagName, int tagId) {
        Label chip = new Label(tagName);
        chip.setFont(normalBoldFont);
        chip.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 4 10 4 10;"
                + TEXT_COLOR);
        chip.setUserData(tagId);
        searchField.getChildren().add(chip);
    }

    private void removeTagChip(int tagId) {
        searchField.getChildren().removeIf(
                node -> Integer.valueOf(tagId).equals(node.getUserData()));
    }

    private void addCategoryChip(String displayName) {
        Label chip = new Label(displayName);
        chip.setFont(normalBoldFont);
        chip.setStyle(
                "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 4 10 4 10;"
                + "-fx-text-fill: white;");
        chip.setUserData(CATEGORY_CHIP_SENTINEL);
        searchField.getChildren().add(0, chip); // put type chip first
    }

    private void removeCategoryChip() {
        searchField.getChildren().removeIf(
                node -> Integer.valueOf(CATEGORY_CHIP_SENTINEL).equals(node.getUserData()));
    }


    private void showResults(List<ClothingItem> items) {
        resultsBox.getChildren().clear();

        if (items == null || items.isEmpty()) {
            Label empty = new Label("No items match your selection.");
            empty.setFont(normalFont);
            empty.setStyle(TEXT_COLOR);
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
            name.setStyle(TEXT_COLOR);

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


    private String cap(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private void styleTagButton(ToggleButton btn) {
        String normal =
                "-fx-background-color: white;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;"
                + "-fx-text-fill: #1a1a1a;";
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