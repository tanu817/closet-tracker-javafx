package com.mycompany.irr00_group_project.service;

import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;

/**
 * Service for adding new clothing items to the closet.
 *
 * Responsible for:
 * 
 *   Generating a new unique item ID.
 *   Persisting the {@link ClothingItem} to {@code clothing_items.csv}.
 *   Creating new tags (if they do not already exist) and persisting them
 *       to {@code tags.csv}.
 *   Writing all tag relationships to {@code clothing_tags.csv} using
 *       Tag IDs: never tag names.
 * 
 *
 * No file I/O is performed here: all persistence is delegated to the
 * repository layer.
 *
 * @author Arshiya Shaik
 */
public class AddItemService {

    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;
    
    /**
     * Initialize.
     */
    public AddItemService() {
        this.clothingRepository = new ClothingRepository();
        this.tagRepository = new TagRepository();
    }

    /**
     * Creates a new clothing item and assigns the supplied tag IDs to it.
     *
     * <p>All tag IDs must already exist in {@code tags.csv}. For user-typed
     * tag names that may not exist yet, use
     * {@link #addItemWithTagNames(String, ClothingCategory, List, List)}
     * instead.</p>
     *
     * @param name     display name of the new item (must not be blank)
     * @param category clothing category
     * @param tagIds   list of existing tag IDs to assign (may be empty)
     * @return the newly created {@link ClothingItem}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    public ClothingItem addItem(String name, ClothingCategory category,
                                List<Integer> tagIds) {
        validateName(name);

        int newId = clothingRepository.generateNextId();
        ClothingItem item = new ClothingItem(newId, name, category);
        clothingRepository.addItem(item);

        if (tagIds != null) {
            for (int tagId : tagIds) {
                tagRepository.addTagToItem(newId, tagId);
            }
        }

        return item;
    }

    /**
     * Creates a new clothing item and resolves tag names to IDs automatically.
     *
     * <p>For each name in {@code existingTagNames}: the tag is looked up by
     * name; if found, its ID is used; if not found, the name is silently
     * ignored (use {@code newTagNames} for tags that should be created).</p>
     *
     * <p>For each name in {@code newTagNames}: if a tag with that name does
     * not yet exist, it is created with the given {@code newTagCategory};
     * then the ID is assigned to the item.</p>
     *
     * @param name             display name of the new item (must not be blank)
     * @param category         clothing category
     * @param existingTagNames tag names that must already exist in the DB
     * @param newTagNames      tag names to create on-the-fly if absent
     * @return the newly created {@link ClothingItem}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    public ClothingItem addItemWithTagNames(String name,
                                            ClothingCategory category,
                                            List<String> existingTagNames,
                                            List<String> newTagNames) {
        validateName(name);

        int newId = clothingRepository.generateNextId();
        ClothingItem item = new ClothingItem(newId, name, category);
        clothingRepository.addItem(item);

        // Resolve existing tag names → IDs
        if (existingTagNames != null) {
            for (String tagName : existingTagNames) {
                int tagId = tagRepository.getTagIdByName(tagName);
                if (tagId != -1) {
                    tagRepository.addTagToItem(newId, tagId);
                }
            }
        }

        // Create new tags if absent, then assign
        if (newTagNames != null) {
            for (String tagName : newTagNames) {
                if (tagName == null || tagName.isBlank()) {
                    continue;
                }
                int tagId = tagRepository.getTagIdByName(tagName);
                if (tagId == -1) {
                    tagId = createTag(tagName, TagCategory.OTHER);
                }
                tagRepository.addTagToItem(newId, tagId);
            }
        }

        return item;
    }

    /**
     * Creates a new tag in {@code tags.csv} and returns its generated ID.
     *
     * @param tagName  display name of the new tag (must not be blank)
     * @param category tag category
     * @return the ID assigned to the new tag
     * @throws IllegalArgumentException if {@code tagName} is blank
     */
    public int createTag(String tagName, TagCategory category) {
        if (tagName == null || tagName.isBlank()) {
            throw new IllegalArgumentException("Tag name must not be blank.");
        }
        int newTagId = tagRepository.generateNextId();
        Tag newTag = new Tag(newTagId, tagName, category);
        tagRepository.addTag(newTag);
        return newTagId;
    }

    /**
     * Assigns an already-existing tag (identified by ID) to an item.
     *
     * @param clothingId the target clothing item ID
     * @param tagId      the tag ID to assign
     */
    public void assignTagToItem(int clothingId, int tagId) {
        tagRepository.addTagToItem(clothingId, tagId);
    }

    /**
     * Returns all available tags for populating the Add Item UI dropdowns.
     *
     * @return list of all {@link Tag} objects
     */
    public java.util.List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name must not be blank.");
        }
    }
}