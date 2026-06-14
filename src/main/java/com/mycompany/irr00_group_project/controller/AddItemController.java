package com.mycompany.irr00_group_project.controller;

import java.util.List;

import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;
import com.mycompany.irr00_group_project.service.AddItemService;

/**
 * Controller for the Add Item overlay/page.
 *
 * <p>
 * Receives calls from {@code AddItemsPage} and forwards them to
 * {@link AddItemService}. Contains no business logic and performs no I/O.</p>
 *
 * @author Arshiya Shaik
 */
public class AddItemController {

    private final AddItemService addItemService;

    public AddItemController() {
        this.addItemService = new AddItemService();
    }

    /**
     * Adds a new clothing item with known tag IDs.
     *
     * <p>
     * Use this when every tag already exists in the database and you have its
     * ID (e.g. tags selected via toggle buttons loaded from the DB).</p>
     *
     * @param name display name (must not be blank)
     * @param category the clothing category
     * @param tagIds IDs of existing tags to assign (may be empty)
     * @return the persisted {@link ClothingItem}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    public ClothingItem addItem(String name,
            ClothingCategory category,
            List<Integer> tagIds) {
        return addItemService.addItem(name, category, tagIds);
    }

    /**
     * Adds a new clothing item where some tags are typed in by the user and may
     * not yet exist.
     *
     * <p>
     * Tags listed in {@code existingTagNames} are resolved by name. Tags listed
     * in {@code newTagNames} are created (with category {@code OTHER}) if not
     * already present, then assigned.</p>
     *
     * @param name display name (must not be blank)
     * @param category the clothing category
     * @param existingTagNames names of tags that must already be in the DB
     * @param newTagNames names of tags to create on-the-fly if absent
     * @return the persisted {@link ClothingItem}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    public ClothingItem addItemWithTagNames(String name,
            ClothingCategory category,
            List<String> existingTagNames,
            List<String> newTagNames) {
        return addItemService.addItemWithTagNames(
                name, category, existingTagNames, newTagNames);
    }

    /**
     * Returns all existing tags so the UI can display them as selectable chips.
     *
     * @return all {@link Tag} objects
     */
    public List<Tag> getAllTags() {
        return addItemService.getAllTags();
    }

    /**
     * Creates a standalone new tag (for use before calling addItem, if needed).
     *
     * @param tagName display name (must not be blank)
     * @param category tag category
     * @return the ID assigned to the new tag
     */
    public int createTag(String tagName, TagCategory category) {
        return addItemService.createTag(tagName, category);
    }

    /**
     * Assigns an existing tag to an existing item (post-creation update).
     *
     * @param clothingId the item's ID
     * @param tagId the tag's ID
     */
    public void assignTagToItem(int clothingId, int tagId) {
        addItemService.assignTagToItem(clothingId, tagId);
    }
}
