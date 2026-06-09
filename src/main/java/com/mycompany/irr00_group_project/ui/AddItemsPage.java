package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.AddItemController;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Add items overlay — appears when the plus button is clicked on the main page.
 * Wired to {@link AddItemController} for persistence.
 *
 * @author Bhavini Kopparam Ajay
 */
public class AddItemsPage {

    private Button backButton;

    // Per-category tag panes — keyed by TagCategory
    private FlowPane colorPane;
    private FlowPane weatherPane;
    private FlowPane eventPane;
    private FlowPane detailsPane;
    private FlowPane otherPane;

    // Category selection
    private ToggleGroup typeGroup;
    private ToggleButton topBtn;
    private ToggleButton bottomBtn;
    private ToggleButton onePieceBtn;
    private ToggleButton accessoryBtn;

    private TextField itemName;

    private final AddItemController addItemController = new AddItemController();

    // ==================
    // Fonts (loaded once)
    // ==================

    private final Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);
    private final Font normalFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"), 13);
    private final Font titleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 24);
    private final Font subTitleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 15);
    private final Font saveButtonFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 19);

    /**
     * Builds the Add Items overlay VBox.
     *
     * @return VBox with all components
     */
    public VBox createAddItemsOverlay() {

        // MAIN LAYOUT
        VBox overlay = new VBox(20);
        overlay.setPadding(new Insets(30));
        overlay.setStyle(
                "-fx-background-color: #FFFAF4;"
                + "-fx-background-radius: 15;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 15;");

        // TOP ROW (back button + item name field)
        HBox topRow = new HBox(20);

        Image backImg = new Image(
                getClass().getResourceAsStream("/icons/arrow.png"));
        ImageView backIcon = new ImageView(backImg);
        backIcon.setFitWidth(35);
        backIcon.setFitHeight(35);

        backButton = new Button();
        backButton.setGraphic(backIcon);
        backButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        backButton.setPrefSize(40, 35);

        itemName = new TextField();
        itemName.setPromptText("Enter Name");
        itemName.setPrefWidth(900);
        itemName.setPrefHeight(42);
        itemName.setMinHeight(42);
        itemName.setMaxHeight(42);
        itemName.setFont(normalFont);

        topRow.getChildren().addAll(backButton, itemName);

        // NOTE LABEL
        Text note = new Text(
                "Note: You can create tags by typing in the + field or choose from existing ones. "
                + "It is mandatory to choose the type. "
                + "To delete a new tag right-click it; to edit double-click it.");
        note.setFont(normalFont);
        note.setWrappingWidth(900);

        HBox noteBox = new HBox(note);
        noteBox.setPadding(new Insets(10, 15, 10, 15));
        noteBox.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 8;");
        noteBox.setAlignment(Pos.CENTER_LEFT);

        // CHOOSE TAGS TITLE
        Label chooseTags = new Label("Choose Tags");
        chooseTags.setFont(titleFont);

        // TYPE ROW (clothing category — single select)
        typeGroup = new ToggleGroup();
        topBtn       = makeTypeButton("Top",       typeGroup);
        bottomBtn    = makeTypeButton("Bottom",    typeGroup);
        onePieceBtn  = makeTypeButton("One Piece", typeGroup);
        accessoryBtn = makeTypeButton("Accessory", typeGroup);

        HBox typeRow = new HBox(20);
        typeRow.setAlignment(Pos.CENTER_LEFT);
        Label typeLabel = new Label("Type *");
        typeLabel.setFont(subTitleFont);
        typeLabel.setPrefWidth(80);
        typeRow.getChildren().addAll(typeLabel, topBtn, bottomBtn, onePieceBtn, accessoryBtn);

        // TAG CATEGORY ROWS — pre-populate from DB, then allow custom additions
        colorPane   = createTagSection(TagCategory.COLOR);
        weatherPane = createTagSection(TagCategory.WEATHER);
        eventPane   = createTagSection(TagCategory.EVENT);
        detailsPane = createTagSection(TagCategory.DETAIL);
        otherPane   = createTagSection(TagCategory.OTHER);

        HBox colourRow  = makeTagRow("Colour",  colorPane);
        HBox weatherRow = makeTagRow("Weather", weatherPane);
        HBox eventRow   = makeTagRow("Event",   eventPane);
        HBox detailsRow = makeTagRow("Details", detailsPane);
        HBox otherRow   = makeTagRow("Other",   otherPane);

        // SAVE BUTTON
        Button saveButton = new Button("Save");
        saveButton.setFont(saveButtonFont);
        saveButton.setPrefSize(140, 50);
        saveButton.setStyle(
                "-fx-background-color: #565448;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 10;");
        saveButton.setOnAction(e -> handleSave());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox saveRow = new HBox();
        saveRow.setAlignment(Pos.CENTER_RIGHT);
        saveRow.getChildren().add(saveButton);

        overlay.getChildren().addAll(
                topRow,
                noteBox,
                chooseTags,
                typeRow,
                colourRow,
                weatherRow,
                eventRow,
                detailsRow,
                otherRow,
                spacer,
                saveRow
        );

        return overlay;
    }

    /**
     * Returns the back button so {@code MainPage} can attach its listener.
     *
     * @return the back button
     */
    public Button getBackButton() {
        return backButton;
    }

    // ==================
    // Save logic
    // ==================

    /**
     * Collects form values, validates, and delegates to {@link AddItemController}.
     */
    private void handleSave() {
        String name = itemName.getText().trim();
        if (name.isEmpty()) {
            showAlert("Validation Error", "Please enter an item name.");
            return;
        }

        ClothingCategory category = resolveCategory();
        if (category == null) {
            showAlert("Validation Error", "Please select a clothing type.");
            return;
        }

        // Collect selected (existing) tag IDs from toggle buttons in each pane
        List<Integer> existingTagIds = collectSelectedTagIds(colorPane);
        existingTagIds.addAll(collectSelectedTagIds(weatherPane));
        existingTagIds.addAll(collectSelectedTagIds(eventPane));
        existingTagIds.addAll(collectSelectedTagIds(detailsPane));
        existingTagIds.addAll(collectSelectedTagIds(otherPane));

        // Collect custom (new) tag names from un-persisted toggle buttons
        List<String> newTagNames = collectCustomTagNames(colorPane,   TagCategory.COLOR);
        newTagNames.addAll(collectCustomTagNames(weatherPane, TagCategory.WEATHER));
        newTagNames.addAll(collectCustomTagNames(eventPane,   TagCategory.EVENT));
        newTagNames.addAll(collectCustomTagNames(detailsPane, TagCategory.DETAIL));
        newTagNames.addAll(collectCustomTagNames(otherPane,   TagCategory.OTHER));

        try {
            // addItem handles existing IDs
            var item = addItemController.addItem(name, category, existingTagIds);
            // createTag + assign for any new tag names (tagged as OTHER by default
            // since we cannot know the user's intent from the name alone)
            for (String tagName : newTagNames) {
                int tagId = addItemController.createTag(tagName, TagCategory.OTHER);
                addItemController.assignTagToItem(item.getId(), tagId);
            }
            showAlert("Saved", "\"" + name + "\" has been added to your closet.");
            resetForm();
        } catch (IllegalArgumentException ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    /** Maps the selected type button to a {@link ClothingCategory}. */
    private ClothingCategory resolveCategory() {
        if (topBtn.isSelected())       { return ClothingCategory.TOP; }
        if (bottomBtn.isSelected())    { return ClothingCategory.BOTTOM; }
        if (onePieceBtn.isSelected())  { return ClothingCategory.ONE_PIECE; }
        if (accessoryBtn.isSelected()) { return ClothingCategory.ACCESSORY; }
        return null;
    }

    /**
     * Returns IDs of all selected toggle buttons in a tag pane.
     * Buttons backed by a DB tag carry their tag ID as user data (Integer).
     */
    private List<Integer> collectSelectedTagIds(FlowPane pane) {
        List<Integer> ids = new ArrayList<>();
        for (var node : pane.getChildren()) {
            if (node instanceof ToggleButton btn
                    && btn.isSelected()
                    && btn.getUserData() instanceof Integer id) {
                ids.add(id);
            }
        }
        return ids;
    }

    /**
     * Returns names of selected toggle buttons that were typed in by the user
     * (user data is not an Integer — they have no DB ID yet).
     */
    private List<String> collectCustomTagNames(FlowPane pane, TagCategory ignored) {
        List<String> names = new ArrayList<>();
        for (var node : pane.getChildren()) {
            if (node instanceof ToggleButton btn
                    && btn.isSelected()
                    && !(btn.getUserData() instanceof Integer)) {
                names.add(btn.getText());
            }
        }
        return names;
    }

    /** Clears the form after a successful save. */
    private void resetForm() {
        itemName.clear();
        typeGroup.selectToggle(null);
        clearTagPane(colorPane);
        clearTagPane(weatherPane);
        clearTagPane(eventPane);
        clearTagPane(detailsPane);
        clearTagPane(otherPane);
    }

    /** Deselects all toggle buttons in a pane (does not remove DB-backed ones). */
    private void clearTagPane(FlowPane pane) {
        for (var node : pane.getChildren()) {
            if (node instanceof ToggleButton btn) {
                btn.setSelected(false);
            }
        }
    }

    // ==================
    // UI builders
    // ==================

    /**
     * Creates a type-selection toggle button and registers it with
     * {@code typeGroup}.
     */
    private ToggleButton makeTypeButton(String text, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setFont(normalBoldFont);
        btn.setPrefHeight(35);
        styleTagButton(btn);
        return btn;
    }

    /**
     * Creates a labelled row containing a tag pane.
     */
    private HBox makeTagRow(String labelText, FlowPane pane) {
        Label label = new Label(labelText);
        label.setFont(subTitleFont);
        label.setPrefWidth(80);

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(label, pane);
        return row;
    }

    /**
     * Creates a {@link FlowPane} pre-populated with toggle buttons for every
     * existing DB tag in {@code category}, plus a + button to add custom tags.
     */
    private FlowPane createTagSection(TagCategory category) {
        FlowPane pane = new FlowPane();
        pane.setHgap(10);
        pane.setVgap(10);

        // Load existing tags for this category from DB
        List<Tag> allTags = addItemController.getAllTags();
        for (Tag tag : allTags) {
            if (tag.getCategory() == category) {
                ToggleButton btn = new ToggleButton(tag.getName());
                btn.setFont(normalBoldFont);
                btn.setPrefHeight(35);
                btn.setUserData(tag.getId()); // mark as DB-backed
                styleTagButton(btn);
                pane.getChildren().add(btn);
            }
        }

        // + button and text field for custom tag creation
        Image addImg = new Image(
                getClass().getResourceAsStream("/icons/plus.png"));
        ImageView addIcon = new ImageView(addImg);
        addIcon.setFitWidth(20);
        addIcon.setFitHeight(20);

        Button addButton = new Button();
        addButton.setGraphic(addIcon);
        addButton.setPrefSize(50, 35);
        addButton.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #565448;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");

        TextField newTagField = new TextField();
        newTagField.setVisible(false);
        newTagField.setManaged(false);
        newTagField.setPrefWidth(120);
        newTagField.setFont(normalBoldFont);
        newTagField.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;");

        addButton.setOnAction(e -> {
            addButton.setVisible(false);
            addButton.setManaged(false);
            newTagField.setVisible(true);
            newTagField.setManaged(true);
            newTagField.requestFocus();
        });

        newTagField.setOnAction(e -> commitCustomTag(newTagField, pane, addButton));

        newTagField.focusedProperty().addListener((obs, oldVal, focused) -> {
            if (!focused) {
                commitCustomTag(newTagField, pane, addButton);
            }
        });

        pane.getChildren().addAll(newTagField, addButton);
        return pane;
    }

    /**
     * Commits a custom tag name typed by the user into the pane as a new
     * (unselected) toggle button with no user data (signals "new tag").
     */
    private void commitCustomTag(TextField field, FlowPane pane, Button addButton) {
        String text = field.getText().trim();
        if (!text.isEmpty()) {
            ToggleButton tag = new ToggleButton(text);
            tag.setFont(normalBoldFont);
            tag.setPrefHeight(35);
            // No userData → signals a new tag (not yet in DB)
            styleTagButton(tag);

            // Double-click to edit
            tag.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    TextField editField = new TextField(tag.getText());
                    editField.setFont(normalBoldFont);
                    int idx = pane.getChildren().indexOf(tag);
                    pane.getChildren().set(idx, editField);
                    editField.requestFocus();
                    Runnable commit = () -> {
                        tag.setText(editField.getText());
                        pane.getChildren().set(pane.getChildren().indexOf(editField), tag);
                    };
                    editField.setOnAction(ev -> { commit.run(); ev.consume(); });
                    editField.focusedProperty().addListener(
                            (o, ov, nv) -> { if (!nv) { commit.run(); } });
                }
            });

            // Right-click to delete
            tag.setOnContextMenuRequested(event -> pane.getChildren().remove(tag));

            // Insert before the TextField + addButton (last two nodes)
            int insertAt = pane.getChildren().size() - 2;
            pane.getChildren().add(Math.max(insertAt, 0), tag);
        }

        field.clear();
        field.setVisible(false);
        field.setManaged(false);
        addButton.setVisible(true);
        addButton.setManaged(true);
    }

    // ==================
    // Style helpers
    // ==================

    private void styleTagButton(ToggleButton button) {
        String normal =
                "-fx-background-color: white;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;";
        String selected =
                "-fx-background-color: #565448;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;";

        button.setStyle(normal);
        button.selectedProperty().addListener((obs, oldVal, isSelected) ->
                button.setStyle(isSelected ? selected : normal));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}