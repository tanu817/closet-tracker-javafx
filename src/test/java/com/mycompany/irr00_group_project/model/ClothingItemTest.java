package com.mycompany.irr00_group_project.model;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ClothingItem}.
 *
 * @author Alexandra Varapai
 */
public class ClothingItemTest {

    @Test
    @DisplayName("New item starts with wornCount 0 and isFavorite false")
    void defaultState() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        assertEquals(0, item.getWornCount());
        assertFalse(item.isFavorite());
        assertEquals(1, item.getId());
        assertEquals("Shirt", item.getName());
        assertEquals(ClothingCategory.TOP, item.getCategory());
        assertTrue(item.getTags().isEmpty());
    }

    @Test
    @DisplayName("Full constructor sets all fields correctly")
    void fullConstructor() {
        ClothingItem item = new ClothingItem(5, "Jeans", true, ClothingCategory.BOTTOM, 7);
        assertEquals(5, item.getId());
        assertEquals("Jeans", item.getName());
        assertTrue(item.isFavorite());
        assertEquals(ClothingCategory.BOTTOM, item.getCategory());
        assertEquals(7, item.getWornCount());
    }

    @Test
    @DisplayName("wear() increments wornCount by 1 each call")
    void wearIncrementsCount() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        item.wear();
        assertEquals(1, item.getWornCount());
        item.wear();
        assertEquals(2, item.getWornCount());
    }

    @Test
    @DisplayName("toggleFavorite() flips isFavorite both ways")
    void toggleFavorite() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        assertFalse(item.isFavorite());
        item.toggleFavorite();
        assertTrue(item.isFavorite());
        item.toggleFavorite();
        assertFalse(item.isFavorite());
    }

    @Test
    @DisplayName("rename() updates the item name")
    void rename() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        item.rename("Blouse");
        assertEquals("Blouse", item.getName());
    }

    @Test
    @DisplayName("changeCategory() updates the category")
    void changeCategory() {
        ClothingItem item = new ClothingItem(1, "Pants", ClothingCategory.TOP);
        item.changeCategory(ClothingCategory.BOTTOM);
        assertEquals(ClothingCategory.BOTTOM, item.getCategory());
    }

    @Test
    @DisplayName("addTag() associates tag, duplicate tag is ignored")
    void addTagDeduplicated() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        Tag tag = new Tag(10, "Blue", TagCategory.COLOR);
        item.addTag(tag);
        item.addTag(tag);
        assertEquals(1, item.getTags().size());
        assertTrue(item.hasTag(10));
    }

    @Test
    @DisplayName("removeTag() disassociates the given tag")
    void removeTag() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        Tag tag = new Tag(10, "Blue", TagCategory.COLOR);
        item.addTag(tag);
        item.removeTag(tag);
        assertFalse(item.hasTag(10));
        assertTrue(item.getTags().isEmpty());
    }

    @Test
    @DisplayName("removeTagById() removes only the tag with matching ID")
    void removeTagById() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        item.addTag(new Tag(10, "Blue", TagCategory.COLOR));
        item.addTag(new Tag(11, "Red", TagCategory.COLOR));
        item.removeTagById(10);
        assertFalse(item.hasTag(10));
        assertTrue(item.hasTag(11));
    }

    @Test
    @DisplayName("getTags() returns an unmodifiable view of this item's tags")
    void getTagsIsLiveView() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        Tag tag = new Tag(10, "Blue", TagCategory.COLOR);
        Set<Tag> tags = item.getTags();
        assertTrue(tags.isEmpty());
        item.addTag(tag);
        assertTrue(tags.contains(tag));
        item.removeTag(tag);
        assertFalse(tags.contains(tag));
    }

    @Test
    @DisplayName("hasTag() returns false when tag is not present")
    void hasTagMissing() {
        ClothingItem item = new ClothingItem(1, "Shirt", false, ClothingCategory.TOP, 0);
        assertFalse(item.hasTag(10));
    }

    @Test
    @DisplayName("toString() contains id and name")
    void toStringContainsIdAndName() {
        ClothingItem item = new ClothingItem(3, "Hoodie", true, ClothingCategory.TOP, 1);
        String s = item.toString();
        assertTrue(s.contains("3"));
        assertTrue(s.contains("Hoodie"));
    }
}
