package com.mycompany.irr00_group_project.controller;

import java.util.List;

import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.service.SearchService;

/**
 * Controller for the Search page.
 *
 * <p>Receives calls from {@code SearchPage} and forwards them to
 * {@link SearchService}. Contains no business logic and performs no I/O.</p>
 *
 * @author Arshiya Shaik
 */
public class SearchController {

    private final SearchService searchService;

    public SearchController() {
        this.searchService = new SearchService();
    }

    // ==================
    // Tag helpers
    // ==================

    /**
     * Returns the display names of all known tags.
     * Used to pre-populate the filter UI.
     *
     * @return list of tag name strings
     */
    public List<String> getAllTagNames() {
        return searchService.getAllTagNames();
    }

    /**
     * Returns all tags as objects so the UI can build a name→ID map.
     *
     * @return all {@link Tag} objects
     */
    public List<Tag> getAllTags() {
        return searchService.getAllTags();
    }

    /**
     * Resolves a tag name to its ID (case-insensitive).
     *
     * @param tagName the display name to resolve
     * @return the tag ID, or {@code -1} if not found
     */
    public int getTagIdByName(String tagName) {
        return searchService.getTagIdByName(tagName);
    }

    // ==================
    // Search
    // ==================

    /**
     * Executes an AND-logic tag search using Tag IDs.
     *
     * <p>Every ID in {@code selectedTagIds} must be present on a result item.
     * Passing an empty list returns all items.</p>
     *
     * @param selectedTagIds list of tag IDs that must all match
     * @return matching {@link ClothingItem} objects; never {@code null}
     */
    public List<ClothingItem> searchByTags(List<Integer> selectedTagIds) {
        return searchService.searchByTagIds(selectedTagIds);
    }
}