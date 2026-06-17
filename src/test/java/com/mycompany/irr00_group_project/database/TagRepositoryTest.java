package com.mycompany.irr00_group_project.database;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TagRepository}.
 *
 * @author Alexandra Varapai
 */
public class TagRepositoryTest extends CsvTestBase {

    @Test
    @DisplayName("tag added with addTag() appears in getAllTags()")
    void addTagAppearsinList() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        List<Tag> tags = repo.getAllTags();
        assertEquals(1, tags.size());
        assertEquals("Blue", tags.get(0).getName());
    }

    @Test
    @DisplayName("2 tags added with addTag() appear in getAllTags()")
    void addMultipleTagsAppearinList() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTag(new Tag(2, "Pink", TagCategory.COLOR));
        List<Tag> tags = repo.getAllTags();
        assertEquals(2, tags.size());
        assertEquals("Blue", tags.get(0).getName());
        assertEquals("Pink", tags.get(1).getName());
    }

    @Test
    @DisplayName("getAllTags() is empty if no tags were added")
    void getAllTagsIsEmpty() {
        TagRepository repo = new TagRepository();
        List<Tag> tags = repo.getAllTags();
        assertTrue(tags.isEmpty());
    }

    @Test
    @DisplayName("generateNextId() returns next available tag ID (max existing ID + 1)")
    void generateNextIdIncrements() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Shiny", TagCategory.OTHER));
        repo.addTag(new Tag(2, "Blue", TagCategory.COLOR));
        assertEquals(3, repo.generateNextId());
    }

    @Test
    @DisplayName("generateNextId() returns 1 when repository is empty")
    void generateNextIdEmpty() {
        TagRepository repo = new TagRepository();
        assertEquals(1, repo.generateNextId());
    }

    @Test
    @DisplayName("getTagById() returns correct tag by its ID")
    void getTagByIdCorrect() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(5, "Casual", TagCategory.EVENT));
        Tag found = repo.getTagById(5);
        assertNotNull(found);
        assertEquals("Casual", found.getName());
    }

    @Test
    @DisplayName("getTagById() returns null for unknown ID")
    void getTagByIdNotFound() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(5, "Casual", TagCategory.EVENT));
        assertNull(repo.getTagById(10));
    }

    @Test
    @DisplayName("getTagIdByName() finds a tag by its name")
    void getTagIdByNameCorrect() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(3, "Formal", TagCategory.EVENT));
        assertEquals(3, repo.getTagIdByName("Formal"));
    }

    @Test
    @DisplayName("getTagIdByName() finds tag case-insensitively")
    void getTagIdByNameCaseInsensitive() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(3, "Formal", TagCategory.EVENT));
        assertEquals(3, repo.getTagIdByName("formal"));
        assertEquals(3, repo.getTagIdByName("FORMAL"));
    }

    @Test
    @DisplayName("getTagIdByName() returns -1 when no matching tags exist")
    void getTagIdByNameMissing() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(3, "Formal", TagCategory.EVENT));
        assertEquals(-1, repo.getTagIdByName("Casual"));
    }

    @Test
    @DisplayName("updateTag() updates existing tag's details (identified by ID)")
    void updateTagCorrect() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(3, "Formal", TagCategory.EVENT));
        repo.updateTag(new Tag(3, "Pink", TagCategory.COLOR));
        Tag found = repo.getTagById(3);
        assertEquals("Pink", found.getName());
        assertEquals(TagCategory.COLOR, found.getCategory());
    }

    @Test
    @DisplayName("updateTag() does not update tag's details with unknown ID")
    void updateTagUnknownID() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(3, "Formal", TagCategory.EVENT));
        repo.updateTag(new Tag(2, "Pink", TagCategory.COLOR));
        Tag found = repo.getTagById(3);
        assertEquals("Formal", found.getName());
        assertEquals(TagCategory.EVENT, found.getCategory());
    }

    @Test
    @DisplayName("deleteTag() removes tag so getAlltags() is empty")
    void deleteTagCorrect() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.deleteTag(1);
        assertTrue(repo.getAllTags().isEmpty());
    }

    @Test
    @DisplayName("deleteTag() removes tag and its clothing relationships")
    void deleteTagAndRemoveRelationships() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTagToItem(10, 1);
        repo.deleteTag(1);
        assertTrue(repo.getAllTags().isEmpty());
        assertTrue(repo.getTagIdsOfItem(10).isEmpty());
    }

    @Test
    @DisplayName("addTagToItem() prevents duplicate relationships")
    void addTagToItemNoDuplicate() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTagToItem(10, 1);
        repo.addTagToItem(10, 1);
        assertEquals(1, repo.getTagIdsOfItem(10).size());
    }

    @Test
    @DisplayName("removeTagFromItem() removes only the specified relationship")
    void removeTagFromItemOnlySrelationship() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTag(new Tag(2, "Red", TagCategory.COLOR));
        repo.addTagToItem(10, 1);
        repo.addTagToItem(10, 2);
        repo.removeTagFromItem(10, 1);
        List<Integer> remaining = repo.getTagIdsOfItem(10);
        assertFalse(remaining.contains(1));
        assertTrue(remaining.contains(2));
    }

    @Test
    @DisplayName("getTagIdsOfItem() returns tag IDs for a clothing item")
    void getTagIdsOfItemCorrect() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTag(new Tag(2, "Casual", TagCategory.EVENT));
        repo.addTagToItem(5, 1);
        repo.addTagToItem(5, 2);
        List<Integer> ids = repo.getTagIdsOfItem(5);
        assertEquals(2, ids.size());
        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
    }

    @Test
    @DisplayName("getTagIdsOfItem() returns empty list when item has no tags")
    void getTagIdsOfItemEmpty() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTagToItem(5, 1);
        List<Integer> ids = repo.getTagIdsOfItem(10);
        assertTrue(ids.isEmpty());
    }

    @Test
    @DisplayName("getTagIdsOfItem() returns only IDs of tags for the specified item")
    void getTagIdsOfItemDoesNotReturnOtherItemIds() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTag(new Tag(2, "Red", TagCategory.COLOR));
        repo.addTagToItem(5, 1);
        repo.addTagToItem(6, 2);
        List<Integer> ids = repo.getTagIdsOfItem(5);
        assertTrue(ids.contains(1));
        assertFalse(ids.contains(2));
    }

    @Test
    @DisplayName("getTagsOfItem() returns the item's tags")
    void getTagsOfItemCorrect() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(5, "Summer", TagCategory.WEATHER));
        repo.addTagToItem(7, 5);
        List<Tag> tags = repo.getTagsOfItem(7);
        assertEquals(1, tags.size());
        assertEquals("Summer", tags.get(0).getName());
    }

    @Test
    @DisplayName("getTagsOfItem() returns empty list when item has no tags")
    void getTagsOfItemEmpty() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(5, "Summer", TagCategory.WEATHER));
        repo.addTagToItem(7, 5);
        List<Tag> tags = repo.getTagsOfItem(3);
        assertTrue(tags.isEmpty());
    }

    @Test
    @DisplayName("getTagsOfItem() silently skips tag IDs that no longer exist in tags.csv")
    void getTagsOfItemSkipsOrphanedTags() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Blue", TagCategory.COLOR));
        repo.addTagToItem(5, 1);
        repo.addTagToItem(5, 2);
        List<Tag> tags = repo.getTagsOfItem(5);
        assertEquals(1, tags.size());
        assertEquals("Blue", tags.get(0).getName());
    }

}
