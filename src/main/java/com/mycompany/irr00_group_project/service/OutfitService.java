package com.mycompany.irr00_group_project.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.OutfitRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Outfit;

/**
 * Service for managing outfits.
 *
 * An outfit groups exactly two clothing items (one top, one bottom, as enforced
 * by {@link OutfitRepository#addItemToOutfit}). This service handles creation,
 * retrieval, deletion, and wear-tracking while keeping the controller free of
 * any repository calls.
 *
 * @author Arshiya Shaik
 */
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;

    public OutfitService() {
        this.outfitRepository = new OutfitRepository();
        this.clothingRepository = new ClothingRepository();
        this.tagRepository = new TagRepository();
    }

    /**
     * Creates a new outfit from two clothing item IDs and persists it.
     *
     * <p>
     * The outfit name is auto-generated from the item names if
     * {@code outfitName} is blank.</p>
     *
     * @param outfitName display name (may be blank for auto-generation)
     * @param clothingId1 ID of the first item (e.g. top)
     * @param clothingId2 ID of the second item (e.g. bottom)
     * @return the newly created {@link Outfit}
     * @throws IllegalArgumentException when either item ID does not exist
     */
    public Outfit createOutfit(String outfitName, int clothingId1, int clothingId2) {
        ClothingItem item1 = clothingRepository.getItemById(clothingId1);
        ClothingItem item2 = clothingRepository.getItemById(clothingId2);

        if (item1 == null) {
            throw new IllegalArgumentException(
                    "Clothing item not found: id=" + clothingId1);
        }
        if (item2 == null) {
            throw new IllegalArgumentException(
                    "Clothing item not found: id=" + clothingId2);
        }

        if (outfitRepository.outfitExists(clothingId1, clothingId2)) {
            throw new IllegalStateException("This outfit already exists.");
        }

        int newId = outfitRepository.generateNextId();

        // Build name from items if caller did not supply one
        String name = (outfitName != null && !outfitName.isBlank())
                ? outfitName
                : item1.getName() + " + " + item2.getName();

        Outfit outfit = new Outfit(newId, name);
        outfitRepository.addOutfit(outfit);
        outfitRepository.addItemToOutfit(newId, clothingId1, clothingId2);

        return outfit;
    }

    /**
     * Returns all outfits with their clothing items hydrated.
     *
     * @return list of fully loaded {@link Outfit} objects
     */
    public List<Outfit> getAllOutfits() {
        List<Outfit> outfits = outfitRepository.getAllOutfits();
        for (Outfit outfit : outfits) {
            hydrateOutfit(outfit);
        }
        return outfits;
    }

    /**
     * Returns a single outfit by ID with its items hydrated.
     *
     * @param outfitId the outfit ID to look up
     * @return the matching {@link Outfit}, or {@code null} if not found
     */
    public Outfit getOutfitById(int outfitId) {
        for (Outfit outfit : outfitRepository.getAllOutfits()) {
            if (outfit.getId() == outfitId) {
                hydrateOutfit(outfit);
                return outfit;
            }
        }
        return null;
    }

    /**
     * Returns the two {@link ClothingItem} objects that make up the outfit.
     *
     * @param outfitId the outfit whose items should be returned
     * @return list of (up to) two items; empty if not found
     */
    public List<ClothingItem> getItemsForOutfit(int outfitId) {
        List<Integer> itemIds = outfitRepository.getItemIdsForOutfit(outfitId);
        List<ClothingItem> items = new ArrayList<>();
        for (int id : itemIds) {
            ClothingItem item = clothingRepository.getItemById(id);
            if (item != null) {
                tagRepository.getTagsOfItem(id).forEach(item::addTag);
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Renames an outfit and persists the change.
     *
     * @param outfitId the ID of the outfit to rename
     * @param newName the new display name
     */
    public void renameOutfit(int outfitId, String newName) {
        Outfit outfit = getOutfitById(outfitId);
        if (outfit != null) {
            outfit.rename(newName);
            outfitRepository.updateOutfit(outfit);
        }
    }

    /**
     * Deletes an outfit and its item relationships.
     *
     * @param outfitId the ID of the outfit to delete
     */
    public void deleteOutfit(int outfitId) {
        outfitRepository.deleteOutfit(outfitId);
    }

    /**
     * Records that an outfit was worn: increments {@code wornCount} on both
     * component items and persists the changes.
     *
     * @param outfitId the ID of the outfit that was worn
     */
    public void wearOutfit(int outfitId) {
        List<Integer> itemIds = outfitRepository.getItemIdsForOutfit(outfitId);
        for (int id : itemIds) {
            clothingRepository.markAsWorn(id);
        }
    }

    /**
     * Loads the two clothing items for an outfit into its in-memory item set.
     *
     * @param outfit the outfit to hydrate
     */
    private void hydrateOutfit(Outfit outfit) {
        List<ClothingItem> items = getItemsForOutfit(outfit.getId());
        for (ClothingItem item : items) {
            outfit.addItem(item);
        }
    }

    /**
     * Deletes all outfits that contain the given clothing item. Called
     * automatically when a clothing item is deleted.
     *
     * @param clothingId the ID of the deleted clothing item
     */
    public void deleteOutfitsContainingItem(int clothingId) {
        List<Integer> outfitIds = outfitRepository.getOutfitIdsContainingItem(clothingId);
        for (int id : outfitIds) {
            outfitRepository.deleteOutfit(id);
        }
    }

    /**
     *
     * @return all {@link ClothingItem} objects with tags loaded
     */
    public List<ClothingItem> getAllClothingItems() {
        List<ClothingItem> all = clothingRepository.getAllItems();
        for (ClothingItem item : all) {
            tagRepository.getTagsOfItem(item.getId()).forEach(item::addTag);
        }
        return all;
    }
}
