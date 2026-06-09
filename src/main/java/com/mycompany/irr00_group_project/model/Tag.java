package com.mycompany.irr00_group_project.model;

import java.util.Objects;

/**
 * Class for tags for clothing items.
 *
 * Tags are identified by a unique {@code id} and have a {@code name} and
 * a {@code category} (as defined in the {@link TagCategory} enum).
 *
 * <p>Equality and hashing are based solely on {@code id} so that a
 * {@code Set<Tag>} inside {@link ClothingItem} correctly deduplicates
 * tags that have been loaded separately from CSV storage.</p>
 *
 * @author Priyanshu Guha
 */
public class Tag {

    private final int id;
    private String name;
    private TagCategory category;

    /**
     * Make a new tag with a specific category.
     *
     * @param id       the unique identifier of the tag
     * @param name     the display name of the tag
     * @param category the type/category of the tag
     */
    public Tag(int id, String name, TagCategory category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    /**
     * Make a new tag with category defaulting to {@code TagCategory.OTHER}.
     *
     * @param id   the unique identifier of the tag
     * @param name the display name of the tag
     */
    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
        this.category = TagCategory.OTHER;
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

    public TagCategory getCategory() {
        return category;
    }

    // =======
    // Setters
    // =======

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(TagCategory category) {
        this.category = category;
    }

    // ==================
    // Equality & Hashing
    // ==================

    /**
     * Two tags are equal when they share the same {@code id}.
     * This ensures {@code Set<Tag>} deduplication works correctly.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tag{id=" + id + ", name='" + name + "', category=" + category + "}";
    }
}