package com.mycompany.irr00_group_project.service;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;

/**
 * Unit tests for {@link OutfitService}.
 *
 * @author Alexandra Varapai
 */
public class OutfitServiceTest extends CsvTestBase {

    /**
     * Seeds 3 clothing items before each test.
     *
     * @throws IOException if file write fails
     */
    @BeforeEach
    void seedData() throws IOException {
        writeFile("clothing_items.csv",
                "id,name,isFavorite,category,wornCount\n"
                + "1,Blouse,true,TOP,0\n"
                + "2,Blue Jeans,false,BOTTOM,1\n"
                + "3,Shirt,false,TOP,2\n");
    }

    @Test
    @DisplayName("createOutfit() persists and returns outfit with given name")
    void createOutfitWithCustomName() {
        Outfit outfit = new OutfitService().createOutfit("Summer Look", 1, 2);
        assertNotNull(outfit);
        assertEquals("Summer Look", outfit.getName());
    }

    @Test
    @DisplayName("createOutfit() auto-generates name containing item names when blank")
    void createOutfitAutoName() {
        Outfit outfit = new OutfitService().createOutfit("", 1, 2);
        assertNotNull(outfit);
        String name = outfit.getName();
        assertTrue(name.contains("Blouse") || name.contains("Blue Jeans"),
                "Auto-name should contain at least one item name, was: " + name);
    }

    @Test
    @DisplayName("createOutfit() throws IllegalArgumentException for invalid first item ID")
    void createOutfitInvalidItem1() {
        assertThrows(IllegalArgumentException.class,
                () -> new OutfitService().createOutfit("Rainy Mood", 10, 2));
    }

    @Test
    @DisplayName("createOutfit() throws IllegaArgumentException for invalid second item ID")
    void createOutfitInvalidItem2() {
        assertThrows(IllegalArgumentException.class,
                () -> new OutfitService().createOutfit("Rainy Mood", 1, 10));
    }

    @Test
    @DisplayName("createOutfit() throws IllegalStateException if outfit already exists")
    void createOutfitExists() {
        OutfitService service = new OutfitService();
        service.createOutfit("Sunny weather", 1, 2);
        assertThrows(IllegalStateException.class,
                () -> service.createOutfit("Hot weather", 1, 2)
        );
    }

    @Test
    @DisplayName("getAllOutfits() returns all created outfits")
    void getAllOutfitsReturnsAll() {
        OutfitService service = new OutfitService();
        service.createOutfit("Sunny Weather", 1, 2);
        service.createOutfit("Rainy Weather", 3, 2);
        assertEquals(2, service.getAllOutfits().size());
    }

    @Test
    @DisplayName("getOutfitById() returns the correct outfit by ID")
    void getOutfitByIdFound() {
        OutfitService service = new OutfitService();
        Outfit created = service.createOutfit("Sunny Weather", 1, 2);
        Outfit found = service.getOutfitById(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("Sunny Weather", found.getName());
    }

    @Test
    @DisplayName("getOutfitById() returns null for unknown ID")
    void getOutfitByIdNotFound() {
        assertNull(new OutfitService().getOutfitById(10));
    }

    @Test
    @DisplayName("getItemsForOutfit() returns the 2 component items")
    void getItemsForOutfitReturnsBoth() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Sunny Weather", 1, 2);
        List<ClothingItem> items = service.getItemsForOutfit(outfit.getId());
        assertEquals(2, items.size());
    }

    @Test
    @DisplayName("getItemsForOutfit() returns the correct items")
    void getItemsForOutfitReturnsCorrectItems() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Sunny Weather", 1, 2);
        List<ClothingItem> items = service.getItemsForOutfit(outfit.getId());
        assertTrue(items.stream().anyMatch(i -> i.getId() == 1));
        assertTrue(items.stream().anyMatch(i -> i.getId() == 2));
    }

    @Test
    @DisplayName("getItemsForOutfit() returns empty list for unknown outfit ID")
    void getItemsForOutfitNotFound() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Sunny Weather", 1, 2);
        assertTrue(service.getItemsForOutfit(10).isEmpty());
    }

    @Test
    @DisplayName("renameOutfit() renames an outfit and persists the change")
    void renameOutfit() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Sunny Weather", 1, 2);
        service.renameOutfit(outfit.getId(), "Hot Weather");
        assertEquals("Hot Weather", service.getOutfitById(outfit.getId()).getName());
    }

    @Test
    @DisplayName("renameOutfit() does nothing when outfit ID does not exist")
    void renameOutfitUnknownId() {
        OutfitService service = new OutfitService();
        service.createOutfit("Sunny Weather", 1, 2);
        service.renameOutfit(10, "Hot weather");
        assertNull(service.getOutfitById(10));
    }

    @Test
    @DisplayName("deleteOutfit() removes it from getAllOutfits()")
    void deleteOutfit() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Sunny Weather", 1, 2);
        service.deleteOutfit(outfit.getId());
        assertNull(service.getOutfitById(outfit.getId()));
    }

    @Test
    @DisplayName("deleteOutfitsContainingItem() removes all outfits that contain the specific item")
    void deleteOutfitsContainingItem() {
        OutfitService service = new OutfitService();
        service.createOutfit("Sunny Weather", 1, 2);
        service.createOutfit("Hot Weather", 3, 2);
        service.deleteOutfitsContainingItem(1);
        assertEquals(1, service.getAllOutfits().size());
    }

    @Test
    @DisplayName("getAllClothingItems() returns all 3 seeded items")
    void getAllClothingItems() {
        assertEquals(3, new OutfitService().getAllClothingItems().size());
    }
}
