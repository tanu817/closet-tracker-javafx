package com.mycompany.irr00_group_project.service;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingItem;

/**
 * Unit tests for {@link DashboardService}.
 *
 * @author Alexandra Varapai
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
    void getTop5FavouritesOnlyFavourites() {
        List<ClothingItem> favs = new DashboardService().getTop5Favourites();
        assertEquals(3, favs.size());
        for (ClothingItem item : favs) {
            assertTrue(item.isFavorite());
        }
    }

    @Test
    @DisplayName("getTop5Favourites() returns favourite items sorted descending by wornCount")
    void getTop5FavouritesSortedDescending() {
        List<ClothingItem> favs = new DashboardService().getTop5Favourites();
        assertTrue(favs.size() <= 5);
        assertEquals(3, favs.get(0).getId(), "Item C should be first");
        for (int i = 0; i < favs.size() - 1; i++) {
            assertTrue(favs.get(i).getWornCount() >= favs.get(i + 1).getWornCount(),
                    "Favourites should be sorted descending by wornCount");
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
    @DisplayName("getStats() returns correct totalItems, totalWears, totalFavourites and avgWearPerItem")
    void getStatsCorrect() {
        DashboardService.DashboardStats stats = new DashboardService().getStats();
        assertEquals(6, stats.totalItems);
        assertEquals(21, stats.totalWears);
        assertEquals(3, stats.totalFavourites);
        assertEquals(3.5, stats.avgWearsPerItem, 0.001);
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
        service.markItemWorn(6);
        assertEquals(22, service.getStats().totalWears);
    }

    @Test
    @DisplayName("getAllItemsWithTags() returns all items")
    void getAllItemsWithTagsReturnsAll() {
        assertEquals(6, new DashboardService().getAllItemsWithTags().size());
    }

    @Test
    @DisplayName("getAllItemsWithTags() returns items with their tags")
    void getAllItemsWithTagsHasTags() throws IOException {
        writeFile("tags.csv",
                "id,name,category\n"
                + "1,Blue,COLOR\n");
        writeFile("clothing_tags.csv",
                "clothingId,tagId\n"
                + "1,1\n");
        List<ClothingItem> items = new DashboardService().getAllItemsWithTags();
        ClothingItem item1 = items.stream()
                .filter(i -> i.getId() == 1)
                .findFirst()
                .orElse(null);
        assertNotNull(item1);
        assertTrue(item1.hasTag(1), "Item A should have tag 1 loaded");
    }

    @Test
    @DisplayName("getAllItemsWithTags() returns items with no tags when none assigned")
    void getAllItemsWithTagsNoTags() {
        List<ClothingItem> items = new DashboardService().getAllItemsWithTags();
        for (ClothingItem item : items) {
            assertTrue(item.getTags().isEmpty(),
                    "Items should have no tags when none are assigned");
        }
    }
}
