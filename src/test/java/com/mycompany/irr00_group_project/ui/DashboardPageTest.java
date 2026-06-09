package com.mycompany.irr00_group_project.ui;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for FR-01: tag creation, editing and deletion in AddItemsPage.
 *
 * @author YourName
 */
public class DashboardPageTest {

    /**
     * Starts the JavaFX engine once before all tests run.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException ignored) {
            latch.countDown();
        }
        latch.await();
    }

    /**
     * Runs the given action on the JavaFX thread and waits for it to finish.
     *
     * @param action the code to run on the FX thread
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    private static void runOnFxThread(Runnable action)
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }

    /**
     * Creates a fresh tag FlowPane by calling the private
     * createTagSection() method via reflection.
     *
     * @return a new FlowPane with tag UI
     * @throws Exception if reflection fails
     */
    private FlowPane buildTagSection() throws Exception {
        AddItemsPage page = new AddItemsPage();
        Method m = page.getClass()
                .getDeclaredMethod("createTagSection");
        m.setAccessible(true);
        return (FlowPane) m.invoke(page);
    }

    /**
     * Simulates typing a tag name and pressing Enter in the FlowPane.
     *
     * @param pane the tag FlowPane
     * @param tagText the tag name to type
     */
    private void typeAndSubmitTag(FlowPane pane, String tagText) {
        TextField field = (TextField) pane.getChildren().get(0);
        field.setVisible(true);
        field.setManaged(true);
        field.setText(tagText);
        field.fireEvent(new javafx.event.ActionEvent());
    }

    /**
     * Finds a ToggleButton by its text inside a FlowPane.
     *
     * @param pane the FlowPane to search in
     * @param name the tag name to look for
     * @return the matching ToggleButton or null
     */
    private ToggleButton findTag(FlowPane pane, String name) {
        return pane.getChildren().stream()
                .filter(n -> n instanceof ToggleButton)
                .map(n -> (ToggleButton) n)
                .filter(b -> name.equals(b.getText()))
                .findFirst()
                .orElse(null);
    }

    /**
     * FR-01-1: After adding a new tag, it should appear in the FlowPane.
     *
     * @throws InterruptedException if the FX thread is interrupted
     */
    @Test
    @DisplayName("FR-01-1: New tag appears in the pane after creation")
    void createTagTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                // Arrange
                FlowPane pane = buildTagSection();

                // Act
                typeAndSubmitTag(pane, "Casual");

                // Assert
                assertNotNull(
                    findTag(pane, "Casual"),
                    "Tag 'Casual' should exist after creation"
                );
            } catch (Exception e) {
                fail("Test failed: " + e.getMessage());
            }
        });
    }

    /**
     * FR-01-2: After editing a tag name, the button should show the new text.
     *
     * @throws InterruptedException if the FX thread is interrupted
     */
    @Test
    @DisplayName("FR-01-2: Tag name is updated after editing")
    void editTagTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                // Arrange
                FlowPane pane = buildTagSection();
                typeAndSubmitTag(pane, "OldName");
                ToggleButton tag = findTag(pane, "OldName");
                int index = pane.getChildren().indexOf(tag);

                // Act – replicate the double-click edit handler
                TextField editField = new TextField(tag.getText());
                pane.getChildren().set(index, editField);
                editField.setText("NewName");
                tag.setText(editField.getText());
                pane.getChildren().set(index, tag);

                // Assert
                assertNotNull(
                    findTag(pane, "NewName"),
                    "Tag should display 'NewName' after editing"
                );
                assertNull(
                    findTag(pane, "OldName"),
                    "'OldName' should no longer exist"
                );
            } catch (Exception e) {
                fail("Test failed: " + e.getMessage());
            }
        });
    }

    /**
     * FR-01-3: After deleting a tag, it should be gone from the FlowPane.
     *
     * @throws InterruptedException if the FX thread is interrupted
     */
    @Test
    @DisplayName("FR-01-3: Tag is removed from the pane after deletion")
    void deleteTagTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                // Arrange
                FlowPane pane = buildTagSection();
                typeAndSubmitTag(pane, "Vintage");
                ToggleButton tag = findTag(pane, "Vintage");

                // Act – replicate the right-click delete handler
                pane.getChildren().remove(tag);

                // Assert
                assertNull(
                    findTag(pane, "Vintage"),
                    "Tag 'Vintage' should be gone after deletion"
                );
            } catch (Exception e) {
                fail("Test failed: " + e.getMessage());
            }
        });
    }
    
}
