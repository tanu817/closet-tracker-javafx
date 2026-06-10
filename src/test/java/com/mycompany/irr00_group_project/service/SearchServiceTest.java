package com.mycompany.irr00_group_project.service;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SearchService}.
 *
 * @author  Alexandra Varapai
 */
public class SearchServiceTest extends CsvTestBase {

    /**
     * Seeds test data into the CSV files before each test.
     *
     * @throws IOException if file write fails
     */
    @BeforeEach
    void seedData() throws IOException {
        writeFile("clothing_items.csv",
                "id,name,isFavorite,category,wornCount\n"
              + "1,White Tee,false,TOP,3\n"
              + "2,Blue Jeans,false,BOTTOM,1\n"
              + "3,Red Dress,false,ONE_PIECE,0\n");
        writeFile("tags.csv",
                "id,name,category\n"
              + "5,White,COLOR\n"
              + "7,Blue,COLOR\n"
              + "17,Casual,EVENT\n");
        writeFile("clothing_tags.csv",
                "clothingId,tagId\n"
              + "1,5\n"
              + "1,17\n"
              + "2,7\n"
              + "2,17\n");
    }

    @Test
    @DisplayName("getAllTagNames() returns all 3 tag names")
    void getAllTagNamesReturnsAll() {
        SearchService service = new SearchService();
        List<String> names = service.getAllTagNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("White"));
        assertTrue(names.contains("Blue"));
        assertTrue(names.contains("Casual"));
    }

    @Test
    @DisplayName("getAllTags() returns 3 Tag objects")
    void getAllTagsReturnsObjects() {
        SearchService service = new SearchService();
        assertEquals(3, service.getAllTags().size());
    }

    @Test
    @DisplayName("getTagIdByName() returns correct ID")
    void getTagIdByNameFound() {
        SearchService service = new SearchService();
        assertEquals(5, service.getTagIdByName("White"));
        assertEquals(7, service.getTagIdByName("Blue"));
    }

    @Test
    @DisplayName("getTagIdByName() is case-insensitive")
    void getTagIdByNameCaseInsensitive() {
        assertEquals(17, new SearchService().getTagIdByName("casual"));
    }

    @Test
    @DisplayName("getTagIdByName() returns -1 for unknown tag")
    void getTagIdByNameNotFound() {
        assertEquals(-1, new SearchService().getTagIdByName("NonExistent"));
    }

    @Test
    @DisplayName("searchByTagIds() with empty list returns all 3 items")
    void searchEmptyTagIdsReturnsAll() {
        List<ClothingItem> results =
                new SearchService().searchByTagIds(Collections.emptyList());
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("searchByTagIds() with null returns all 3 items")
    void searchNullTagIdsReturnsAll() {
        assertEquals(3, new SearchService().searchByTagIds(null).size());
    }

    @Test
    @DisplayName("searchByTagIds() with White tag (id=5) returns only item 1")
    void searchByWhiteTagReturnsItem1() {
        List<ClothingItem> results =
                new SearchService().searchByTagIds(Collections.singletonList(5));
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());
    }

    @Test
    @DisplayName("searchByTagIds() with Blue tag (id=7) returns only item 2")
    void searchByBlueTagReturnsItem2() {
        List<ClothingItem> results =
                new SearchService().searchByTagIds(Collections.singletonList(7));
        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getId());
    }

    @Test
    @DisplayName("searchByTagIds() AND logic: White AND Casual returns only item 1")
    void searchAndLogicReturnsItem1() {
        List<ClothingItem> results =
                new SearchService().searchByTagIds(Arrays.asList(5, 17));
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());
    }

    @Test
    @DisplayName("searchByTagIds() with Casual returns 2 items")
    void searchByCasualReturnsTwoItems() {
        assertEquals(2, new SearchService().searchByTagIds(
                Collections.singletonList(17)).size());
    }

    @Test
    @DisplayName("searchByTagIds() returns empty when no item matches all tags")
    void searchNoMatchReturnsEmpty() {
        // White AND Blue — no item has both
        assertTrue(new SearchService().searchByTagIds(Arrays.asList(5, 7)).isEmpty());
    }

    @Test
    @DisplayName("searchByTagId() single-ID overload works correctly")
    void searchByTagIdOverload() {
        List<ClothingItem> results = new SearchService().searchByTagId(5);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());
    }
}
