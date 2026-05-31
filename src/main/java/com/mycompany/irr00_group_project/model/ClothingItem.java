package com.mycompany.irr00_group_project.model;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

/**
 * Class for clothing items.
 * 
 * A clothing item
 * - is defined by an {@code id},
 * - is known by a {@code name},
 * - has a recorded list of {@code WearEvents},
 * - {@code isFavorite} or not,
 * - has a {@code category}, defined at the enum ClothingCategory,
 * - has a list of {@code tags}.
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
    private LocalDate lastWornDate;
    
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
        this.lastWornDate = null;
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
    
    /**
     * Function for returning the worn count.
     * 
     * @return the total number of times this clothing item has been worn, as recorded in the DB.
     */
    public int getWornCount() {
        // TODO: Search the database for WearEvents for outfits containing this item,
        // and return the total number of found WearEvents.
        return 0;
    }
    
    /**
     * Function for returning the last worn date.
     * 
     * @return the date when this clothing item was last worn.
     */
    public LocalDate getLastWornDate() {
        if (wornCount == 0) {
            return null;
        } else {
            return lastWornDate;
        }
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
    
    public void wear() {
        wornCount++;
        lastWornDate = LocalDate.now();
    }
}