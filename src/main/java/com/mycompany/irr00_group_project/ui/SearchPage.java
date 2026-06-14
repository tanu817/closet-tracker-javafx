package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.SearchController;
import com.mycompany.irr00_group_project.controller.ViewItemController;
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
import javafx.scene.control.Alert;
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
 * user-created tags). A Type row filters by {@link ClothingCategory}; all other
 * rows filter by tag ID. Both filters combine with AND logic.
 *
 * <p>
 * Each result card has a "Mark as Worn" button. If the item is a TOP or BOTTOM,
 * matching items of the opposite category are shown inline; each match has a
 * "Create Outfit" button.</p>
 *
 * @author Bhavini Kopparam Ajay
 * @author Arshiya Tasneem Shaik
 */
public class SearchPage extends BorderPane {

    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN = "#565448";
    private static final String CREAM = "#FFFAF4";
    private static final String TEXT_COLOR = "-fx-text-fill: #1a1a1a;";

    private final Stage stage;
    private FlowPane searchField;
    private VBox resultsBox;
    private Label noFilterWarning;

    private final SearchController searchController = new SearchController();
    // Reuse ViewItemController for markAsWorn + createOutfitFromMatch
    private final ViewItemController viewItemController = new ViewItemController();

    /**
     * Tag IDs selected via the tag filter rows.
     */
    private final List<Integer> selectedTagIds = new ArrayList<>();

    /**
     * The single ClothingCategory selected via the Type filter row, or
     * {@code null} when no type is selected.
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
    private final Font smallFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"), 11);

    /**
     * Initialize search page.
     *
     * @param stage stage for items to be displayed
     */
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
        backButton.setOnAction(e -> new MainPage().start(stage));

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
        showAllButton.setOnAction(e
                -> showResults(searchController.searchByCategoryAndTags(null, new ArrayList<>())));

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

        // Row 1: Type filter (ClothingCategory-based, not tag IDs)
        filters.getChildren().add(createTypeFilterRow());

        // Rows 2-N: Tag-based filters (Color, Weather, Event, Detail, Other)
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
        labels.put(TagCategory.COLOR, "Colour");
        labels.put(TagCategory.WEATHER, "Weather");
        labels.put(TagCategory.EVENT, "Event");
        labels.put(TagCategory.DETAIL, "Details");
        labels.put(TagCategory.OTHER, "Other");

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

    private HBox createTypeFilterRow() {
        Label label = new Label("Type");
        label.setFont(subTitleFont);
        label.setPrefWidth(80);
        label.setStyle(TEXT_COLOR);

        FlowPane tagPane = new FlowPane();
        tagPane.setHgap(10);
        tagPane.setVgap(8);

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
                    removeCategoryChip();
                    addCategoryChip(displayName);
                } else {
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

            final int tagId = tag.getId();
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
        searchField.getChildren().add(0, chip);
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
            resultsBox.getChildren().add(buildResultCard(item));
        }
    }

    /**
     * Builds a full result card for one clothing item.
     *
     * <p>
     * The card contains:</p>
     * <ul>
     * <li>Item name, category chip, heart favourite button</li>
     * <li>Tag chips</li>
     * <li>"Mark as Worn" button + worn count label</li>
     * <li>"Show Matches" toggle button for TOP/BOTTOM items (lazy-loads
     * matches)</li>
     * </ul>
     */
    private VBox buildResultCard(ClothingItem item) {
        // Outer wrapper: card + optional matches panel appended below it
        VBox wrapper = new VBox(6);

        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-border-radius: 10;");

        // Item name
        Label name = new Label(item.getName());
        name.setFont(normalBoldFont);
        name.setStyle(TEXT_COLOR);

        // Category chip
        Label cat = new Label(cap(item.getCategory().name()));
        cat.setFont(smallFont);
        cat.setStyle(
                "-fx-text-fill: white;"
                + "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-background-radius: 6;"
                + "-fx-padding: 3 8 3 8;");

        Image emptyHeartImg = new Image(
                getClass().getResourceAsStream("/icons/heart.png"));
        Image filledHeartImg = new Image(
                getClass().getResourceAsStream("/icons/heartFilled.png"));

        ImageView heart = new ImageView(
                item.isFavorite() ? filledHeartImg : emptyHeartImg);
        heart.setFitWidth(22);
        heart.setFitHeight(22);

        Button heartBtn = new Button();
        heartBtn.setGraphic(heart);
        heartBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        heartBtn.setOnAction(e -> {
            viewItemController.toggleFavourite(item);
            heart.setImage(item.isFavorite() ? filledHeartImg : emptyHeartImg);
        });

        // Spacer pushes heart to the right of the top row
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.getChildren().addAll(name, cat, spacer, heartBtn);

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

        Label wornLabel = new Label("Worn: " + item.getWornCount() + " time(s)");
        wornLabel.setFont(smallFont);
        wornLabel.setStyle("-fx-text-fill: #888;");

        //Mark as Worn button
        Button wornBtn = new Button("Mark as Worn");
        wornBtn.setFont(normalBoldFont);
        wornBtn.setStyle(
                "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 14 6 14;");
        wornBtn.setOnAction(e -> {
            viewItemController.markAsWorn(item.getId());
            item.wear();
            wornLabel.setText("Worn: " + item.getWornCount() + " time(s)");
            showDialog(Alert.AlertType.INFORMATION,
                    "Marked as Worn",
                    "\"" + item.getName() + "\" worn count updated to "
                    + item.getWornCount() + ".");
        });

        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        actionRow.getChildren().addAll(wornBtn, wornLabel);

        // ── TASK 2: "Show Matches" button for TOP/BOTTOM only (lazy) ──
        ClothingCategory category = item.getCategory();
        if (category == ClothingCategory.TOP || category == ClothingCategory.BOTTOM) {
            Button matchBtn = new Button("Show Matches");
            matchBtn.setFont(normalBoldFont);
            matchBtn.setStyle(
                    "-fx-background-color: #F3EEE6;"
                    + "-fx-background-radius: 8;"
                    + "-fx-padding: 6 14 6 14;"
                    + TEXT_COLOR);
            matchBtn.setOnAction(e -> {
                // wrapper: index 0 = card; index 1 (if present) = matches panel
                if (wrapper.getChildren().size() > 1) {
                    wrapper.getChildren().remove(1);
                    matchBtn.setText("Show Matches");
                } else {
                    wrapper.getChildren().add(buildMatchesPanel(item));
                    matchBtn.setText("Hide Matches");
                }
            });
            actionRow.getChildren().add(matchBtn);
        }

        card.getChildren().addAll(topRow, chips, actionRow);
        wrapper.getChildren().add(card);
        return wrapper;
    }

    /**
     * Builds the inline matches panel shown beneath a TOP or BOTTOM result.
     * Each match row shows the match name/category and a "Create Outfit"
     * button.
     *
     * <p>
     * Outfit creation logic is handled in handleCreateOutfit():</p>
     */
    private VBox buildMatchesPanel(ClothingItem selectedItem) {
        List<ClothingItem> matches = viewItemController.getMatchingItems(selectedItem);

        VBox panel = new VBox(8);
        panel.setPadding(new Insets(10, 0, 2, 0));

        String headingText = matches.isEmpty()
                ? "No matching items found."
                : "Matching " + (selectedItem.getCategory() == ClothingCategory.TOP
                ? "bottoms" : "tops") + ":";

        Label heading = new Label(headingText);
        heading.setFont(smallFont);
        heading.setStyle("-fx-text-fill: #666;");
        panel.getChildren().add(heading);

        for (ClothingItem match : matches) {
            HBox matchRow = new HBox(12);
            matchRow.setAlignment(Pos.CENTER_LEFT);
            matchRow.setPadding(new Insets(8));
            matchRow.setStyle(
                    "-fx-background-color: #F8F4ED;"
                    + "-fx-background-radius: 8;"
                    + "-fx-border-color: " + LIGHT_GREEN + ";"
                    + "-fx-border-radius: 8;");

            // Match info
            VBox info = new VBox(2);
            Label matchName = new Label(match.getName());
            matchName.setFont(normalBoldFont);
            matchName.setStyle(TEXT_COLOR);

            Label matchCat = new Label(cap(match.getCategory().name()));
            matchCat.setFont(smallFont);
            matchCat.setStyle("-fx-text-fill: #888;");

            info.getChildren().addAll(matchName, matchCat);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button outfitBtn = new Button("Create Outfit");
            outfitBtn.setFont(normalBoldFont);
            outfitBtn.setStyle(
                    "-fx-background-color: " + DARK_GREEN + ";"
                    + "-fx-text-fill: white;"
                    + "-fx-background-radius: 8;"
                    + "-fx-padding: 6 14 6 14;");
            outfitBtn.setOnAction(e
                    -> handleCreateOutfit(selectedItem, match, outfitBtn));

            matchRow.getChildren().addAll(info, outfitBtn);
            panel.getChildren().add(matchRow);
        }

        return panel;
    }

    /**
     * Attempts to create an outfit from the two given items.
     *
     * <ul>
     * <li>On success: disables the button, shows a confirmation dialog.</li>
     * <li>On duplicate: shows an informational dialog, disables the
     * button.</li>
     * <li>On other error: shows an error dialog.</li>
     * </ul>
     */
    private void handleCreateOutfit(ClothingItem selectedItem,
            ClothingItem matchItem,
            Button outfitBtn) {
        try {
            viewItemController.createOutfitFromMatch(selectedItem, matchItem);
            outfitBtn.setDisable(true);
            outfitBtn.setText("Saved ✓");
            showDialog(Alert.AlertType.INFORMATION,
                    "Outfit Created",
                    "\"" + selectedItem.getName() + " + " + matchItem.getName()
                    + "\" has been saved to your outfits.");
        } catch (IllegalStateException ex) {
            // Duplicate outfit — OutfitService throws this
            outfitBtn.setDisable(true);
            outfitBtn.setText("Already exists");
            showDialog(Alert.AlertType.INFORMATION,
                    "Duplicate Outfit",
                    "An outfit with these two items already exists.");
        } catch (Exception ex) {
            showDialog(Alert.AlertType.ERROR,
                    "Error",
                    "Could not create outfit: " + ex.getMessage());
        }
    }

    private void showDialog(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String cap(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private void styleTagButton(ToggleButton btn) {
        String normal
                = "-fx-background-color: white;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;"
                + "-fx-text-fill: #1a1a1a;";
        String selected
                = "-fx-background-color:" + DARK_GREEN + ";"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;";

        btn.setStyle(normal);
        btn.selectedProperty().addListener((obs, oldVal, isSelected)
                -> btn.setStyle(isSelected ? selected : normal));
    }
}
