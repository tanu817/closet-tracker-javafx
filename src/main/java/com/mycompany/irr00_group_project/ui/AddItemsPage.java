/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.irr00_group_project.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

/**
 *
 * @author Bhavini Kopparam Ajay
 */
public class AddItemsPage {

    private Button backButton;
    
    /**
     * main method through which the overlay is created. 
     * @return Vbox with all components. 
     */
    public VBox createAddItemsOverlay() {

        //Fonts
        Font normalBoldFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/MuktaVaani-SemiBold.ttf"
                ),
                14
        );

        Font normalFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/MuktaVaani-Regular.ttf"
                ),
                13
        );

        Font titleFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/YesevaOne-Regular.ttf"
                ),
                24
        );

        Font subTitleFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/YesevaOne-Regular.ttf"
                ),
                15
        );
        
        Font saveButtonFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/MuktaVaani-SemiBold.ttf"
                ),
                19
        );

        //MAIN LAYOUT
        VBox overlay = new VBox(20);
        overlay.setPadding(new Insets(30));

        overlay.setStyle(
                "-fx-background-color: #FFFAF4;"
                + "-fx-background-radius: 15;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-border-radius: 15;"
        );

        //TOP ROW(BACKBUTTON + TEXT FIEILD)
        HBox topRow = new HBox(20);

        Image backImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/arrow.png"
                )
        );

        ImageView backIcon = new ImageView(backImg);

        backIcon.setFitWidth(35);
        backIcon.setFitHeight(35);

        backButton = new Button();

        backButton.setGraphic(backIcon);

        backButton.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
        );

        backButton.setPrefSize(40, 35);

        TextField itemName = new TextField();
        itemName.setPromptText("Enter Name");
        itemName.setPrefWidth(900);
        itemName.setPrefHeight(42);
        itemName.setMinHeight(42);
        itemName.setMaxHeight(42);

        itemName.setFont(normalFont);

        topRow.getChildren().addAll(
                backButton,
                itemName
        );

        //NOTE LABEL
        Text note = new Text(
                "Note: You can create tags by typing in or choose from existing one. "
                + "It is mandatory to choose the type. To delete tags right click, "
                + "to edit tags double click."
        );

        note.setFont(normalFont);
        note.setWrappingWidth(900);

        HBox noteBox = new HBox(note);

        noteBox.setPadding(new Insets(10, 15, 10, 15));

        noteBox.setStyle(
                "-fx-background-color: #F3EEE6;"
                + "-fx-background-radius: 8;"
        );

        noteBox.setAlignment(Pos.CENTER_LEFT);

        //CHOOSE TAGS TITLE
        Label chooseTags = new Label("Choose Tags");

        chooseTags.setFont(titleFont);

        //TYPE ROW
        HBox typeRow = new HBox(20);

        Label typeLabel = new Label("Type *");
        typeRow.setAlignment(Pos.CENTER_LEFT);
        typeLabel.setFont(subTitleFont);

        typeLabel.setPrefWidth(80);

        String tagStyle
                = "-fx-background-color: white;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;";

        ToggleButton topBtn = new ToggleButton("Top");
        ToggleButton bottomBtn = new ToggleButton("Bottom");
        ToggleButton onePieceBtn = new ToggleButton("One Piece");
        ToggleButton accessoryBtn = new ToggleButton("Accessory");

        topBtn.setPrefHeight(20);
        bottomBtn.setPrefHeight(20);
        onePieceBtn.setPrefHeight(20);
        accessoryBtn.setPrefHeight(20);

        topBtn.setFont(normalBoldFont);
        bottomBtn.setFont(normalBoldFont);
        onePieceBtn.setFont(normalBoldFont);
        accessoryBtn.setFont(normalBoldFont);

        ToggleGroup typeGroup = new ToggleGroup();

        topBtn.setToggleGroup(typeGroup);
        bottomBtn.setToggleGroup(typeGroup);
        onePieceBtn.setToggleGroup(typeGroup);
        accessoryBtn.setToggleGroup(typeGroup);

        topBtn.setStyle(tagStyle);
        bottomBtn.setStyle(tagStyle);
        onePieceBtn.setStyle(tagStyle);
        accessoryBtn.setStyle(tagStyle);

        styleTagButton(topBtn);
        styleTagButton(bottomBtn);
        styleTagButton(onePieceBtn);
        styleTagButton(accessoryBtn);

        typeRow.getChildren().addAll(
                typeLabel,
                topBtn,
                bottomBtn,
                onePieceBtn,
                accessoryBtn
        );

        //COLOR ROW
        HBox colourRow = new HBox(20);

        Label colourLabel = new Label("Colour");
        colourLabel.setFont(subTitleFont);

        FlowPane colourTags = createTagSection();

        colourRow.setAlignment(Pos.CENTER_LEFT);

        colourLabel.setPrefWidth(80);

        colourRow.getChildren().addAll(
                colourLabel,
                colourTags
        );

        Label weatherLabel = new Label("Weather");

        weatherLabel.setFont(subTitleFont);
        weatherLabel.setPrefWidth(80);

        FlowPane weatherTags
                = createTagSection();

        HBox weatherRow = new HBox(20);

        weatherRow.setAlignment(Pos.CENTER_LEFT);

        weatherRow.getChildren().addAll(
                weatherLabel,
                weatherTags
        );

        Label eventLabel = new Label("Event");

        eventLabel.setFont(subTitleFont);
        eventLabel.setPrefWidth(80);

        FlowPane eventTags
                = createTagSection();

        HBox eventRow = new HBox(20);

        eventRow.setAlignment(Pos.CENTER_LEFT);

        eventRow.getChildren().addAll(
                eventLabel,
                eventTags
        );

        Label detailsLabel = new Label("Details");

        detailsLabel.setFont(subTitleFont);
        detailsLabel.setPrefWidth(80);

        FlowPane detailsTags
                = createTagSection();

        HBox detailsRow = new HBox(20);

        detailsRow.setAlignment(Pos.CENTER_LEFT);

        detailsRow.getChildren().addAll(
                detailsLabel,
                detailsTags
        );

        Label otherLabel = new Label("Other");

        otherLabel.setFont(subTitleFont);
        otherLabel.setPrefWidth(80);

        FlowPane otherTags
                = createTagSection();

        HBox otherRow = new HBox(20);

        otherRow.setAlignment(Pos.CENTER_LEFT);

        otherRow.getChildren().addAll(
                otherLabel,
                otherTags
        );

        //SAVE BUTTON
        Button saveButton = new Button("Save");
        saveButton.setFont(saveButtonFont);

        saveButton.setPrefSize(140, 80);

        saveButton.setStyle(
                "-fx-background-color: #565448;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 10;"
        );

        Region spacer = new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

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
     * returns the back button which helps with the listener of this button in 
     * main page. 
     * @return 
     */
    public Button getBackButton() {
        return backButton;
    }
    
    /**
     * Styles each tag. 
     * @param button 
     */
    private void styleTagButton(ToggleButton button) {

        String normal
                = "-fx-background-color: white;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;";

        String selected
                = "-fx-background-color: #565448;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 8;";

        button.setStyle(normal);

        button.selectedProperty().addListener((obs, oldVal, isSelected) -> {

            if (isSelected) {
                button.setStyle(selected);
            } else {
                button.setStyle(normal);
            }

        });
    }
    
    /**
     * Helper method to display different categories on add items overlay.
     * @return Flow pane of each category. 
     */
    private FlowPane createTagSection() {
        
        //FONT 
        Font normalBoldFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/MuktaVaani-SemiBold.ttf"
                ),
                14
        );

        FlowPane tagsPane = new FlowPane();

        tagsPane.setHgap(10);
        tagsPane.setVgap(10);

        // PLUS ICON
        Image addImg = new Image(
                getClass().getResourceAsStream(
                        "/icons/plus.png"
                )
        );

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
                + "-fx-border-radius: 10;"
        );

        // TEXT FIELD
        TextField newTagField = new TextField();

        newTagField.setVisible(false);

        newTagField.setManaged(false);

        newTagField.setPrefWidth(120);

        newTagField.setFont(normalBoldFont);

        newTagField.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #D8CFBC;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;"
        );

        // SHOW TEXT FIELD
        addButton.setOnAction(e -> {

            addButton.setVisible(false);
            addButton.setManaged(false);

            newTagField.setVisible(true);
            newTagField.setManaged(true);

            newTagField.requestFocus();
        });

        // CREATE NEW TAG
        newTagField.setOnAction(e -> {

            String text = newTagField.getText().trim();

            if (!text.isEmpty()) {

                ToggleButton tag
                        = new ToggleButton(text);

                tag.setFont(normalBoldFont);

                tag.setPrefHeight(40);

                // SAME STYLE AS TYPE BUTTONS
                styleTagButton(tag);

                //LISTERNER FOR THE TAGS
                tag.setOnMouseClicked(event -> {

                    if (event.getClickCount() == 2) {

                        TextField editField = new TextField(
                                tag.getText()
                        );

                        editField.setFont(normalBoldFont);

                        int index
                                = tagsPane.getChildren().indexOf(tag);

                        tagsPane.getChildren().set(
                                index,
                                editField
                        );

                        editField.requestFocus();

                        editField.setOnAction(enter -> {

                            tag.setText(
                                    editField.getText()
                            );

                            tagsPane.getChildren().set(
                                    index,
                                    tag
                            );

                            enter.consume();
                        });

                        editField.focusedProperty().addListener(
                                (obs, oldVal, newVal) -> {

                                    if (!newVal) {

                                        tag.setText(
                                                editField.getText()
                                        );

                                        tagsPane.getChildren().set(
                                                index,
                                                tag
                                        );
                                    }
                                }
                        );

                    }
                });
       
                // RIGHT CLICK TO DELETE
                tag.setOnContextMenuRequested(event -> {

                    tagsPane.getChildren().remove(tag);

                });

                tagsPane.getChildren().add(
                        tagsPane.getChildren().size() - 1,
                        tag
                );

            }

            newTagField.clear();

            newTagField.setVisible(false);
            newTagField.setManaged(false);

            addButton.setVisible(true);
            addButton.setManaged(true);
        });
        
        //LAYOUT
        tagsPane.getChildren().addAll(
                newTagField,
                addButton
        );

        return tagsPane;
    }
}
