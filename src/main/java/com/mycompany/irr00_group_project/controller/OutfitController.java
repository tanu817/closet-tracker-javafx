package com.mycompany.irr00_group_project.controller;

import java.util.List;

import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;
import com.mycompany.irr00_group_project.service.OutfitService;

/**
 * Controller for the Outfits page.
 *
 * <p>Receives calls from {@code OutfitsPage} and forwards them to
 * {@link OutfitService}. Contains no business logic and performs no I/O.</p>
 *
 * @author Arshiya Shaik
 */
public class OutfitController {

    private final OutfitService outfitService;

    public OutfitController() {
        this.outfitService = new OutfitService();
    }


    /**
     * Returns all outfits with their clothing items hydrated.
     *
     * @return list of fully loaded {@link Outfit} objects
     */
    public List<Outfit> getAllOutfits() {
        return outfitService.getAllOutfits();
    }

    /**
     * Returns all available clothing items (for the outfit builder UI).
     *
     * @return list of all {@link ClothingItem} objects with tags loaded
     */
    public List<ClothingItem> getAllClothingItems() {
        return outfitService.getAllClothingItems();
    }

    /**
     * Returns the two {@link ClothingItem} objects for an outfit.
     *
     * @param outfitId the outfit ID to look up
     * @return list of up to two items
     */
    public List<ClothingItem> getItemsForOutfit(int outfitId) {
        return outfitService.getItemsForOutfit(outfitId);
    }


    /**
     * Creates a new outfit from two clothing item IDs.
     *
     * @param outfitName  display name (may be blank for auto-generation)
     * @param clothingId1 ID of the first item
     * @param clothingId2 ID of the second item
     * @return the newly created {@link Outfit}
     * @throws IllegalArgumentException when either item ID does not exist
     */
    public Outfit createOutfit(String outfitName, int clothingId1, int clothingId2) {
        return outfitService.createOutfit(outfitName, clothingId1, clothingId2);
    }


    /**
     * Renames an outfit and persists the change.
     *
     * @param outfitId the ID of the outfit to rename
     * @param newName  the new display name
     */
    public void renameOutfit(int outfitId, String newName) {
        outfitService.renameOutfit(outfitId, newName);
    }

    /**
     * Deletes an outfit and its item relationships.
     *
     * @param outfitId the ID of the outfit to delete
     */
    public void deleteOutfit(int outfitId) {
        outfitService.deleteOutfit(outfitId);
    }


    /**
     * Records that an outfit was worn (increments wornCount on both items).
     *
     * @param outfitId the ID of the outfit that was worn
     */
    public void wearOutfit(int outfitId) {
        outfitService.wearOutfit(outfitId);
    }
}