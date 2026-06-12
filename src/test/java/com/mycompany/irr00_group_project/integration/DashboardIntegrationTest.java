package com.mycompany.irr00_group_project.integration;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.service.AddItemService;
import com.mycompany.irr00_group_project.service.DashboardService;
import com.mycompany.irr00_group_project.service.OutfitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Dashboard flow
 *
 * @author Alexandra Varapai
 */
public class DashboardIntegrationTest extends CsvTestBase {

    @Test
    @DisplayName("Wearing an outfit twice updates dashboard top-5-worn statistics")
    void wearOutfitAppearsInDashboardTop5() {
        AddItemService addService = new AddItemService();
        ClothingItem top = addService.addItem("Hoodie", ClothingCategory.TOP,
                Collections.emptyList());
        ClothingItem bot = addService.addItem("Jeans", ClothingCategory.BOTTOM,
                Collections.emptyList());
        
        OutfitService outfitService = new OutfitService();
        var outfit = outfitService.createOutfit("Daily", top.getId(), bot.getId());
        outfitService.wearOutfit(outfit.getId());
        outfitService.wearOutfit(outfit.getId());

        List<ClothingItem> top5 = new DashboardService().getTop5Worn();
        assertFalse(top5.isEmpty());
        assertEquals(2, top5.get(0).getWornCount(), "Top item should have 2 wears");
    }

    @Test
    @DisplayName("Dashboard statistics reflects amount of items and wears after only adding of items")
    void dashboardStatsAfterAdding() {
        AddItemService addService = new AddItemService();
        addService.addItem("Shirt", ClothingCategory.TOP,
                Collections.emptyList());
        addService.addItem("Pants", ClothingCategory.BOTTOM,
                Collections.emptyList());
        
        DashboardService.DashboardStats stats = new DashboardService().getStats();
        assertEquals(2, stats.totalItems);
        assertEquals(0, stats.totalWears);
    }

    @Test
    @DisplayName("markItemWorn() from DashboardService updates statistics")
    void markItemWornUpdatesStats() {
        AddItemService addService = new AddItemService();
        ClothingItem item = addService.addItem("Cap", ClothingCategory.HEADWEAR,
                Collections.emptyList());
        
        DashboardService service = new DashboardService();
        service.markItemWorn(item.getId());
        assertEquals(1, service.getStats().totalWears);
    }
}
