package com.mycompany.irr00_group_project.controller;

import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.service.EditItemService;
import com.mycompany.irr00_group_project.service.OutfitService;
import com.mycompany.irr00_group_project.service.RecommendationService;

/**
 * Controller for the item detail / view-items actions.
 *
 * <p>
 * Handles: marking an item as worn, toggling favourite status, fetching
 * matching items, and creating outfits from a matched pair.</p>
 *
 * @author Arshiya Shaik
 */
public class ViewItemController {

    private final ClothingRepository clothingRepository;
    private final RecommendationService recommendationService;
    private final OutfitService outfitService;
    private final EditItemService editItemService;

    public ViewItemController() {
        this.clothingRepository = new ClothingRepository();
        this.recommendationService = new RecommendationService();
        this.outfitService = new OutfitService();
        this.editItemService = new EditItemService();
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
     * Deletes a clothing item by ID, removes all its CSV relationships, and
     * automatically deletes any outfits that contained this item.
     *
     * @param itemId the ID of the item to delete
     */
    public void deleteItem(int itemId) {
        outfitService.deleteOutfitsContainingItem(itemId);
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

    /**
     * Updates a clothing item's name and tag set, persisting all changes to
     * CSV.
     *
     * @param item the item to edit (mutated in-place)
     * @param newName the new display name
     * @param tagNames the desired set of tag names
     */
    public void editItem(ClothingItem item, String newName, List<String> tagNames) {
        editItemService.updateItem(item, newName, tagNames);
    }

    /**
     * Returns all available tags for the edit UI.
     *
     * @return list of all {@link Tag} objects
     */
    public List<Tag> getAllTags() {
        return editItemService.getAllTags();
    }

    /**
     * Creates an outfit from two clothing items and persists it to CSV.
     *
     * <p>
     * The outfit name is auto-generated as "item1Name + item2Name".</p>
     *
     * @param selectedItem the item the user is currently viewing
     * @param matchingItem the suggested matching item
     * @return the newly created {@link Outfit}
     */
    public Outfit createOutfitFromMatch(ClothingItem selectedItem, ClothingItem matchingItem) {
        return outfitService.createOutfit(
                selectedItem.getName() + " + " + matchingItem.getName(),
                selectedItem.getId(),
                matchingItem.getId());
    }
}
