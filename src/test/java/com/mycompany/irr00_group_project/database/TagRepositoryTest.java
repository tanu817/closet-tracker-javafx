package com.mycompany.irr00_group_project.database;

import com.mycompany.irr00_group_project.CsvTestBase;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Tag Repository
 *
 * @author Alexandra Varapai
 */
public class TagRepositoryTest extends CsvTestBase {

   

    // ==================
    // Tests
    // ==================
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
    @DisplayName("generateNextId() returns 1 when repository is empty")
    void generateNextIdEmpty() {
        TagRepository repo = new TagRepository();
        assertEquals(1, repo.generateNextId());
    }

    @Test
    @DisplayName("generateNextId() returns max ID + 1")
    void generateNextIdIncrements() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(10, "A", TagCategory.OTHER));
        assertEquals(11, repo.generateNextId());
    }

    @Test
    @DisplayName("getTagById() returns correct tag")
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
        assertNull(repo.getTagById(999));
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
        assertEquals(-1, repo.getTagIdByName("DoesNotExist"));
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
    @DisplayName("updateTag() persists changes to name and category")
    void updateTagPersistsChanges() {
        TagRepository repo = new TagRepository();
        repo.addTag(new Tag(1, "Old", TagCategory.OTHER));
        repo.updateTag(new Tag(1, "New", TagCategory.EVENT));
        Tag found = repo.getTagById(1);
        assertEquals("New", found.getName());
        assertEquals(TagCategory.EVENT, found.getCategory());
    }

    
}
