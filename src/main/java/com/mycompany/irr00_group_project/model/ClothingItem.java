package com.mycompany.irr00_group_project.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for clothing items.
 *
 * A clothing item - is identified by a unique {@code id}, - is known by a
 * {@code name}, - may be marked as a {@code favorite}, - has a {@code category}
 * (defined in {@link ClothingCategory}), - has a set of {@code tags}
 * (identified by their IDs via {@link Tag}), - tracks how many times it has
 * been worn via {@code wornCount}.
 *
 * <p>
 * Tags inside a clothing item are keyed by the {@link Tag#getId()} value. The
 * in-memory set is populated by the repository after loading relationships from
 * {@code clothing_tags.csv}.</p>
 *
 * @author Priyanshu Guha
 */
public class ClothingItem {

    private final int id;
    private String name;
    private boolean isFavorite;
    private ClothingCategory category;
    private final Set<Tag> tags;
    private int wornCount;

    /**
     * Initializes a new clothing item with required properties.
     *
     * @param id the unique ID for this clothing item
     * @param name the display name of this item
     * @param category the clothing category/type
     */
    public ClothingItem(int id, String name, ClothingCategory category) {
        this.id = id;
        this.name = name;
        this.isFavorite = false;
        this.category = category;
        this.tags = new HashSet<>();
        this.wornCount = 0;
    }

    /**
     * Constructor for loading an existing item from CSV storage.
     *
     * @param id the unique ID for this clothing item
     * @param name the display name of this item
     * @param isFavorite whether this item is marked as a favorite
     * @param category the clothing category/type
     * @param wornCount the number of times this item has been worn
     */
    public ClothingItem(int id, String name,
            boolean isFavorite, ClothingCategory category,
            int wornCount) {
        this.id = id;
        this.name = name;
        this.isFavorite = isFavorite;
        this.category = category;
        this.tags = new HashSet<>();
        this.wornCount = wornCount;
    }

    // =======
    // Getters
    // =======
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public ClothingCategory getCategory() {
        return category;
    }

    /**
     * Returns an unmodifiable view of this item's tags. Use
     * {@link #addTag(Tag)} and {@link #removeTag(Tag)} to mutate.
     *
     * @return read-only view of all associated {@link Tag} objects
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public int getWornCount() {
        return wornCount;
    }

    // =======
    // Setters
    // =======
    public void rename(String newName) {
        this.name = newName;
    }

    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
    }

    public void changeCategory(ClothingCategory newCategory) {
        this.category = newCategory;
    }

    /**
     * Adds a tag to this item. Duplicate tags (same {@code id}) are ignored
     * because the backing collection is a {@code Set}.
     *
     * @param tag the tag to associate with this item
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Removes a tag from this item by object identity (matched on tag ID).
     *
     * @param tag the tag to remove
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * Removes a tag from this item by its ID.
     *
     * @param tagId the ID of the tag to remove
     */
    public void removeTagById(int tagId) {
        tags.removeIf(t -> t.getId() == tagId);
    }

    /**
     * Returns {@code true} if this item has a tag with the given ID.
     *
     * @param tagId the tag ID to check
     * @return {@code true} if the tag is present
     */
    public boolean hasTag(int tagId) {
        for (Tag t : tags) {
            if (t.getId() == tagId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Records that this clothing item has been worn once.
     */
    public void wear() {
        wornCount++;
    }

    @Override
    public String toString() {
        return "ClothingItem{id=" + id + ", name='" + name + "', category=" + category + "}";
    }
}
