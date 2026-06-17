package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.OutfitController;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
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
 * Outfits page: shows all outfits loaded from CSV storage.
 *
 * @author Bhavini Kopparam Ajay
 * @author Arshiya Shaik
 */
public class OutfitsPage extends BorderPane {

    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN  = "#565448";
    private static final String CREAM       = "#FFFAF4";

    private final Stage stage;

    private final Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 14);
    private final Font normalFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"), 13);
    private final Font titleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 24);
    private final Font outfitFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"), 20);
    private final Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"), 19);

    private final OutfitController outfitController = new OutfitController();
    private VBox outfitsRoot;

    /**
     * Constructs the OutfitsPage and initialises the layout.
     *
     * @param stage the primary application stage
     */
    public OutfitsPage(Stage stage) {
        this.stage = stage;
        setStyle("-fx-background-color: " + CREAM + ";");

        VBox root = new VBox(25);
        root.setPadding(new Insets(25));
        root.getChildren().addAll(createTopSection(), createOutfitsSection());
        setCenter(root);
    }

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
                + "-fx-background-radius: 10;");
        backButton.setOnAction(e -> {
            MainPage mainPage = new MainPage();
            mainPage.start(stage);
        });

        Label title = new Label("My Outfits");
        title.setFont(titleFont);

        Region leftSpacer  = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer,  Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER);
        topRow.getChildren().addAll(backButton, leftSpacer, title, rightSpacer);

        VBox topSection = new VBox();
        topSection.getChildren().add(topRow);
        return topSection;
    }

    private ScrollPane createOutfitsSection() {
        outfitsRoot = new VBox(25);
        refreshOutfits();

        ScrollPane scrollPane = new ScrollPane(outfitsRoot);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: transparent;"
                + "-fx-background-color: transparent;");
        return scrollPane;
    }

    private void refreshOutfits() {
        VBox leftColumn  = new VBox(25);
        VBox rightColumn = new VBox(25);

        List<Outfit> outfits = outfitController.getAllOutfits();

        if (outfits.isEmpty()) {
            Label empty = new Label("No outfits yet. Create your first outfit!");
            empty.setFont(normalFont);
            leftColumn.getChildren().add(empty);
        } else {
            for (int i = 0; i < outfits.size(); i++) {
                Outfit outfit = outfits.get(i);
                List<ClothingItem> items = outfitController.getItemsForOutfit(outfit.getId());

                ClothingItem item1 = items.size() > 0 ? items.get(0) : null;
                ClothingItem item2 = items.size() > 1 ? items.get(1) : null;

                VBox card = createOutfitCard(outfit.getId(), outfit.getName(), item1, item2);

                if (i % 2 == 0) {
                    leftColumn.getChildren().add(card);
                } else {
                    rightColumn.getChildren().add(card);
                }
            }
        }

        Region divider = new Region();
        divider.setPrefWidth(2);
        divider.setStyle("-fx-background-color: #EAD7BC;");

        HBox outfitsRow = new HBox(40);
        outfitsRow.getChildren().addAll(leftColumn, divider, rightColumn);

        outfitsRoot.getChildren().setAll(outfitsRow);
    }

    private VBox createOutfitCard(int outfitId,
                                  String outfitName,
                                  ClothingItem item1,
                                  ClothingItem item2) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setPrefSize(580, 260);
        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAD7BC;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;");

        Label title = new Label(outfitName);
        title.setFont(outfitFont);
        title.setStyle("-fx-text-fill: black;");

        HBox itemsRow = buildOutfitItemsRow(item1, item2);
        HBox actionsRow = buildOutfitActionsRow(outfitId, outfitName, title);

        card.getChildren().addAll(title, itemsRow, actionsRow);
        return card;
    }

    /**
     * Builds the items row showing the two outfit items with a plus icon between them.
     *
     * @param item1 first clothing item (may be null)
     * @param item2 second clothing item (may be null)
     * @return HBox with mini-cards and plus icon
     */
    private HBox buildOutfitItemsRow(ClothingItem item1, ClothingItem item2) {
        Image plusImg = new Image(
                getClass().getResourceAsStream("/icons/plus.png"));
        ImageView plusIcon = new ImageView(plusImg);
        plusIcon.setFitWidth(40);
        plusIcon.setFitHeight(40);

        VBox miniCard1 = createMiniItemCard(item1);
        VBox miniCard2 = createMiniItemCard(item2);

        HBox row = new HBox(30);
        row.setAlignment(Pos.CENTER);
        row.getChildren().addAll(miniCard1, plusIcon, miniCard2);
        return row;
    }

    /**
     * Builds the actions row (Edit, Mark as Worn, Delete) for an outfit card.
     *
     * @param outfitId id of the outfit
     * @param outfitName name of the outfit
     * @param titleLabel label reference so rename can update it
     * @return HBox with action buttons
     */
    private HBox buildOutfitActionsRow(int outfitId, String outfitName, Label titleLabel) {
        Button editBtn = new Button("Edit");
        editBtn.setFont(normalBoldFont);
        editBtn.setStyle("-fx-background-color: #5C8A6E; -fx-background-radius: 8; "
                + "-fx-border-radius: 8; -fx-text-fill: white;");
        editBtn.setOnAction(e -> handleRenameOutfit(outfitId, titleLabel));

        Button wornBtn = new Button("Mark as Worn");
        wornBtn.setFont(normalBoldFont);
        wornBtn.setStyle("-fx-background-color: " + DARK_GREEN + "; -fx-background-radius: 8; "
                + "-fx-border-radius: 8; -fx-text-fill: white;");
        wornBtn.setOnAction(e -> {
            outfitController.wearOutfit(outfitId);
            showInfo("Outfit Worn", "Outfit marked as worn. Wear counts updated.");
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setFont(normalBoldFont);
        deleteBtn.setStyle("-fx-background-color: #c0392b; -fx-background-radius: 8; "
                + "-fx-border-radius: 8; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            outfitController.deleteOutfit(outfitId);
            showInfo("Outfit Deleted", "Outfit \"" + outfitName + "\" has been deleted.");
            refreshOutfits();
        });

        HBox actionsRow = new HBox(10);
        actionsRow.getChildren().addAll(wornBtn, editBtn, deleteBtn);
        return actionsRow;
    }

    /**
     * Opens a rename dialog for the given outfit and updates the label on success.
     *
     * @param outfitId id of the outfit to rename
     * @param titleLabel label in the card to update after rename
     */
    private void handleRenameOutfit(int outfitId, Label titleLabel) {
        TextInputDialog dialog = new TextInputDialog(titleLabel.getText());
        dialog.setTitle("Rename Outfit");
        dialog.setHeaderText(null);
        dialog.setContentText("New name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (!newName.isBlank()) {
                outfitController.renameOutfit(outfitId, newName);
                titleLabel.setText(newName);
                showInfo("Outfit Updated", "Outfit renamed to \"" + newName + "\".");
            }
        });
    }

    private VBox createMiniItemCard(ClothingItem item) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefSize(220, 150);
        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #EAD7BC;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;");

        if (item == null) {
            Label placeholder = new Label("—");
            placeholder.setFont(buttonFont);
            placeholder.setStyle("-fx-text-fill: black;");
            card.getChildren().add(placeholder);
            return card;
        }

        Label name = new Label(item.getName());
        name.setFont(buttonFont);
        name.setStyle("-fx-text-fill: black;");

        Label catLabel = new Label(item.getCategory().name());
        catLabel.setFont(normalFont);
        catLabel.setStyle(
                "-fx-background-color: " + DARK_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 6 12 6 12;"
                + "-fx-text-fill: white;");

        card.getChildren().addAll(name, catLabel);

        if (!item.getTags().isEmpty()) {
            String tagName = item.getTags().iterator().next().getName();
            Label tagChip = new Label(tagName);
            tagChip.setFont(normalFont);
            tagChip.setStyle(
                    "-fx-background-color: #F3EEE6;"
                    + "-fx-background-radius: 8;"
                    + "-fx-padding: 4 8 4 8;"
                    + "-fx-text-fill: black;");
            card.getChildren().add(tagChip);
        }

        return card;
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
