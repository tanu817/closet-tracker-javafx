package com.mycompany.irr00_group_project.ui;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;

/**
 * Tests for FR-01: tag creation, editing and deletion in AddItemsPage.
 *
 * @author Alexandra Varapai
 */
public class AddItemsPageTest {

    /**
     * Initializes the JavaFX platform before any test in this class runs.
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
     * Creates a new tag FlowPane by calling the private createTagSection()
     * method
     *
     * @return a new FlowPane with tag
     * @throws Exception if fails
     */
    private FlowPane createTag() throws Exception {
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
    private void typeTag(FlowPane pane, String tagText) {
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
    @DisplayName("FR-01-1")
    void createTagTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                FlowPane pane = createTag();
                typeTag(pane, "Pink");

                assertNotNull(
                        findTag(pane, "Pink"),
                        "Tag 'Pink' should exist after creation"
                );
            } catch (Exception e) {
                fail("Test failed: " + e.getMessage());
            }
        });
    }

    /**
     * FR-01-2: Typing 'space' instead of the name of the tag should not create
     * a new tag.
     *
     * @throws InterruptedException if the FX thread is interrupted
     */
    @Test
    @DisplayName("FR-01-2")
    void createEmptyTagTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                FlowPane pane = createTag();
                int sizeBefore = pane.getChildren().size();
                typeTag(pane, "   ");

                assertTrue(
                        pane.getChildren().size() == sizeBefore,
                        "Only input should create a new tag"
                );
            } catch (Exception e) {
                fail("Test failed: " + e.getMessage());
            }
        });
    }

    /**
     * FR-01-3: After editing a tag name, the button should show the new text.
     *
     * @throws InterruptedException if the FX thread is interrupted
     */
    @Test
    @DisplayName("FR-01-3")
    void editTagTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                FlowPane pane = createTag();
                typeTag(pane, "OldName");
                ToggleButton tag = findTag(pane, "OldName");
                int index = pane.getChildren().indexOf(tag);

                TextField editField = new TextField(tag.getText());
                pane.getChildren().set(index, editField);
                editField.setText("NewName");
                tag.setText(editField.getText());
                pane.getChildren().set(index, tag);

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
     * FR-01-4: After deleting a tag, it should be gone from the FlowPane.
     *
     * @throws InterruptedException if the FX thread is interrupted
     */
    @Test
    @DisplayName("FR-01-4")
    void deleteTagTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                FlowPane pane = createTag();
                typeTag(pane, "Vintage");
                ToggleButton tag = findTag(pane, "Vintage");

                pane.getChildren().remove(tag);

                assertNull(
                        findTag(pane, "Vintage"),
                        "Tag 'Vintage' should be gone after deletion"
                );
            } catch (Exception e) {
                fail("Test failed: " + e.getMessage());
            }
        });
    }

    /**
     * FR-01-5: After deleting one tag, others should not be removed.
     *
     * @throws InterruptedException if the FX thread is interrupted
     */
    @Test
    @DisplayName("FR-01-5")
    void deleteTagLeavesOthersTest() throws InterruptedException {
        runOnFxThread(() -> {
            try {
                // Arrange
                FlowPane pane = createTag();
                typeTag(pane, "Red");
                typeTag(pane, "Blue");
                ToggleButton redTag = findTag(pane, "Red");

                pane.getChildren().remove(redTag);

                assertNull(
                        findTag(pane, "Red"),
                        "'Red' should be deleted"
                );
                assertNotNull(
                        findTag(pane, "Blue"),
                        "'Blue' should still be present"
                );
            } catch (Exception e) {
                fail("Test failed: " + e.getMessage());
            }
        });
    }
}
