package com.mycompany.irr00_group_project.database;

import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Clothing Repository
 *
 * @author Alexandra Varapai
 */
public class ClothingRepositoryTest {

    //Original CSV file contents saved before each test
    private String originalItems;
    private String originalOutfitItems;
    private String originalClothingTags;

    /**
     * Saves original CSV files and replaces them with empty test versions.
     *
     * @throws IOException if reading or writing a file fails
     */
    @BeforeEach
    void setUp() throws IOException {
        originalItems = readOrEmpty("clothing_items.csv");
        originalOutfitItems = readOrEmpty("outfit_items.csv");
        originalClothingTags = readOrEmpty("clothing_tags.csv");
        writeFile("clothing_items.csv", "id,name,isFavorite,category,wornCount\n");
        writeFile("outfit_items.csv", "outfitId,clothingId1,clothingId2\n");
        writeFile("clothing_tags.csv", "clothingId,tagId\n");
    }

    /**
     * Restores original CSV files after each test.
     *
     * @throws IOException if reading or writing a file fails
     */
    @AfterEach
    void restoreFile() throws IOException {
        writeFile("clothing_items.csv", originalItems);
        writeFile("outfit_items.csv", originalOutfitItems);
        writeFile("clothing_tags.csv", originalClothingTags);
    }

    // ==================
    // TESTS
    // ==================
    @Test
    @DisplayName("item added with addItem() appears in getAllItems()")
    void addItemAppearsInList() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Shirt", ClothingCategory.TOP));
        List<ClothingItem> all = repo.getAllItems();
        assertEquals(1, all.size());
        assertEquals("Shirt", all.get(0).getName());
    }

    @Test
    @DisplayName("generateNextId() returns 1 when repository is empty")
    void generateNextIdEmpty() {
        ClothingRepository repo = new ClothingRepository();
        assertEquals(1, repo.generateNextId());
    }

    @Test
    @DisplayName("generateNextId() returns max existing ID + 1")
    void generateNextIdIncrements() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "A", ClothingCategory.TOP));
        repo.addItem(new ClothingItem(5, "B", ClothingCategory.BOTTOM));
        assertEquals(6, repo.generateNextId());
    }

    @Test
    @DisplayName("getItemById() returns correct item")
    void getItemByIdCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(3, "Hoodie", ClothingCategory.TOP));
        ClothingItem found = repo.getItemById(3);
        assertNotNull(found);
        assertEquals("Hoodie", found.getName());
    }

    @Test
    @DisplayName("getItemById() returns null for unknown ID")
    void getItemByIdNotFound() {
        ClothingRepository repo = new ClothingRepository();
        assertNull(repo.getItemById(999));
    }

    @Test
    @DisplayName("deleteItem() removes the item")
    void deleteItemCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "ToDelete", ClothingCategory.TOP));
        repo.deleteItem(1);
        assertTrue(repo.getAllItems().isEmpty());
    }

    @Test
    @DisplayName("markAsWorn() increments wornCount by 1")
    void markAsWornIncrementsWornCount() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Jeans", false, ClothingCategory.BOTTOM, 0));
        repo.markAsWorn(1);
        assertEquals(1, repo.getItemById(1).getWornCount());
    }

    @Test
    @DisplayName("changeItem() updates existing item's details")
    void changeItemCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Old Name", ClothingCategory.TOP));
        repo.changeItem(new ClothingItem(1, "New Name", false, ClothingCategory.BOTTOM, 5));
        ClothingItem found = repo.getItemById(1);
        assertEquals("New Name", found.getName());
        assertEquals(5, found.getWornCount());
    }

    @Test
    @DisplayName("getTop5Worn() returns items sorted descending by wornCount")
    void getTop5WornCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "A", false, ClothingCategory.TOP, 3));
        repo.addItem(new ClothingItem(2, "B", false, ClothingCategory.TOP, 9));
        repo.addItem(new ClothingItem(3, "C", false, ClothingCategory.TOP, 1));
        List<ClothingItem> top = repo.getTop5Worn();
        assertEquals(9, top.get(0).getWornCount());
        assertEquals(3, top.get(1).getWornCount());
        assertEquals(1, top.get(2).getWornCount());
    }

    @Test
    @DisplayName("getLeast5Worn() returns items sorted ascending by wornCount")
    void getLeast5WornCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "A", false, ClothingCategory.TOP, 5));
        repo.addItem(new ClothingItem(2, "B", false, ClothingCategory.TOP, 1));
        repo.addItem(new ClothingItem(3, "C", false, ClothingCategory.TOP, 3));
        List<ClothingItem> least = repo.getLeast5Worn();
        assertEquals(1, least.get(0).getWornCount());
        assertEquals(3, least.get(1).getWornCount());
        assertEquals(5, least.get(2).getWornCount());
    }

    @Test
    @DisplayName("getTop5FavouriteItems() returns only favourite items")
    void getTop5FavouritesCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Fav", true, ClothingCategory.TOP, 2));
        repo.addItem(new ClothingItem(2, "NotFav", false, ClothingCategory.TOP, 10));
        List<ClothingItem> favs = repo.getTop5FavouriteItems();
        assertEquals(1, favs.size());
        assertTrue(favs.get(0).isFavorite());
    }

    @Test
    @DisplayName("getTop5FavouriteItems() returns favourite items sorted descending by wornCount")
    void getTop5FavouritesSortedDescending() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Fav A", true, ClothingCategory.BOTTOM, 2));
        repo.addItem(new ClothingItem(2, "Fav B", true, ClothingCategory.ACCESSORY, 8));
        repo.addItem(new ClothingItem(3, "Fav C", true, ClothingCategory.FOOTWEAR, 5));
        List<ClothingItem> favs = repo.getTop5FavouriteItems();
        assertEquals(8, favs.get(0).getWornCount());
        assertEquals(5, favs.get(1).getWornCount());
        assertEquals(2, favs.get(2).getWornCount());
    }

    // ==================
    // Helper methods
    // ==================
    private String readOrEmpty(String name) throws IOException {
        File f = new File(name);
        if (!f.exists()) {
            return "";
        }
        return new String(Files.readAllBytes(f.toPath()));
    }

    private void writeFile(String name, String content) throws IOException {
        try (FileWriter fw = new FileWriter(name)) {
            fw.write(content);
        }
    }
}
