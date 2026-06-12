package com.mycompany.irr00_group_project.database;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ClothingRepository}
 *
 * @author Alexandra Varapai
 */
public class ClothingRepositoryTest extends CsvTestBase {

    @Test
    @DisplayName("item added with addItem() appears in getAllItems()")
    void addItemAppearsInList() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0));
        List<ClothingItem> all = repo.getAllItems();
        assertEquals(1, all.size());
        assertEquals("Shirt", all.get(0).getName());
    }

    @Test
    @DisplayName("2 items added with addItem() appear in getAllItems()")
    void addMultipleItemsAppearInList() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0));
        repo.addItem(new ClothingItem(2, "Blazer", false, ClothingCategory.TOP, 0));
        List<ClothingItem> all = repo.getAllItems();
        assertEquals(2, all.size());
        assertEquals("Shirt", all.get(0).getName());
        assertEquals("Blazer", all.get(1).getName());
    }

    @Test
    @DisplayName("getAllItems() is empty if no items were added")
    void getAllItemsIsEmpty() {
        ClothingRepository repo = new ClothingRepository();
        List<ClothingItem> all = repo.getAllItems();
        assertTrue(all.isEmpty());
    }

    @Test
    @DisplayName("generateNextId() returns next available item ID (max existing ID + 1)")
    void generateNextIdIncrements() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0));
        repo.addItem(new ClothingItem(4, "Trousers", true, ClothingCategory.BOTTOM, 1));
        assertEquals(5, repo.generateNextId());
    }

    @Test
    @DisplayName("generateNextId() returns 1 when repository is empty")
    void generateNextIdEmpty() {
        ClothingRepository repo = new ClothingRepository();
        assertEquals(1, repo.generateNextId());
    }

    @Test
    @DisplayName("getItemById() returns correct item by its ID")
    void getItemByIdCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(3, "Hoodie", true, ClothingCategory.TOP, 0));
        ClothingItem found = repo.getItemById(3);
        assertNotNull(found);
        assertEquals("Hoodie", found.getName());
    }

    @Test
    @DisplayName("getItemById() returns null for unknown ID")
    void getItemByIdNotFound() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(3, "T-shirt", true, ClothingCategory.TOP, 0));
        assertNull(repo.getItemById(10));
    }

    @Test
    @DisplayName("changeItem() updates existing item's details (identified by ID)")
    void changeItemCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0));
        repo.changeItem(new ClothingItem(1, "Blouse", true, ClothingCategory.BOTTOM, 1));
        ClothingItem found = repo.getItemById(1);
        assertEquals("Blouse", found.getName());
        assertTrue(found.isFavorite());
        assertEquals(ClothingCategory.BOTTOM, found.getCategory());
        assertEquals(1, found.getWornCount());
    }

    @Test
    @DisplayName("changeItem() does not update item's details with unknown ID")
    void changeItemUnknownID() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0));
        repo.changeItem(new ClothingItem(3, "Blouse", true, ClothingCategory.BOTTOM, 0));
        ClothingItem found = repo.getItemById(1);
        assertEquals("Shirt", found.getName());
        assertFalse(found.isFavorite());
        assertEquals(ClothingCategory.TOP, found.getCategory());
        assertEquals(0, found.getWornCount());
    }

    @Test
    @DisplayName("deleteItem() removes the item so getAllItems() is empty")
    void deleteItemCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Jeans", true, ClothingCategory.BOTTOM, 0));
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
    @DisplayName("getTop5Worn() returns items sorted descending by wornCount")
    void getTop5WornCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Blouse", false, ClothingCategory.TOP, 3));
        repo.addItem(new ClothingItem(2, "Shirt", true, ClothingCategory.TOP, 0));
        repo.addItem(new ClothingItem(3, "Jacket", false, ClothingCategory.TOP, 1));
        List<ClothingItem> top = repo.getTop5Worn();
        assertEquals(3, top.get(0).getWornCount());
        assertEquals(1, top.get(1).getWornCount());
        assertEquals(0, top.get(2).getWornCount());
    }

    @Test
    @DisplayName("getLeast5Worn() returns items sorted ascending by wornCount")
    void getLeast5WornCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Blouse", false, ClothingCategory.TOP, 0));
        repo.addItem(new ClothingItem(2, "Shirt", true, ClothingCategory.TOP, 1));
        repo.addItem(new ClothingItem(3, "Jacket", false, ClothingCategory.TOP, 2));
        List<ClothingItem> least = repo.getLeast5Worn();
        assertEquals(0, least.get(0).getWornCount());
        assertEquals(1, least.get(1).getWornCount());
        assertEquals(2, least.get(2).getWornCount());
    }

    @Test
    @DisplayName("getTop5FavouriteItems() returns only favourite items")
    void getTop5FavouritesCorrect() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Hoodie", true, ClothingCategory.TOP, 2));
        repo.addItem(new ClothingItem(2, "T-shirt", false, ClothingCategory.TOP, 7));
        List<ClothingItem> favs = repo.getTop5FavouriteItems();
        assertEquals(1, favs.size());
        assertEquals("Hoodie", favs.get(0).getName());
        assertTrue(favs.get(0).isFavorite());
        assertFalse(favs.stream().anyMatch(i -> i.getName().equals("T-shirt")));
    }

    @Test
    @DisplayName("getTop5FavouriteItems() returns favourite items sorted descending by wornCount")
    void getTop5FavouritesSortedDescending() {
        ClothingRepository repo = new ClothingRepository();
        repo.addItem(new ClothingItem(1, "Trousers", true, ClothingCategory.BOTTOM, 2));
        repo.addItem(new ClothingItem(2, "Earrings", true, ClothingCategory.ACCESSORY, 8));
        repo.addItem(new ClothingItem(3, "Trainers", true, ClothingCategory.FOOTWEAR, 5));
        List<ClothingItem> favs = repo.getTop5FavouriteItems();
        assertEquals(8, favs.get(0).getWornCount());
        assertEquals(5, favs.get(1).getWornCount());
        assertEquals(2, favs.get(2).getWornCount());
    }

}
