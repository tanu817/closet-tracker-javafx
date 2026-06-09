package com.mycompany.irr00_group_project.controller;

import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.service.RecommendationService;

/**
 * Controller for the item detail / view-items actions.
 *
 * <p>Handles: marking an item as worn, toggling favourite status,
 * and fetching matching items.</p>
 *
 * @author Arshiya Shaik
 */
public class ViewItemController {

    private final ClothingRepository clothingRepository;
    private final RecommendationService recommendationService;

    public ViewItemController() {
        this.clothingRepository = new ClothingRepository();
        this.recommendationService = new RecommendationService();
    }

    /**
     * Increments the worn count of an item and persists the change.
     *
     * @param itemId the ID of the item that was worn
     */
    public void markAsWorn(int itemId) {
        clothingRepository.markAsWorn(itemId);
    }

    /**
     * Toggles the favourite flag on an item and persists the change.
     *
     * @param item the item whose favourite status should be toggled
     */
    public void toggleFavourite(ClothingItem item) {
        item.toggleFavorite();
        clothingRepository.changeItem(item);
    }

    /**
     * Deletes a clothing item by ID and removes all its CSV relationships.
     *
     * @param itemId the ID of the item to delete
     */
    public void deleteItem(int itemId) {
        clothingRepository.deleteItem(itemId);
    }

    /**
     * Returns matching items for the given clothing item.
     *
     * @param item the item to find matches for
     * @return list of matching {@link ClothingItem} objects; never {@code null}
     */
    public List<ClothingItem> getMatchingItems(ClothingItem item) {
        return recommendationService.getMatchingItems(item);
    }
}
