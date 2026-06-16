package com.mycompany.irr00_group_project.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;

/**
 * Unit tests for tag management in {@link AddItemService}
 *
 * @author Alexandra Varapai
 */
public class TagServiceTest extends CsvTestBase {

    @Test
    @DisplayName("createTag() persists a new tag and returns its ID")
    void createTagPersistsTag() {
        AddItemService service = new AddItemService();
        int tagId = service.createTag("Sunny", TagCategory.WEATHER);
        assertTrue(tagId > 0);
        List<Tag> tags = service.getAllTags();
        assertEquals(1, tags.size());
        assertEquals("Sunny", tags.get(0).getName());
    }

    @Test
    @DisplayName("createTag() with blank name throws IllegalArgumentException")
    void createTagBlankNameThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new AddItemService().createTag("  ", TagCategory.OTHER));
    }

    @Test
    @DisplayName("addItem() with blank name throws IllegalArgumentException")
    void addItemBlankNameThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new AddItemService().addItem(
                        "", ClothingCategory.TOP, Collections.emptyList()));
    }

    @Test
    @DisplayName("addItem() creates item and assigns existing tag IDs")
    void addItemWithTagIds() {
        AddItemService service = new AddItemService();
        int tagId = service.createTag("Party", TagCategory.EVENT);
        ClothingItem item = service.addItem(
                "Blazer", ClothingCategory.TOP, Collections.singletonList(tagId));
        assertNotNull(item);
        assertEquals("Blazer", item.getName());
    }

    @Test
    @DisplayName("addItemWithTagNames() creates new tags for new item")
    void addItemWithNewTagNames() {
        AddItemService service = new AddItemService();
        service.addItemWithTagNames("Summer Dress", ClothingCategory.ONE_PIECE,
                Collections.emptyList(), Arrays.asList("Floral", "Lightweight"));
        long count = service.getAllTags().stream()
                .filter(t -> t.getName().equals("Floral")
                || t.getName().equals("Lightweight"))
                .count();
        assertEquals(2, count);
    }

    @Test
    @DisplayName("addItemWithTagNames() reuses existing tag and does not duplicate it")
    void addItemWithExistingTagName() {
        AddItemService service = new AddItemService();
        service.createTag("Vintage", TagCategory.DETAIL);
        service.addItemWithTagNames("Coat", ClothingCategory.TOP,
                Collections.singletonList("Vintage"), Collections.emptyList());
        long vintageCount = service.getAllTags().stream()
                .filter(t -> t.getName().equalsIgnoreCase("Vintage")).count();
        assertEquals(1, vintageCount, "Existing tag must not be duplicated");
    }

    @Test
    @DisplayName("addItemWithTagNames() with blank name throws IllegalArgumentException")
    void addItemWithTagNamesBlankNameThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AddItemService().addItemWithTagNames(
                        "",
                        ClothingCategory.TOP,
                        Collections.emptyList(),
                        Collections.emptyList()
                )
        );
    }

    @Test
    @DisplayName("addItemWithTagNames() with whitespace-only name throws IllegalArgumentException")
    void addItemWithTagNameswhiteSpaceOnlyNameThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AddItemService().addItemWithTagNames(
                        " ",
                        ClothingCategory.TOP,
                        Collections.emptyList(),
                        Collections.emptyList()
                )
        );
    }

    @Test
    @DisplayName("getAllTags() returns empty list when no tags exist")
    void getAllTagsEmpty() {
        assertTrue(new AddItemService().getAllTags().isEmpty());
    }

    @Test
    @DisplayName("assignTagToItem() assigns an already-existing tag (identified by ID) to an item")
    void assignTagToItem() {
        AddItemService service = new AddItemService();
        int tagId = service.createTag("Formal", TagCategory.EVENT);
        ClothingItem item = service.addItem(
                "Suit", ClothingCategory.TOP, Collections.emptyList());
        service.assignTagToItem(item.getId(), tagId);
    }
}
