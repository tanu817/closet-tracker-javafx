package com.mycompany.irr00_group_project.integration;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.service.AddItemService;
import com.mycompany.irr00_group_project.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Search Outfit flow
 *
 * @author Alexandra Varapai
 */
public class SearchFlowIntegrationTest extends CsvTestBase {

    @Test
    @DisplayName("Items added with tags appear in search results for those tags")
    void itemsWithTagsAreSearchable() {
        AddItemService addService = new AddItemService();
        addService.addItemWithTagNames("Shirt", ClothingCategory.TOP,
                Collections.emptyList(), Collections.singletonList("Casual"));
        addService.addItemWithTagNames("Blazer", ClothingCategory.TOP,
                Collections.emptyList(), Collections.singletonList("Formal"));
        addService.addItemWithTagNames("Jeans", ClothingCategory.BOTTOM,
                Collections.emptyList(), Arrays.asList("Casual", "Blue"));

        SearchService searchService = new SearchService();
        int casualId = searchService.getTagIdByName("Casual");
        assertNotEquals(-1, casualId);

        List<ClothingItem> results = searchService.searchByTagId(casualId);
        assertEquals(2, results.size(), "Shirt and Jeans both have Casual tag");
    }

    @Test
    @DisplayName("Search returns only items matching all selected tags")
    void SearchLogicMatchingAllTags() {
        AddItemService addService = new AddItemService();
        addService.addItemWithTagNames("Shirt", ClothingCategory.TOP,
                Collections.emptyList(), Arrays.asList("Casual", "Blue"));
        addService.addItemWithTagNames("Blouse", ClothingCategory.TOP,
                Collections.emptyList(), Arrays.asList("Casual", "Red"));

        SearchService searchService = new SearchService();
        int casualId = searchService.getTagIdByName("Casual");
        int blueId   = searchService.getTagIdByName("Blue");

        List<ClothingItem> results = searchService.searchByTagIds(Arrays.asList(casualId, blueId));
        assertEquals(1, results.size());
        assertEquals("Shirt", results.get(0).getName());
    }

    @Test
    @DisplayName("Search with no tags returns all added items")
    void SearchEmptyNoTags() {
        AddItemService addService = new AddItemService();
        addService.addItemWithTagNames("Shirt", ClothingCategory.TOP,
                Collections.emptyList(), Collections.singletonList("Blue"));
        addService.addItemWithTagNames("Jeans", ClothingCategory.BOTTOM,
                Collections.emptyList(), Collections.singletonList("White"));

        assertEquals(2, new SearchService().searchByTagIds(Collections.emptyList()).size());
    }
    
    
   
    @Test
    @DisplayName("Tag search returns at least 2 items to allow outfit creation")
    void searchedItemsCanFormOutfit() {
        AddItemService addService = new AddItemService();
        addService.addItemWithTagNames("T-Shirt", ClothingCategory.TOP,
                Collections.emptyList(), Collections.singletonList("Summer"));
        addService.addItemWithTagNames("Shorts", ClothingCategory.BOTTOM,
                Collections.emptyList(), Collections.singletonList("Summer"));

        SearchService searchService = new SearchService();
        int summerId = searchService.getTagIdByName("Summer");
        List<ClothingItem> results = searchService.searchByTagId(summerId);
        assertTrue(results.size() >= 2, "Need at least 2 items for an outfit");
    }
}
