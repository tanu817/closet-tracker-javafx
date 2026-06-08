package com.mycompany.irr00_group_project.ui;

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

/**
 * Search page of the closet tracker app. Appears when search bar clicked on
 * main page. 
 *
 * @author Bhavini Kopparam Ajay
 */
public class SearchPage extends BorderPane {

    // COLOR PALETTE
    private static final String LIGHT_GREEN = "#D8CFBC";
    private static final String DARK_GREEN = "#565448";
    private static final String CREAM = "#FFFAF4";

    //CLASS FIELDS
    private Stage stage;
    private FlowPane searchField;

    private VBox resultsBox;

    //FONTS
    private Font normalBoldFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/MuktaVaani-SemiBold.ttf"
            ),
            14
    );

    private Font normalFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/MuktaVaani-Regular.ttf"
            ),
            13
    );

    private Font titleFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/YesevaOne-Regular.ttf"
            ),
            24
    );

    private Font subTitleFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/YesevaOne-Regular.ttf"
            ),
            15
    );

    private Font buttonFont = Font.loadFont(
            getClass().getResourceAsStream(
                    "/fonts/MuktaVaani-SemiBold.ttf"
            ),
            19
    );

    /**
     * Constructor of the class.
     *
     * @param stage
     */
    public SearchPage(Stage stage) {
        this.stage = stage;
        setStyle("-fx-background-color: #FFFAF4;");

        VBox root = new VBox(20);

        root.setPadding(new Insets(25));

        root.getChildren().addAll(
                createTopSection(),
                createFiltersSection()
        //createResultsSection()
        );

        setCenter(root);
    }

    /**
     * Designing the top section of the Page which is the back button, search
     * bar, and search button.
     *
     * @return Vbox with all components
     */
    private VBox createTopSection() {

        //BACK BUTTON
        Image backImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/arrow.png"
                )
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

        //SEARCH BAR
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

        //SEARCH BUTTON 
        Button searchButton = new Button("Search");
        searchButton.setFont(buttonFont);

        searchButton.setStyle(
                "-fx-background-color:" + DARK_GREEN + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-text-fill: white;"
        );

        searchButton.setPrefSize(120, 40);

        //PUTTING EVERYTHING IN A ROW
        HBox searchRow = new HBox(15);

        searchRow.getChildren().addAll(
                backButton,
                searchField,
                searchButton
        );

        //PUTTING IT ALL AT THE TOP
        VBox topSection = new VBox(15);
        topSection.getChildren().add(searchRow);

        return topSection;
    }

    /**
     * Creating the part of the page where you select tags. Uses 2 helper
     * methods.
     *
     * @return Vbox with components.
     */
    private VBox createFiltersSection() {

        VBox filters = new VBox(15);

        filters.getChildren().addAll(
                createFilterRow(
                        true,
                        "Type",
                        "Top",
                        "Bottom",
                        "One Piece",
                        "Accessory"
                ),
                createFilterRow(
                        false,
                        "Colour",
                        "Pink",
                        "Blue",
                        "Green",
                        "Black"
                ),
                createFilterRow(
                        false,
                        "Event",
                        "Party",
                        "Formal",
                        "Casual"
                ),
                createFilterRow(
                        false,
                        "Details",
                        "Minimal",
                        "Vintage"
                ),
                createFilterRow(
                        false,
                        "Other",
                        "Summer",
                        "Winter"
                )
        );

        return filters;

    }

    /**
     * Helper method to help create each category of tags.
     *
     * @param singleSelect allow to select only one tag if true.
     * @param title category name.
     * @param tags the example tags which needs to come from data base later.
     * Some edits might be required.
     * @return
     */
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

            ToggleButton tag
                    = new ToggleButton(tagText);
            if (singleSelect) {
                tag.setToggleGroup(group);
            }
            tag.setFont(normalBoldFont);

            styleTagButton(tag);

            tagPane.getChildren().add(tag);
        }

        HBox row = new HBox(20);

        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(
                label,
                tagPane
        );

        return row;
    }

    /**
     * Helps style each tag in the tag selection section of the page.
     *
     * @param tag
     */
    private void styleTagButton(
            ToggleButton tag
    ) {

        tag.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: " + LIGHT_GREEN + ";"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;"
        );

        tag.selectedProperty().addListener(
                (obs, oldVal, selected) -> {

                    if (selected) {

                        tag.setStyle(
                                "-fx-background-color: "
                                + DARK_GREEN + ";"
                                + "-fx-text-fill: white;"
                                + "-fx-background-radius: 8;"
                                + "-fx-border-radius: 8;"
                        );

                        addSelectedTag(tag.getText());

                    } else {

                        tag.setStyle(
                                "-fx-background-color: white;"
                                + "-fx-border-color: "
                                + LIGHT_GREEN + ";"
                                + "-fx-background-radius: 8;"
                                + "-fx-border-radius: 8;"
                        );

                        removeSelectedTag(tag.getText());
                    }
                }
        );
    }

    private void addSelectedTag(String text) {
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

        searchField.getChildren().removeIf(
                node -> text.equals(node.getUserData())
        );
    }

}
