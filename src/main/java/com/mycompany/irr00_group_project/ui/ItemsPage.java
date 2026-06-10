package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.DashboardController;
import com.mycompany.irr00_group_project.controller.ViewItemController;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;
import com.mycompany.irr00_group_project.model.Tag;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
 * Supports toggling favourites, marking items as worn, viewing
 * matching items, creating outfits from matches, and removing items.
 *
 * @author Bhavini Kopparam Ajay
 * @author Arshiya Shaik
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

    // ──────────────────────────────────────────────────────────────
    // Items grid
    // ──────────────────────────────────────────────────────────────

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
            // Each item gets a wrapper VBox: the card + a collapsible matches panel
            VBox wrapper = new VBox(8);
            wrapper.setPrefWidth(480);
            VBox card = createItemCard(item, wrapper);
            wrapper.getChildren().add(card);
            itemsGrid.getChildren().add(wrapper);
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Item card builder
    // ──────────────────────────────────────────────────────────────

    /**
     * Builds one item card. The {@code wrapper} is passed in so the
     * "Show Matches" button can append/remove the matches panel into it.
     */
    private VBox createItemCard(ClothingItem item, VBox wrapper) {
        VBox card = new VBox(12);
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

        Image heartImg = new Image(getClass().getResourceAsStream("/icons/heart.png"));
        ImageView heart = new ImageView(heartImg);
        heart.setFitWidth(26);
        heart.setFitHeight(26);
        heart.setOpacity(item.isFavorite() ? 1.0 : 0.3);

        Button heartBtn = new Button();
        heartBtn.setGraphic(heart);
        heartBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        heartBtn.setOnAction(e -> {
            viewItemController.toggleFavourite(item);
            heart.setOpacity(item.isFavorite() ? 1.0 : 0.3);
        });

        topRow.getChildren().addAll(nameLabel, spacer, heartBtn);

        // CATEGORY chip
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

        // WORN COUNT label (updated by Mark as Worn button)
        Label wornLabel = new Label("Worn: " + item.getWornCount() + " time(s)");
        wornLabel.setFont(smallFont);
        wornLabel.setStyle("-fx-text-fill: #888;");

        // ACTION BUTTONS
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
        });

        // "Show Matches" toggles the matches panel in the wrapper
        Button matchBtn = new Button("Show Matches");
        matchBtn.setFont(normalBoldFont);
        matchBtn.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 14 6 14;");
        matchBtn.setOnAction(e -> toggleMatchesPanel(item, wrapper, matchBtn));

        Button deleteBtn = new Button("Remove");
        deleteBtn.setFont(normalBoldFont);
        deleteBtn.setStyle(
                "-fx-background-color: #c0392b;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 14 6 14;");
        deleteBtn.setOnAction(e -> {
            viewItemController.deleteItem(item.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Deleted");
            alert.setHeaderText(null);
            alert.setContentText("\"" + item.getName() + "\" has been removed from your closet.");
            alert.showAndWait();
            loadItemsFromDb();
        });

        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        actionRow.getChildren().addAll(wornBtn, matchBtn, deleteBtn, wornLabel);

        card.getChildren().addAll(topRow, catLabel, tagsPane, actionRow);
        return card;
    }

    // ──────────────────────────────────────────────────────────────
    // Matches panel (Issue 2 — inline, with "Add to Outfit" button)
    // ──────────────────────────────────────────────────────────────

    /**
     * Appends or removes the matches panel from the wrapper depending on
     * whether it is currently visible.
     */
    private void toggleMatchesPanel(ClothingItem item, VBox wrapper, Button matchBtn) {
        // The wrapper always has the card at index 0; index 1 would be the panel
        if (wrapper.getChildren().size() > 1) {
            // Panel is visible — collapse it
            wrapper.getChildren().remove(1);
            matchBtn.setText("Show Matches");
        } else {
            // Panel not yet shown — build and append it
            wrapper.getChildren().add(buildMatchesPanel(item));
            matchBtn.setText("Hide Matches");
        }
    }

    /**
     * Builds the matches panel for the given item.
     * Each suggested item gets a small card with an "Add to Outfit" button.
     */
    private VBox buildMatchesPanel(ClothingItem selectedItem) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(12, 18, 12, 18));
        panel.setStyle(
                "-fx-background-color: #F8F4ED;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-border-radius: 10;");

        List<ClothingItem> matches = viewItemController.getMatchingItems(selectedItem);

        Label heading = new Label(matches.isEmpty()
                ? "No matching items found."
                : "Matching items:");
        heading.setFont(normalBoldFont);
        heading.setStyle("-fx-text-fill: #555;");
        panel.getChildren().add(heading);

        for (ClothingItem match : matches) {
            HBox matchRow = new HBox(12);
            matchRow.setAlignment(Pos.CENTER_LEFT);
            matchRow.setPadding(new Insets(8));
            matchRow.setStyle(
                    "-fx-background-color: white;"
                    + "-fx-background-radius: 8;"
                    + "-fx-border-color: #EAE4D8;"
                    + "-fx-border-radius: 8;");

            VBox info = new VBox(3);
            Label matchName = new Label(match.getName());
            matchName.setFont(normalBoldFont);
            matchName.setStyle("-fx-text-fill: black;");

            Label matchCat = new Label(cap(match.getCategory().name()));
            matchCat.setFont(smallFont);
            matchCat.setStyle("-fx-text-fill: #888;");

            info.getChildren().addAll(matchName, matchCat);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button addToOutfitBtn = new Button("Add to Outfit");
            addToOutfitBtn.setFont(normalBoldFont);
            addToOutfitBtn.setStyle(
                    "-fx-background-color: " + DARK_GREEN + ";"
                    + "-fx-text-fill: white;"
                    + "-fx-background-radius: 8;"
                    + "-fx-padding: 6 14 6 14;");
            addToOutfitBtn.setOnAction(e ->
                    handleAddToOutfit(selectedItem, match, addToOutfitBtn));

            matchRow.getChildren().addAll(info, addToOutfitBtn);
            panel.getChildren().add(matchRow);
        }

        return panel;
    }

    /**
     * Creates the outfit, disables the button (so it can't be double-saved),
     * and shows a brief confirmation label.
     */
    private void handleAddToOutfit(ClothingItem selectedItem,
                                   ClothingItem matchItem,
                                   Button addToOutfitBtn) {
        try {
            Outfit created = viewItemController.createOutfitFromMatch(selectedItem, matchItem);
            addToOutfitBtn.setDisable(true);
            addToOutfitBtn.setText("Saved ✓");

            // Show a small confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Outfit Saved");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Outfit \"" + created.getName() + "\" saved!\n"
                    + "View it on the Outfits page.");
            alert.showAndWait();

        } catch (IllegalStateException ex) {
            // Duplicate outfit
            Alert dup = new Alert(Alert.AlertType.WARNING);
            dup.setTitle("Duplicate Outfit");
            dup.setHeaderText(null);
            dup.setContentText("This outfit already exists.");
            dup.showAndWait();
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Error");
            err.setHeaderText(null);
            err.setContentText("Could not save outfit: " + ex.getMessage());
            err.showAndWait();
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Top section (back button + title)
    // ──────────────────────────────────────────────────────────────

    private VBox createTopSection() {
        Image backImg = new Image(getClass().getResourceAsStream("/icons/arrow.png"));
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
        backButton.setOnAction(e -> new MainPage().start(stage));

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

    // ──────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────

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

    private String cap(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
