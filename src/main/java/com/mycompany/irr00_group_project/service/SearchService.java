package com.mycompany.irr00_group_project.service;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;

/**
 * Service for searching clothing items by tags.
 *
 * <p>All search operations use Tag IDs as the stable key — never tag names.
 * AND semantics: every supplied tag ID must be present on a result item.</p>
 *
 * @author Arshiya Shaik
 * @author Aleksandra Levenkova
 */
public class SearchService {

    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;

    /**
     * Initializes the service with new instances of
     * {@link ClothingRepository} and {@link TagRepository}.
     */
    public SearchService() {
        this.clothingRepository = new ClothingRepository();
        this.tagRepository = new TagRepository();
    }

    // ==================
    // Tag helpers for UI
    // ==================

    /**
     * Returns the display names of every known tag (for populating filter UI).
     *
     * @return ordered list of tag names
     */
    public List<String> getAllTagNames() {
        List<Tag> tags = tagRepository.getAllTags();
        List<String> names = new ArrayList<>();
        for (Tag t : tags) {
            names.add(t.getName());
        }
        return names;
    }

    /**
     * Returns all tags (objects) — useful for building tag-to-ID maps in the UI.
     *
     * @return all persisted {@link Tag} objects
     */
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    /**
     * Resolves a tag name to its ID (case-insensitive).
     *
     * @param name the display name of the tag
     * @return the tag ID, or {@code -1} if not found
     */
    public int getTagIdByName(String name) {
        return tagRepository.getTagIdByName(name);
    }

    // ==================
    // Search
    // ==================

    /**
     * Returns all clothing items that carry <em>every</em> tag in
     * {@code selectedTagIds} (AND logic).
     *
     * <p>An empty or null list returns all items.</p>
     *
     * @param selectedTagIds list of tag IDs that must all be present
     * @return matching {@link ClothingItem} objects; never {@code null}
     */
    public List<ClothingItem> searchByTagIds(List<Integer> selectedTagIds) {
        List<ClothingItem> allItems = clothingRepository.getAllItems();
        List<ClothingItem> results = new ArrayList<>();

        // Empty filter → return everything (with tags hydrated for display)
        if (selectedTagIds == null || selectedTagIds.isEmpty()) {
            for (ClothingItem item : allItems) {
                for (Tag tag : tagRepository.getTagsOfItem(item.getId())) {
                    item.addTag(tag);
                }
            }
            return allItems;
        }

        for (ClothingItem item : allItems) {
            List<Integer> itemTagIds = tagRepository.getTagIdsOfItem(item.getId());

            boolean matchesAll = true;
            for (Integer requiredId : selectedTagIds) {
                if (!itemTagIds.contains(requiredId)) {
                    matchesAll = false;
                    break;
                }
            }

            if (matchesAll) {
                // Hydrate tags onto the in-memory item for display
                for (Tag tag : tagRepository.getTagsOfItem(item.getId())) {
                    item.addTag(tag);
                }
                results.add(item);
            }
        }

        return results;
    }

    /**
     * Convenience overload: search by a single tag ID.
     *
     * @param tagId the single tag ID that must be present
     * @return matching {@link ClothingItem} objects; never {@code null}
     */
    public List<ClothingItem> searchByTagId(int tagId) {
        List<Integer> ids = new ArrayList<>();
        ids.add(tagId);
        return searchByTagIds(ids);
    }

    /**
     * Returns all clothing items whose {@link ClothingCategory} matches
     * {@code category} AND that carry every tag in {@code selectedTagIds}.
     *
     * <p>If {@code category} is {@code null} the category filter is skipped.
     * If {@code selectedTagIds} is empty the tag filter is skipped.</p>
     *
     * @param category       the required clothing category, or {@code null} to skip
     * @param selectedTagIds tag IDs that must all be present, may be empty
     * @return matching {@link ClothingItem} objects; never {@code null}
     */
    public List<ClothingItem> searchByCategoryAndTagIds(
            ClothingCategory category, List<Integer> selectedTagIds) {

        List<ClothingItem> allItems = clothingRepository.getAllItems();
        
        if (hasNoFilters(category, selectedTagIds)) {
            hydrateTags(allItems);
            return allItems;
        }
        
        List<ClothingItem> results = new ArrayList<>();

        for (ClothingItem item : allItems) {
            // Category filter
            if (!matchesCategory(item, category)) {
                continue;
            }

            // Tag filter (AND logic)
            if (!matchesTags(item, selectedTagIds)) {
                continue;
            }

            // Hydrate tags for display
            hydrateTags(item);
            results.add(item);
        }

        return results;
    }
    
    /**
     * Returns whether no category or tag filters were supplied.
     * @param category the selected clothing category
     * @param tagIds the selected tagIds
     * @return {@code true} if no filters were supplied;
     *         {@code false} otherwise
     */
    private boolean hasNoFilters(ClothingCategory category, List<Integer> tagIds) {
        return category == null && (tagIds == null || tagIds.isEmpty());
    }
    
    /**
     * Returns whether the item contains all selected tags.
     * @param item to check
     * @param selectedTagIds that are required
     * @return {@code true} if the item has all selected tags;
     *         {@code false} otherwise
     */
    private boolean matchesTags(ClothingItem item, List<Integer> selectedTagIds) {
        if (selectedTagIds == null || selectedTagIds.isEmpty()) {
            return true;
        }
        
        List<Integer> itemTagIds = tagRepository.getTagIdsOfItem(item.getId());
        
        for (Integer requiredId : selectedTagIds) {
            if (!itemTagIds.contains(requiredId)) {
                return false;
            }
        }
        
        return true;   
    }
    
    /**
     * Returns whether the item matches the requested category.
     * @param item the item to check
     * @param category the required category to check
     * @return {@code true} if the item matches the category;
     *         {@code false} otherwise
     */
    private boolean matchesCategory(ClothingItem item, ClothingCategory category) {
        return category == null || item.getCategory() == category;
    }
    
    /**
     * Adds the corresponding tags to each item in the list.
     * @param items the items to hydrate with their tags
     */
    private void hydrateTags(List<ClothingItem> items) {
        for (ClothingItem item : items) {
            hydrateTags(item);
        }
    }
    
    /**
     * Adds all tags associated with the item.
     * @param item the item to hydrate with its tags
     */
    private void hydrateTags(ClothingItem item) {
        for (Tag tag : tagRepository.getTagsOfItem(item.getId())) {
            item.addTag(tag);
        }
    }
}