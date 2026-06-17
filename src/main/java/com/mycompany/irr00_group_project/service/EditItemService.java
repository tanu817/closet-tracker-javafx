package com.mycompany.irr00_group_project.service;

import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;

/**
 * Service for editing existing clothing items.
 *
 * <p>
 * Allows updating the item name and its tag assignments while leaving all other
 * fields (ID, wornCount, favorite, category) untouched.</p>
 *
 * @author Arshiya Shaik
 */
public class EditItemService {

    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;

    /**
     * Initialize.
     */
    public EditItemService() {
        this.clothingRepository = new ClothingRepository();
        this.tagRepository = new TagRepository();
    }

    /**
     * Updates the name of a clothing item and replaces its tag set.
     *
     * @param item the item to edit (mutated in-place)
     * @param newName the new display name (must not be blank)
     * @param tagNames the desired set of tag names after the edit
     * @throws IllegalArgumentException if {@code newName} is blank
     */
    public void updateItem(ClothingItem item, String newName, List<String> tagNames) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Item name must not be blank.");
        }

        // Update name and persist
        item.rename(newName.trim());
        clothingRepository.changeItem(item);

        // Remove all existing tag relationships from CSV
        List<Integer> existingTagIds = tagRepository.getTagIdsOfItem(item.getId());
        for (int tagId : existingTagIds) {
            tagRepository.removeTagFromItem(item.getId(), tagId);
        }
        // Clear in-memory tags
        for (Tag t : new java.util.ArrayList<>(item.getTags())) {
            item.removeTag(t);
        }

        // Re-add desired tags
        if (tagNames != null) {
            for (String name : tagNames) {
                if (name == null || name.isBlank()) {
                    continue;
                }
                int tagId = tagRepository.getTagIdByName(name.trim());
                if (tagId == -1) {
                    tagId = tagRepository.generateNextId();
                    Tag newTag = new Tag(tagId, name.trim(), TagCategory.OTHER);
                    tagRepository.addTag(newTag);
                }
                tagRepository.addTagToItem(item.getId(), tagId);
                Tag tag = tagRepository.getTagById(tagId);
                if (tag != null) {
                    item.addTag(tag);
                }
            }
        }
    }

    /**
     * Returns all available tags for populating the edit UI.
     *
     * @return list of all {@link Tag} objects
     */
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }
}
