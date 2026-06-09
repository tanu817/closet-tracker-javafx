package com.mycompany.irr00_group_project.service;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;

/**
 * Service for searching clothing items by tags.
 *
 * All search operations use Tag IDs as the stable key: never tag names.
 * AND semantics: every supplied tag ID must be present on a result item.
 *
 * @author Arshiya Shaik
 */
public class SearchService {

    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;

    public SearchService() {
        this.clothingRepository = new ClothingRepository();
        this.tagRepository = new TagRepository();
    }

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
}