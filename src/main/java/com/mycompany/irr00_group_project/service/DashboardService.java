package com.mycompany.irr00_group_project.service;

import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;

/**
 * Service for computing dashboard statistics.
 *
 * All heavy lifting (sorting, filtering) is delegated to
 * {@link ClothingRepository}. This service provides the clean API the
 * controller expects and adds any cross-repository aggregations that a single
 * repository call cannot satisfy on its own.
 *
 * @author Arshiya Shaik
 */
public class DashboardService {

    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;

    public DashboardService() {
        this.clothingRepository = new ClothingRepository();
        this.tagRepository = new TagRepository();
    }

    /**
     * Returns the top 5 most-worn items, sorted by {@code wornCount}
     * descending.
     *
     * @return up to 5 {@link ClothingItem} objects
     */
    public List<ClothingItem> getTop5Worn() {
        return clothingRepository.getTop5Worn();
    }

    /**
     * Returns the top 5 favourite items (marked as favourite), sorted by
     * {@code wornCount} descending.
     *
     * @return up to 5 favourite {@link ClothingItem} objects
     */
    public List<ClothingItem> getTop5Favourites() {
        return clothingRepository.getTop5FavouriteItems();
    }

    /**
     * Returns the 5 least-worn items, sorted by {@code wornCount} ascending.
     * Useful for surfacing neglected pieces in the wardrobe.
     *
     * @return up to 5 least-worn {@link ClothingItem} objects
     */
    public List<ClothingItem> getLeast5Worn() {
        return clothingRepository.getLeast5Worn();
    }

    /**
     * Snapshot of dashboard statistics for the overview cards.
     */
    public static class DashboardStats {

        /**
         * Total number of clothing items in the closet.
         */
        public final int totalItems;
        /**
         * Total number of times any item has been worn.
         */
        public final int totalWears;
        /**
         * Total number of items marked as favourite.
         */
        public final int totalFavourites;
        /**
         * Average wears per item (0.0 when {@code totalItems} is 0).
         */
        public final double avgWearsPerItem;

        DashboardStats(int totalItems, int totalWears,
                int totalFavourites, double avgWearsPerItem) {
            this.totalItems = totalItems;
            this.totalWears = totalWears;
            this.totalFavourites = totalFavourites;
            this.avgWearsPerItem = avgWearsPerItem;
        }
    }

    /**
     * Computes summary statistics for the dashboard overview row.
     *
     * @return a populated {@link DashboardStats} object; never {@code null}
     */
    public DashboardStats getStats() {
        List<ClothingItem> all = clothingRepository.getAllItems();

        int totalItems = all.size();
        int totalWears = 0;
        int totalFavourites = 0;

        for (ClothingItem item : all) {
            totalWears += item.getWornCount();
            if (item.isFavorite()) {
                totalFavourites++;
            }
        }

        double avg = totalItems == 0 ? 0.0 : (double) totalWears / totalItems;

        return new DashboardStats(totalItems, totalWears, totalFavourites, avg);
    }

    /**
     * Marks an item as worn, incrementing its {@code wornCount} by 1 and
     * persisting the change.
     *
     * @param clothingId the ID of the item to mark worn
     */
    public void markItemWorn(int clothingId) {
        clothingRepository.markAsWorn(clothingId);
    }

    /**
     * Returns all items with their tags hydrated from the relationship table.
     * Useful for pages that need to display tag chips per item.
     *
     * @return all {@link ClothingItem} objects with tags loaded
     */
    public List<ClothingItem> getAllItemsWithTags() {
        List<ClothingItem> all = clothingRepository.getAllItems();
        for (ClothingItem item : all) {
            tagRepository.getTagsOfItem(item.getId())
                    .forEach(item::addTag);
        }
        return all;
    }
}
