package com.mycompany.irr00_group_project.ui;

import com.mycompany.irr00_group_project.controller.SearchController;
import com.mycompany.irr00_group_project.model.ClothingItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Search page of the closet tracker app. Appears when search bar clicked on
 * main page.
 *
 * @author Bhavini Kopparam Ajay
 * @author Arshiya Tasneem Shaik
 */
public class SearchPage extends BorderPane {

    // COLOR PALETTE
    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN = "#565448";
    private static final String CREAM = "#FFFAF4";

    // CLASS FIELDS
    private Stage stage;
    private FlowPane searchField;
    private VBox resultsBox;

    private SearchController searchController = new SearchController();
    private final List<String> selectedTags = new ArrayList<>();

    // FONTS
    private Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"),
            14
    );

    private Font normalFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-Regular.ttf"),
            13
    );

    private Font titleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"),
            24
    );

    private Font subTitleFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/YesevaOne-Regular.ttf"),
            15
    );

    private Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream("/fonts/MuktaVaani-SemiBold.ttf"),
            19
    );

    public SearchPage(Stage stage) {
        this.stage = stage;
        setStyle("-fx-background-color: #FFFAF4;");

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));

        resultsBox = new VBox(10);

        root.getChildren().addAll(
                createTopSection(),
                createFiltersSection(),
                resultsBox
        );

        setCenter(root);
    }

    private VBox createTopSection() {

        Image backImg = new Image(
                getClass().getResourceAsStream("/icons/arrow.png")
        );

        ImageView backIcon = new ImageView(backImg);
        backIcon.setFitWidth(35);
        backIcon.setFitHeight(35);

        Button backButton = new Button();
        backButton.setGraphic(backIcon);

        backButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
        );

        backButton.setPrefSize(40, 42);

        backButton.setOnAction(e -> {
            MainPage mainPage = new MainPage();
            mainPage.start(stage);
        });

        searchField = new FlowPane();
        searchField.setHgap(8);
        searchField.setVgap(8);
        searchField.setPrefWidth(950);
        searchField.setPrefHeight(45);
        searchField.setPadding(new Insets(5));

        searchField.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
        );

        Button searchButton = new Button("Search");
        searchButton.setFont(buttonFont);

        searchButton.setStyle(
                "-fx-background-color:" + DARK_GREEN + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-text-fill: white;"
        );

        searchButton.setPrefSize(120, 40);

        searchButton.setOnAction(e -> {
            List<ClothingItem> results =
                    searchController.searchByTags(selectedTags);

            showResults(results);
        });

        HBox searchRow = new HBox(15);
        searchRow.getChildren().addAll(backButton, searchField, searchButton);

        VBox topSection = new VBox(15);
        topSection.getChildren().add(searchRow);

        return topSection;
    }

    private VBox createFiltersSection() {

        VBox filters = new VBox(15);

        filters.getChildren().addAll(
                createFilterRow(true, "Type", "Top", "Bottom", "One Piece", "Accessory"),
                createFilterRow(false, "Colour", "Pink", "Blue", "Green", "Black"),
                createFilterRow(false, "Event", "Party", "Formal", "Casual"),
                createFilterRow(false, "Details", "Minimal", "Vintage"),
                createFilterRow(false, "Other", "Summer", "Winter")
        );

        return filters;
    }

    private HBox createFilterRow(
            boolean singleSelect,
            String title,
            String... tags
    ) {

        ToggleGroup group = null;

        if (singleSelect) {
            group = new ToggleGroup();
        }

        Label label = new Label(title);
        label.setFont(subTitleFont);
        label.setPrefWidth(80);

        FlowPane tagPane = new FlowPane();
        tagPane.setHgap(10);

        for (String tagText : tags) {

            ToggleButton tag = new ToggleButton(tagText);

            if (singleSelect) {
                tag.setToggleGroup(group);
            }

            tag.setFont(normalBoldFont);
            styleTagButton(tag);

            tagPane.getChildren().add(tag);
        }

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(label, tagPane);

        return row;
    }

    private void styleTagButton(ToggleButton tag) {

        tag.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;"
        );

        tag.selectedProperty().addListener((obs, oldVal, selected) -> {

            if (selected) {

                tag.setStyle(
                        "-fx-background-color:" + DARK_GREEN + ";"
                        + "-fx-text-fill: white;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;"
                );

                addSelectedTag(tag.getText());

            } else {

                tag.setStyle(
                        "-fx-background-color: white;"
                        + "-fx-border-color:" + LIGHT_GREEN + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;"
                );

                removeSelectedTag(tag.getText());
            }
        });
    }

    private void addSelectedTag(String text) {

        if (!selectedTags.contains(text)) {
            selectedTags.add(text);
        }

        Label tag = new Label(text);
        tag.setFont(normalBoldFont);

        tag.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 4 10 4 10;"
        );

        tag.setUserData(text);

        searchField.getChildren().add(tag);
    }

    private void removeSelectedTag(String text) {

        selectedTags.remove(text);

        searchField.getChildren().removeIf(
                node -> text.equals(node.getUserData())
        );
    }

    private void showResults(List<ClothingItem> items) {

        resultsBox.getChildren().clear();

        if (items == null || items.isEmpty()) {
            resultsBox.getChildren().add(
                    new Label("No items match your filters")
            );
            return;
        }

        for (ClothingItem item : items) {

            VBox card = new VBox(5);
            card.setStyle(
                    "-fx-background-color: white;"
                    + "-fx-padding: 10;"
                    + "-fx-background-radius: 10;"
            );

            Label name = new Label(item.getName());
            name.setFont(normalBoldFont);

            card.getChildren().add(name);

            resultsBox.getChildren().add(card);
        }
    }
}