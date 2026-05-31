package com.mycompany.irr00_group_project.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for clothing items.
 * 
 * @author Priyanshu Guha
 */
public class ClothingItem {
    private final int id;
    private String name;
    private int wornCount;
    private boolean isFavorite;
    private ClothingCategory category;
    private Set<Tag> tags;
    
    // Constructor
    public ClothingItem(int id, String name, ClothingCategory category) {
        this.id = id;
        this.name = name;
        int wornCount = 0;
        boolean isFavorite = false;
        this.category = category;
        this.tags = new HashSet<>();
    }
    
    // Getters
    public int getId() {return id;}
    public String getName() {return name;}
    public int getWornCount() {return wornCount;}
    public boolean isFavorite() {return isFavorite;}
    public ClothingCategory getCategory() {return category;}
    public Set<Tag> getTags() {return tags;}
    
    // Setters
    public void rename(String newName) {name = newName;}
    public void incrementWornCount() {wornCount++;}
    public void toggleFavorite() {isFavorite = !isFavorite;}
    public void changeCategory(ClothingCategory newCategory) {category = newCategory;}
    public void addTag(Tag t) {tags.add(t);}
    public void removeTag(Tag t) {tags.remove(t);}
}