package com.mycompany.irr00_group_project.integration;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;
import com.mycompany.irr00_group_project.service.AddItemService;
import com.mycompany.irr00_group_project.service.OutfitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Outfit Creation
 *
 * @author Alexandra Varapai
 */
public class OutfitCreationIntegrationTest extends CsvTestBase {

    @Test
    @DisplayName("Outfit contains exactly 2 selected items")
    void outfitHasTwoItems() {
        AddItemService addService = new AddItemService();
        ClothingItem top = addService.addItem("Shirt", ClothingCategory.TOP,
                Collections.emptyList());
        ClothingItem bot = addService.addItem("Jeans", ClothingCategory.BOTTOM,
                Collections.emptyList());
        
        OutfitService outfitService = new OutfitService();
        Outfit outfit = outfitService.createOutfit("Casual", top.getId(), bot.getId());
        assertEquals(2, outfitService.getItemsForOutfit(outfit.getId()).size(),
                "Outfit must consist of exactly 2 items");
    }

    @Test
    @DisplayName("Wearing an outfit increments wornCount on both items")
    void wearOutfitIncrementsItems() {
        AddItemService addService = new AddItemService();
        ClothingItem top = addService.addItem("Shirt", ClothingCategory.TOP,
                Collections.emptyList());
        ClothingItem bot = addService.addItem("Trousers", ClothingCategory.BOTTOM,
                Collections.emptyList());
        
        OutfitService outfitService = new OutfitService();
        Outfit outfit = outfitService.createOutfit("University Outfit", top.getId(), bot.getId());
        outfitService.wearOutfit(outfit.getId());
        for (ClothingItem item : outfitService.getItemsForOutfit(outfit.getId())) {
            assertEquals(1, item.getWornCount(),
                    "wornCount should be 1 after one wear for item " + item.getId());
        }
    }

    @Test
    @DisplayName("Deleting an outfit removes it from getAllOutfits()")
    void deleteOutfitRemovedFromList() {
        AddItemService addService = new AddItemService();
        ClothingItem top = addService.addItem("T-shirt", ClothingCategory.TOP,
                Collections.emptyList());
        ClothingItem bot = addService.addItem("Pants", ClothingCategory.BOTTOM,
                Collections.emptyList());
        
        OutfitService outfitService = new OutfitService();
        Outfit outfit = outfitService.createOutfit("Sport Outfit", top.getId(), bot.getId());
        int id = outfit.getId();
        outfitService.deleteOutfit(id);
        boolean found = outfitService.getAllOutfits().stream()
                .anyMatch(o -> o.getId() == id);
        assertFalse(found, "Deleted outfit must not appear in getAllOutfits()");
    }

    @Test
    @DisplayName("Outfit name is auto-generated from both item names when is blank")
    void autoNameCombinesBothItemNames() {
        AddItemService addService = new AddItemService();
        ClothingItem top = addService.addItem("Blouse", ClothingCategory.TOP,
                Collections.emptyList());
        ClothingItem bot = addService.addItem("Jeans", ClothingCategory.BOTTOM,
                Collections.emptyList());
        
        OutfitService outfitService = new OutfitService();
        Outfit outfit = outfitService.createOutfit(null, top.getId(), bot.getId());
        String expectedName = top.getName() + " + " + bot.getName();
        assertEquals(expectedName, outfit.getName(),
                "Auto-generated name should combine both item names");
    }
}
