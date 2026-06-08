package com.mycompany.irr00_group_project.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for clothing items.
 * 
 * A clothing item
 * - is defined by an {@code id},
 * - is known by a {@code name},
 * - {@code isFavorite} or not,
 * - has a {@code category}, defined at the enum ClothingCategory,
 * - has a list of {@code tags}.
 * - is owned by a {@code user}
 * - has a {@code wornCount} and {@code lastWornDate}.
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
     * Initializes a clothing item with its required properties.
     * 
     * @param id is the ID number for this clothing item.
     * @param name is the name by which this clothing item should be known.
     * @param category is the category/type of clothing item this is.
     */
    public ClothingItem(int id, String name, ClothingCategory category) {
        this.id = id;
        this.name = name;
        this.isFavorite = false;
        this.category = category;
        this.tags = new HashSet<>();
        this.wornCount = 0;
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
    
    public Set<Tag> getTags() {
        return tags;
    }
    
    public int getWornCount() {
        return wornCount;
    }
    
    // =======
    // Setters
    // =======
    
    public void rename(String newName) {
        name = newName;
    }
    
    public void toggleFavorite() {
        isFavorite = !isFavorite;
    }
    
    public void changeCategory(ClothingCategory newCategory) {
        category = newCategory;
    }
    
    public void addTag(Tag t) {
        tags.add(t);
    }
    
    public void removeTag(Tag t) {
        tags.remove(t);
    }
    
    /**
     * Record that a clothing item has been worn.
     */
    public void wear() {
        wornCount++;
    }
    
    /**
     * Constructor for loading an existing item from CSV storage.
     * @param id is the ID number for this clothing item.
     * @param name is the name by which this clothing item should be known.
     * @param isFavorite is the Boolean value fo whether it is liked or not.
     * @param category is the category/type of clothing item this is.
     * @param wornCount is the number of times this item was worn.
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
}