package com.mycompany.irr00_group_project.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for outfits.
 *
 * An outfit should be defined only of a set of clothing items, but it is good
 * to give each outfit an ID.
 *
 * @author Priyanshu Guha
 */
public class Outfit {

    private final int id;
    private String name;
    private final Set<ClothingItem> items;

    /**
     * Initializes an outfit.
     *
     * @param id is the ID number for this outfit.
     * @param name is the name by which this outfit could be known.
     * @param items is the set of items this outfit is made of.
     */
    public Outfit(int id, String name, Set<ClothingItem> items) {
        this.id = id;
        this.name = name;
        this.items = new HashSet<>(items);
    }

    /**
     * Initializes an outfit. {@code name} is automatically generated from the
     * set of items.
     *
     * @param id is the ID number for this outfit.
     * @param items is the set of items this outfit is made of.
     */
    public Outfit(int id, Set<ClothingItem> items) {
        this.id = id;
        this.items = items;

        List<String> itemNames = new ArrayList<>();
        for (ClothingItem item : items) {
            itemNames.add(item.getName());
        }
        this.name = String.join(" + ", itemNames);
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

    // =======
    // Setters
    // =======
    public void rename(String newName) {
        name = newName;
    }

    public void addItem(ClothingItem item) {
        items.add(item);
    }

    public void removeItem(ClothingItem item) {
        items.remove(item);
    }

    public void wear() {
        // TODO: Increment {@code wornCount},
        //       Update {@code lastWornDate},
        //       Call wear() for all of this outfit's clothing items.
        //       Add WearEvent to database.
    }

    // More functions to possibly implement:
    //      getCommonTags()
    //          (get a list of all tags that are common to all clothing items for this outfit)
    // ...
    /**
     * Initializes an outfit for saving in the database.
     *
     * @param id is the ID number for this outfit.
     * @param name is the name by which this outfit could be known.
     */
    public Outfit(int id, String name) {
        this.id = id;
        this.name = name;
        this.items = new HashSet<>();
    }
}
