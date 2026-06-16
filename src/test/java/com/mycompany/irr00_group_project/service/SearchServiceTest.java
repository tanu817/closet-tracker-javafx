package com.mycompany.irr00_group_project.service;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
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
 * @author Alexandra Varapai
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
                + "1,Shirt,false,TOP,3\n"
                + "2,Jeans,false,BOTTOM,1\n"
                + "3,Dress,false,ONE_PIECE,0\n");
        writeFile("tags.csv",
                "id,name,category\n"
                + "1,White,COLOR\n"
                + "2,Blue,COLOR\n"
                + "3,Casual,EVENT\n");
        writeFile("clothing_tags.csv",
                "clothingId,tagId\n"
                + "1,1\n"
                + "1,2\n"
                + "2,2\n"
                + "3,3\n");
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
    @DisplayName("getAllTags() returns 3 tags")
    void getAllTagsReturnsAllTags() {
        SearchService service = new SearchService();
        assertEquals(3, service.getAllTags().size());
    }

    @Test
    @DisplayName("getTagIdByName() returns correct ID")
    void getTagIdByNameCorrect() {
        SearchService service = new SearchService();
        assertEquals(1, service.getTagIdByName("White"));
        assertEquals(2, service.getTagIdByName("Blue"));
    }

    @Test
    @DisplayName("getTagIdByName() is case-insensitive")
    void getTagIdByNameCaseInsensitive() {
        assertEquals(3, new SearchService().getTagIdByName("casual"));
        assertEquals(3, new SearchService().getTagIdByName("CASUAL"));
    }

    @Test
    @DisplayName("getTagIdByName() returns -1 for unknown tag")
    void getTagIdByNameForUnknownTag() {
        assertEquals(-1, new SearchService().getTagIdByName("Pink"));
    }

    @Test
    @DisplayName("searchByTagIds() with empty list returns all 3 items")
    void searchByTagIdsEmpty() {
        List<ClothingItem> results
                = new SearchService().searchByTagIds(Collections.emptyList());
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("searchByTagIds() with null list returns all 3 items")
    void searchByTagIdsNull() {
        assertEquals(3, new SearchService().searchByTagIds(null).size());
    }

    @Test
    @DisplayName("searchByTagIds() with specific tag returns only item with this tag")
    void searchByTagIdsOneItem() {
        List<ClothingItem> results
                = new SearchService().searchByTagIds(Collections.singletonList(1));
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());
    }

    @Test
    @DisplayName("searchByTagIds() with specific tag returns only item with this tag (2 items with the same tag)")
    void searchByTagIdsMultipleItems() {
        List<ClothingItem> results
                = new SearchService().searchByTagIds(Collections.singletonList(2));
        assertEquals(2, results.size());
        assertEquals(1, results.get(0).getId());
        assertEquals(2, results.get(1).getId());
    }

    @Test
    @DisplayName("searchByTagIds() AND logic: tags White and Blue returns only item Shirt")
    void searchAndLogicReturnsItem1() {
        List<ClothingItem> results
                = new SearchService().searchByTagIds(Arrays.asList(1, 2));
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());
    }

    @Test
    @DisplayName("searchByTagIds() returns empty when no item matches selected tags")
    void searchByTagIdsNoMatch() {
        assertTrue(new SearchService().searchByTagIds(Arrays.asList(2, 3)).isEmpty());
    }

    @Test
    @DisplayName("searchByCategoryAndTagIds() with null category and empty tags returns all items")
    void searchNullCategoryEmptyTags() {
        List<ClothingItem> results = new SearchService()
                .searchByCategoryAndTagIds(null, Collections.emptyList());
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("searchByCategoryAndTagIds() filters by category only")
    void searchByCategoryOnly() {
        List<ClothingItem> results = new SearchService()
                .searchByCategoryAndTagIds(ClothingCategory.TOP, Collections.emptyList());
        assertEquals(1, results.size());
        for (ClothingItem item : results) {
            assertEquals(ClothingCategory.TOP, item.getCategory());
        }
    }

    @Test
    @DisplayName("searchByCategoryAndTagIds() filters by tag only")
    void searchByTagOnly() {
        List<ClothingItem> results = new SearchService()
                .searchByCategoryAndTagIds(null, Collections.singletonList(2));
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("searchByCategoryAndTagIds() filters by both category and tag")
    void searchByCategoryAndTag() {
        List<ClothingItem> results = new SearchService()
                .searchByCategoryAndTagIds(ClothingCategory.TOP,
                        Collections.singletonList(2));
        assertEquals(1, results.size());
        assertEquals(ClothingCategory.TOP, results.get(0).getCategory());
    }

    @Test
    @DisplayName("searchByCategoryAndTagIds() returns empty when no items match")
    void searchByCategoryAndTagIdsNoMatch() {
        List<ClothingItem> results = new SearchService()
                .searchByCategoryAndTagIds(ClothingCategory.ONE_PIECE,
                        Collections.singletonList(1));
        assertTrue(results.isEmpty());
    }
}
