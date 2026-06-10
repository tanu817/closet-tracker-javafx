package com.mycompany.irr00_group_project.service;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RecommendationService}.
 *
 * @author  Alexandra Varapai
 */
public class RecommendationServiceTest extends CsvTestBase {

    /**
     * Seeds 5 clothing items (2 tops, 2 bottoms, 1 one-piece).
     *
     * @throws IOException if file write fails
     */
    @BeforeEach
    void seedData() throws IOException {
        writeFile("clothing_items.csv",
                "id,name,isFavorite,category,wornCount\n"
              + "1,White Tee,false,TOP,0\n"
              + "2,Polo Shirt,false,TOP,0\n"
              + "3,Blue Jeans,false,BOTTOM,0\n"
              + "4,Black Skirt,false,BOTTOM,0\n"
              + "5,Red Dress,false,ONE_PIECE,0\n");
    }

    @Test
    @DisplayName("getMatchingItems() for a TOP returns the 2 BOTTOM items")
    void topMatchesBottoms() {
        ClothingItem top = new ClothingItem(1, "White Tee", ClothingCategory.TOP);
        List<ClothingItem> matches = new RecommendationService().getMatchingItems(top);
        assertEquals(2, matches.size());
        for (ClothingItem m : matches) {
            assertEquals(ClothingCategory.BOTTOM, m.getCategory());
        }
    }

    @Test
    @DisplayName("getMatchingItems() for a BOTTOM returns the 2 TOP items")
    void bottomMatchesTops() {
        ClothingItem bottom = new ClothingItem(3, "Blue Jeans", ClothingCategory.BOTTOM);
        List<ClothingItem> matches = new RecommendationService().getMatchingItems(bottom);
        assertEquals(2, matches.size());
        for (ClothingItem m : matches) {
            assertEquals(ClothingCategory.TOP, m.getCategory());
        }
    }

    @Test
    @DisplayName("getMatchingItems() for ONE_PIECE returns empty list")
    void onePieceReturnsEmpty() {
        ClothingItem dress = new ClothingItem(5, "Red Dress", ClothingCategory.ONE_PIECE);
        assertTrue(new RecommendationService().getMatchingItems(dress).isEmpty());
    }

    @Test
    @DisplayName("getMatchingItems() for null returns empty list")
    void nullItemReturnsEmpty() {
        assertTrue(new RecommendationService().getMatchingItems(null).isEmpty());
    }

    @Test
    @DisplayName("getMatchingItems() does not include the source item itself")
    void doesNotIncludeItemItself() {
        ClothingItem top = new ClothingItem(1, "White Tee", ClothingCategory.TOP);
        List<ClothingItem> matches = new RecommendationService().getMatchingItems(top);
        for (ClothingItem m : matches) {
            assertNotEquals(1, m.getId());
        }
    }
}
