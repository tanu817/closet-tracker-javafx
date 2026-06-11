package com.mycompany.irr00_group_project.service;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DashboardService}.
 *
 * @author  Alexandra Varapai
 */
public class DashboardServiceTest extends CsvTestBase {

    /**
     * Seeds 6 clothing items with varying wornCount and "favorite" flags.
     *
     * @throws IOException if file write fails
     */
    @BeforeEach
    void seedData() throws IOException {
        writeFile("clothing_items.csv",
                "id,name,isFavorite,category,wornCount\n"
              + "1,Item A,true,TOP,0\n"
              + "2,Item B,false,BOTTOM,2\n"
              + "3,Item C,true,TOP,4\n"
              + "4,Item D,false,BOTTOM,8\n"
              + "5,Item E,false,ONE_PIECE,6\n"
              + "6,Item F,true,TOP,1\n");
    }

    @Test
    @DisplayName("getTop5Worn() returns items sorted descending by wornCount")
    void getTop5WornOrdered() {
        List<ClothingItem> top5 = new DashboardService().getTop5Worn();
        assertTrue(top5.size() <= 5);
        assertEquals(4, top5.get(0).getId(), "Item D should be first");
        for (int i = 0; i < top5.size() - 1; i++) {
            assertTrue(top5.get(i).getWornCount() >= top5.get(i + 1).getWornCount());
        }
    }

    
     @Test
    @DisplayName("getTop5Favourites() returns only favourite items")
    void getTop5FavouritesOnlyFavs() {
        List<ClothingItem> favs = new DashboardService().getTop5Favourites();
        assertEquals(3, favs.size());
        for (ClothingItem item : favs) {
            assertTrue(item.isFavorite());
        }
    }
    
    @Test
    @DisplayName("getLeast5Worn() returns items sorted ascending by wornCount")
    void getLeast5WornOrdered() {
        List<ClothingItem> least5 = new DashboardService().getLeast5Worn();
        assertTrue(least5.size() <= 5);
        assertEquals(1, least5.get(0).getId(), "Item A should be first");
        for (int i = 0; i < least5.size() - 1; i++) {
            assertTrue(least5.get(i).getWornCount() <= least5.get(i + 1).getWornCount());
        }
    }

 

    @Test
    @DisplayName("getStats() returns correct totalItems, totalWears, totalFavourites")
    void getStatsCorrect() {
        DashboardService.DashboardStats stats = new DashboardService().getStats();
        assertEquals(6, stats.totalItems);
        assertEquals(30, stats.totalWears);
        assertEquals(3, stats.totalFavourites);
        assertEquals(5.0, stats.avgWearsPerItem, 0.001);
    }

    @Test
    @DisplayName("getStats() returns avgWearsPerItem=0 when closet is empty")
    void getStatsEmptyCloset() throws IOException {
        writeFile("clothing_items.csv", "id,name,isFavorite,category,wornCount\n");
        DashboardService.DashboardStats stats = new DashboardService().getStats();
        assertEquals(0, stats.totalItems);
        assertEquals(0.0, stats.avgWearsPerItem, 0.0);
    }

    @Test
    @DisplayName("markItemWorn() increments wornCount and persists it")
    void markItemWornPersists() {
        DashboardService service = new DashboardService();
        service.markItemWorn(6); // Item F starts at 0
        assertEquals(31, service.getStats().totalWears);
    }

    @Test
    @DisplayName("getAllItemsWithTags() returns all 6 items")
    void getAllItemsWithTagsCount() {
        assertEquals(6, new DashboardService().getAllItemsWithTags().size());
    }
}
