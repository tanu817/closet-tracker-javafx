package com.mycompany.irr00_group_project.database;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link OutfitRepository}
 *
 * @author Alexandra Varapai
 */
public class OutfitRepositoryTest extends CsvTestBase {

    // ==================
    // TESTS
    // ==================
    @Test
    @DisplayName("outfit added with addOutfit() appears in getAllOutfits()")
    void addOutfitAppearsInList() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Summer Look"));
        List<Outfit> all = repo.getAllOutfits();
        assertEquals(1, all.size());
        assertEquals("Summer Look", all.get(0).getName());
    }

    @Test
    @DisplayName("2 outfits added with addOutfit() appear in getAllOutfits()")
    void addMultipleOutfitsAppearInList() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Summer Look"));
        repo.addOutfit(new Outfit(2, "Party Mood"));
        List<Outfit> all = repo.getAllOutfits();
        assertEquals(2, all.size());
        assertEquals("Summer Look", all.get(0).getName());
        assertEquals("Party Mood", all.get(1).getName());
    }

    @Test
    @DisplayName("getAllOutfits() is empty if no outfits were added")
    void getAllOutfitsIsEmpty() {
        OutfitRepository repo = new OutfitRepository();
        assertTrue(repo.getAllOutfits().isEmpty());
    }

    @Test
    @DisplayName("generateNextId() returns next available outfit ID (max existing ID + 1)")
    void generateNextIdIncrements() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Pink Mood"));
        repo.addOutfit(new Outfit(2, "Casual"));
        assertEquals(3, repo.generateNextId());
    }

    @Test
    @DisplayName("generateNextId() returns 1 when repository is empty")
    void generateNextIdEmpty() {
        OutfitRepository repo = new OutfitRepository();
        assertEquals(1, repo.generateNextId());
    }

    @Test
    @DisplayName("updateOutfit() updates outfit name correctly")
    void updateOutfitName() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Sunny Look"));
        repo.updateOutfit(new Outfit(1, "Rainy Look"));
        List<Outfit> all = repo.getAllOutfits();
        assertEquals("Rainy Look", all.get(0).getName());
    }

    @Test
    @DisplayName("updateOutfit() updates only the correct outfit and leaves others unchanged")
    void updateOutfitDoesNotAffectOthers() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Sunny Look"));
        repo.addOutfit(new Outfit(2, "Rainy Look"));
        repo.updateOutfit(new Outfit(1, "Cloudy Look"));
        List<Outfit> all = repo.getAllOutfits();
        assertEquals("Cloudy Look", all.get(0).getName());
        assertEquals("Rainy Look", all.get(1).getName());
    }

    @Test
    @DisplayName("updateOutfit() does not update outfit's name entering unknown ID")
    void updateOutfitUnknownID() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Sunny Look"));
        repo.updateOutfit(new Outfit(2, "Cloudy Look"));
        List<Outfit> all = repo.getAllOutfits();
        assertEquals("Sunny Look", all.get(0).getName());
    }

    @Test
    @DisplayName("deleteOutfit() removes the outfit so getAllOutfits() is empty")
    void deleteOutfitCorrect() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(5, "Yellow and Blue Mood"));
        repo.deleteOutfit(5);
        assertTrue(repo.getAllOutfits().isEmpty());
    }

    @Test
    @DisplayName("deleteOutfit() removes only the identified outfit")
    void deleteOutfitIdentified() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Lovely Outfit"));
        repo.addOutfit(new Outfit(2, "Pinky Mood"));
        repo.deleteOutfit(2);
        List<Outfit> all = repo.getAllOutfits();
        assertEquals(1, all.size());
        assertEquals("Lovely Outfit", all.get(0).getName());
    }

    @Test
    @DisplayName("deleteOutfit() does not remove relationships of other outfits")
    void deleteOutfitKeepsOtherRelationships() {
        ClothingRepository clothingRepo = new ClothingRepository();
        clothingRepo.addItem(new ClothingItem(1, "Blouse", false, ClothingCategory.TOP, 0));
        clothingRepo.addItem(new ClothingItem(2, "Jeans", true, ClothingCategory.BOTTOM, 1));
        clothingRepo.addItem(new ClothingItem(3, "Shirt", true, ClothingCategory.TOP, 1));
        clothingRepo.addItem(new ClothingItem(4, "Skirt", true, ClothingCategory.BOTTOM, 4));
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Casual Look"));
        repo.addOutfit(new Outfit(2, "Evening Outfit"));
        repo.addItemToOutfit(1, 1, 2);
        repo.addItemToOutfit(2, 3, 4);
        repo.deleteOutfit(1);
        assertFalse(repo.getItemIdsForOutfit(2).isEmpty(),
                "Relationships of outfit 2 should still exist after outfit 1 is deleted");
    }

    @Test
    @DisplayName("deleteOutfit() does not remove relationships of same items with other outfits")
    void deleteOutfitKeepsOtherRelationshipsSameItems() {
        ClothingRepository clothingRepo = new ClothingRepository();
        clothingRepo.addItem(new ClothingItem(1, "Blouse", false, ClothingCategory.TOP, 0));
        clothingRepo.addItem(new ClothingItem(2, "Jeans", true, ClothingCategory.BOTTOM, 1));
        clothingRepo.addItem(new ClothingItem(3, "Shirt", true, ClothingCategory.TOP, 1));
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Casual Look"));
        repo.addOutfit(new Outfit(2, "Casual Evening Outfit"));
        repo.addItemToOutfit(1, 1, 2);
        repo.addItemToOutfit(2, 3, 2);
        repo.deleteOutfit(1);
        assertFalse(repo.getItemIdsForOutfit(2).isEmpty(),
                "Relationships of outfit 2 with same items should still exist after outfit 1 is deleted");
    }

    @Test
    @DisplayName("deleteOutfit() removes all item relationships for that outfit")
    void deleteOutfitRemovesAllItemRelationships() {
        ClothingRepository clothingRepo = new ClothingRepository();
        clothingRepo.addItem(new ClothingItem(1, "T-shirt", true, ClothingCategory.TOP, 0));
        clothingRepo.addItem(new ClothingItem(2, "Jeans", true, ClothingCategory.BOTTOM, 0));
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Casual Look"));
        repo.addItemToOutfit(1, 1, 2);
        repo.deleteOutfit(1);
        assertTrue(repo.getItemIdsForOutfit(1).isEmpty(),
                "All item relationships for outfit should be removed after outfit is deleted");
    }

    @Test
    @DisplayName("addItemToOutfit() saves item IDs retrievable by getItemIdsForOutfit()")
    void addItemToOutfitPersists() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Casual Look"));
        repo.addItemToOutfit(1, 10, 20);
        List<Integer> ids = repo.getItemIdsForOutfit(1);
        assertEquals(2, ids.size());
        assertTrue(ids.contains(10));
        assertTrue(ids.contains(20));
    }

    @Test
    @DisplayName("getItemIdsForOutfit() returns empty list for unknown outfit ID")
    void getItemIdsForOutfitNotFound() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Casual Look"));
        repo.addItemToOutfit(1, 10, 20);
        assertTrue(repo.getItemIdsForOutfit(999).isEmpty());
    }

    @Test
    @DisplayName("getItemIdsForOutfit() returns the correct item IDs for an outfit")
    void getItemIdsReturnsCorrectIds() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Evening Look"));
        repo.addItemToOutfit(1, 3, 7);
        List<Integer> ids = repo.getItemIdsForOutfit(1);
        assertTrue(ids.contains(3));
        assertTrue(ids.contains(7));
    }

    @Test
    @DisplayName("addOutfit() correctly saves and retrieves name containing commas and quotes")
    void addOutfitWithCommasAndQuotes() {
        OutfitRepository repo = new OutfitRepository();
        repo.addOutfit(new Outfit(1, "Casual, Summer \"Look\""));
        List<Outfit> all = repo.getAllOutfits();
        assertEquals("Casual, Summer \"Look\"", all.get(0).getName(),
                "Name with commas and quotes should be escaped and unescaped correctly");
    }

}
