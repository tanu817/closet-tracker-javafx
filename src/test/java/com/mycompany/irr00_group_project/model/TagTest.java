package com.mycompany.irr00_group_project.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Tag}.
 *
 * @author  Alexandra Varapai
 */
public class TagTest {

    @Test
    @DisplayName("Three-arg constructor sets all fields")
    void fullConstructor() {
        Tag tag = new Tag(1, "Blue", TagCategory.COLOR);
        assertEquals(1, tag.getId());
        assertEquals("Blue", tag.getName());
        assertEquals(TagCategory.COLOR, tag.getCategory());
    }

    @Test
    @DisplayName("Two-arg constructor defaults category to OTHER")
    void defaultCategoryIsOther() {
        Tag tag = new Tag(2, "Casual");
        assertEquals(TagCategory.OTHER, tag.getCategory());
    }

    @Test
    @DisplayName("setName() updates the name")
    void setName() {
        Tag tag = new Tag(1, "Old", TagCategory.COLOR);
        tag.setName("New");
        assertEquals("New", tag.getName());
    }

    @Test
    @DisplayName("setCategory() updates the category")
    void setCategory() {
        Tag tag = new Tag(1, "Summer", TagCategory.OTHER);
        tag.setCategory(TagCategory.WEATHER);
        assertEquals(TagCategory.WEATHER, tag.getCategory());
    }

    @Test
    @DisplayName("equals() is based on ID only")
    void equalsById() {
        Tag t1 = new Tag(5, "Blue", TagCategory.COLOR);
        Tag t2 = new Tag(5, "DifferentName", TagCategory.WEATHER);
        assertEquals(t1, t2);
    }

    @Test
    @DisplayName("equals() returns false for different IDs")
    void notEqualsDifferentId() {
        Tag t1 = new Tag(5, "Blue", TagCategory.COLOR);
        Tag t2 = new Tag(6, "Blue", TagCategory.COLOR);
        assertNotEquals(t1, t2);
    }

    @Test
    @DisplayName("hashCode() is consistent with equals()")
    void hashCodeConsistency() {
        Tag t1 = new Tag(5, "Blue", TagCategory.COLOR);
        Tag t2 = new Tag(5, "Other", TagCategory.WEATHER);
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

