package com.mycompany.irr00_group_project.service;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;

/**
 * Service for generating clothing item matching recommendations.
 *
 * <p>Matching logic: if the selected item is a TOP, return all BOTTOMs;
 * if it is a BOTTOM, return all TOPs. All other categories return an
 * empty list (accessories, one-piece, headwear, footwear are not matched).</p>
 *
 * @author Arshiya Shaik
 */
public class RecommendationService {

    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;

    /**
     * Initializes the service with new instances of
     * {@link ClothingRepository} and {@link TagRepository}.
     */
    public RecommendationService() {
        this.clothingRepository = new ClothingRepository();
        this.tagRepository = new TagRepository();
    }

    /**
     * Returns all items that match the given item based on category.
     *
     * <ul>
     *   <li>TOP  → returns all BOTTOM items</li>
     *   <li>BOTTOM → returns all TOP items</li>
     *   <li>anything else → returns empty list</li>
     * </ul>
     *
     * @param item the clothing item to find matches for
     * @return list of matching {@link ClothingItem} objects; never {@code null}
     */
    public List<ClothingItem> getMatchingItems(ClothingItem item) {
        if (item == null) {
            return new ArrayList<>();
        }

        ClothingCategory targetCategory = getOppositeCategory(item.getCategory());
        if (targetCategory == null) {
            return new ArrayList<>();
        }

        List<ClothingItem> all = clothingRepository.getAllItems();
        List<ClothingItem> matches = new ArrayList<>();

        for (ClothingItem candidate : all) {
            if (candidate.getId() != item.getId()
                    && candidate.getCategory() == targetCategory) {
                // Hydrate tags for display
                tagRepository.getTagsOfItem(candidate.getId())
                             .forEach(candidate::addTag);
                matches.add(candidate);
            }
        }

        return matches;
    }

    /**
     * Returns the opposite category for matching purposes.
     *
     * @param category the source category
     * @return {@link ClothingCategory#BOTTOM} for TOP, {@link ClothingCategory#TOP}
     *         for BOTTOM, or {@code null} for all other categories
     */
    private ClothingCategory getOppositeCategory(ClothingCategory category) {
        if (category == ClothingCategory.TOP) {
            return ClothingCategory.BOTTOM;
        }
        if (category == ClothingCategory.BOTTOM) {
            return ClothingCategory.TOP;
        }
        return null;
    }
}
