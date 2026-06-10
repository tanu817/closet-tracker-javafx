package com.mycompany.irr00_group_project.service;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link OutfitService}.
 *
 * @author  Alexandra Varapai
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
              + "1,White Tee,false,TOP,0\n"
              + "2,Blue Jeans,false,BOTTOM,0\n"
              + "3,Red Dress,false,ONE_PIECE,2\n");
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
        assertTrue(name.contains("White Tee") || name.contains("Blue Jeans"),
                "Auto-name should contain at least one item name, was: " + name);
    }

    @Test
    @DisplayName("createOutfit() throws for invalid first item ID")
    void createOutfitInvalidItem1() {
        assertThrows(IllegalArgumentException.class,
                () -> new OutfitService().createOutfit("Bad", 999, 2));
    }

    @Test
    @DisplayName("createOutfit() throws for invalid second item ID")
    void createOutfitInvalidItem2() {
        assertThrows(IllegalArgumentException.class,
                () -> new OutfitService().createOutfit("Bad", 1, 999));
    }

    @Test
    @DisplayName("getAllOutfits() returns all created outfits")
    void getAllOutfitsReturnsAll() {
        OutfitService service = new OutfitService();
        service.createOutfit("Look 1", 1, 2);
        service.createOutfit("Look 2", 1, 2);
        assertEquals(2, service.getAllOutfits().size());
    }

    @Test
    @DisplayName("getOutfitById() returns the correct outfit")
    void getOutfitByIdFound() {
        OutfitService service = new OutfitService();
        Outfit created = service.createOutfit("My Outfit", 1, 2);
        Outfit found = service.getOutfitById(created.getId());
        assertNotNull(found);
        assertEquals("My Outfit", found.getName());
    }

    @Test
    @DisplayName("getOutfitById() returns null for unknown ID")
    void getOutfitByIdNotFound() {
        assertNull(new OutfitService().getOutfitById(9999));
    }

    @Test
    @DisplayName("getItemsForOutfit() returns the 2 component items")
    void getItemsForOutfitReturnsBoth() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Casual", 1, 2);
        assertEquals(2, service.getItemsForOutfit(outfit.getId()).size());
    }

    @Test
    @DisplayName("renameOutfit() updates the name in the CSV")
    void renameOutfit() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Old Name", 1, 2);
        service.renameOutfit(outfit.getId(), "New Name");
        assertEquals("New Name", service.getOutfitById(outfit.getId()).getName());
    }

    @Test
    @DisplayName("deleteOutfit() removes it from getAllOutfits()")
    void deleteOutfit() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("To Delete", 1, 2);
        service.deleteOutfit(outfit.getId());
        assertNull(service.getOutfitById(outfit.getId()));
    }

    @Test
    @DisplayName("wearOutfit() increments wornCount on both items")
    void wearOutfitIncrementsWornCount() {
        OutfitService service = new OutfitService();
        Outfit outfit = service.createOutfit("Daily", 1, 2);
        service.wearOutfit(outfit.getId());
        for (ClothingItem item : service.getItemsForOutfit(outfit.getId())) {
            assertEquals(1, item.getWornCount(),
                    "wornCount should be 1 after one wear for item " + item.getId());
        }
    }

    @Test
    @DisplayName("getAllClothingItems() returns all 3 seeded items")
    void getAllClothingItems() {
        assertEquals(3, new OutfitService().getAllClothingItems().size());
    }
}
