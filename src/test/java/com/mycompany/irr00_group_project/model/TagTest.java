package com.mycompany.irr00_group_project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Tag}.
 *
 * @author Alexandra Varapai
 */
public class TagTest {

    @Test
    @DisplayName("Constructor sets all fields")
    void fullConstructor() {
        Tag tag = new Tag(1, "Blue", TagCategory.COLOR);
        assertEquals(1, tag.getId());
        assertEquals("Blue", tag.getName());
        assertEquals(TagCategory.COLOR, tag.getCategory());
    }

    @Test
    @DisplayName("Constructor defaults category of a new tag to OTHER")
    void defaultCategoryIsOther() {
        Tag tag = new Tag(2, "Casual");
        assertEquals(TagCategory.OTHER, tag.getCategory());
    }

    @Test
    @DisplayName("setName() updates the name")
    void setName() {
        Tag tag = new Tag(1, "Blue", TagCategory.COLOR);
        tag.setName("Pink");
        assertEquals("Pink", tag.getName());
    }

    @Test
    @DisplayName("setCategory() updates the category")
    void setCategory() {
        Tag tag = new Tag(1, "Summer", TagCategory.OTHER);
        tag.setCategory(TagCategory.WEATHER);
        assertEquals(TagCategory.WEATHER, tag.getCategory());
    }

    @Test
    @DisplayName("equals() make two tags equal when they share the same ID")
    void equalsById() {
        Tag t1 = new Tag(5, "Blue", TagCategory.COLOR);
        Tag t2 = new Tag(5, "Pink", TagCategory.COLOR);
        assertEquals(t1, t2);
    }

    @Test
    @DisplayName("equals() returns false for different IDs")
    void notEqualsDifferentId() {
        Tag t1 = new Tag(5, "Blue", TagCategory.COLOR);
        Tag t2 = new Tag(6, "Pink", TagCategory.COLOR);
        assertNotEquals(t1, t2);
    }

    @Test
    @DisplayName("hashCode() is consistent with equals()")
    void hashCodeConsistency() {
        Tag t1 = new Tag(5, "Blue", TagCategory.COLOR);
        Tag t2 = new Tag(5, "Sunny", TagCategory.WEATHER);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("equals() returns false for non-Tag object")
    void equalsNonTag() {
        Tag tag = new Tag(1, "Blue", TagCategory.COLOR);
        assertNotEquals(tag, "Blue");
        assertNotEquals(tag, null);
    }

    @Test
    @DisplayName("toString() contains id and name")
    void toStringContents() {
        Tag tag = new Tag(7, "Party", TagCategory.EVENT);
        String s = tag.toString();
        assertTrue(s.contains("7"));
        assertTrue(s.contains("Party"));
    }
}
