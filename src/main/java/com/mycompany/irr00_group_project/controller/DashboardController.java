package com.mycompany.irr00_group_project.controller;

import java.util.List;

import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.service.DashboardService;
import com.mycompany.irr00_group_project.service.DashboardService.DashboardStats;

/**
 * Controller for the Dashboard page.
 *
 * <p>
 * Receives calls from the dashboard UI and forwards them to
 * {@link DashboardService}. Contains no business logic and performs no I/O.</p>
 *
 * @author Arshiya Shaik
 * @author Alexandra Varapai
 */
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController() {
        this.dashboardService = new DashboardService();
    }

    /**
     * Returns the top 5 most-worn items.
     *
     * @return up to 5 {@link ClothingItem} objects sorted by wornCount desc
     */
    public List<ClothingItem> getTop5Worn() {
        return dashboardService.getTop5Worn();
    }

    /**
     * Returns the top 5 favourite items.
     *
     * @return up to 5 favourite {@link ClothingItem} objects sorted by
     * wornCount desc
     */
    public List<ClothingItem> getTop5Favourites() {
        return dashboardService.getTop5Favourites();
    }

    /**
     * Returns the 5 least-worn items (for the "neglected" wardrobe section).
     *
     * @return up to 5 {@link ClothingItem} objects sorted by wornCount asc
     */
    public List<ClothingItem> getLeast5Worn() {
        return dashboardService.getLeast5Worn();
    }

    /**
     * Returns aggregate statistics for the overview cards (total items, total
     * wears, favourites count, avg wears per item).
     *
     * @return a populated {@link DashboardStats} object
     */
    public DashboardStats getStats() {
        return dashboardService.getStats();
    }

    /**
     * Returns the wardrobe usage progress as a value between 0 and 1.
     *
     * @return progress value between 0.0 and 1.0
     */
    public double getWardrobeUsageProgress() {
        DashboardService.DashboardStats stats = dashboardService.getStats();
        if (stats.totalItems == 0) {
            return 0.0;
        }
        return (double) dashboardService.getItemsWornMoreThanOnce() / stats.totalItems;
    }

    public int getItemsWornMoreThanOnce() {
        return dashboardService.getItemsWornMoreThanOnce();
    }

    public int getTotalItems() {
        return dashboardService.getStats().totalItems;
    }

    /**
     * Records that a clothing item was worn and persists the updated count.
     *
     * @param clothingId the ID of the item that was worn
     */
    public void markItemWorn(int clothingId) {
        dashboardService.markItemWorn(clothingId);
    }

    /**
     * Returns all clothing items with their tags hydrated. Useful for
     * full-closet views within the dashboard.
     *
     * @return all items with tags populated
     */
    public List<ClothingItem> getAllItemsWithTags() {
        return dashboardService.getAllItemsWithTags();
    }
}
