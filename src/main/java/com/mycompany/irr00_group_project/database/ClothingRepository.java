package com.mycompany.irr00_group_project.database;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.io.*;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.ClothingCategory;

/**
 * Class for managing clothing items in clothing_items.
 * 
 * @author Aleksandra Levenkova
 */
public class ClothingRepository {
    
    /**
     * Method for storing a new clothing item in the database.
     * 
     * @param item clothing item to add
     */
    public void addItem(ClothingItem item) {
        
        try (FileWriter writer = new FileWriter("clothing_items.csv", true)) { //append not overwrite
            writer.write(
            item.getId() + ","
            + item.getName() + ","
            + item.isFavorite() + ","
            + item.getCategory() + ","
            + item.getWornCount() + ","
            + item.getLastWornDate() + "\n");
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }    
    
    //USE THE BELOW METHOD IN UI TO ASSIGN AN ID BEFORE USING addItem()
    
    /**
     * Generate the next item ID for the UI to use
     * @return next available ID
     */
    public int generateNextId() {
        List<ClothingItem> items = getAllItems();
        int maxId = 0;
        for (ClothingItem item : items) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }
        return maxId + 1;
    }
    
    /**
     * Get all clothing items.
     * 
     * @return all clothing items
     */
    public List<ClothingItem> getAllItems() {
        List<ClothingItem> items = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("clothing_items.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String name = values[1];
                boolean isFavorite = Boolean.parseBoolean(values[2]);
                ClothingCategory category = ClothingCategory.valueOf(values[3]);
                int wornCount = Integer.parseInt(values[4]);
                LocalDate lastWornDate;
                if (values[5].equals("null")) {
                    lastWornDate = null;
                } else {
                    lastWornDate =
                            LocalDate.parse(values[5]);
                }
                ClothingItem item = new ClothingItem(id, name, isFavorite, category, wornCount, lastWornDate);
                items.add(item);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return items;
    }
    
    /**
     * Method to save an updated list of items into the csv.
     * 
     * @param items 
     */
    public void saveItems(List<ClothingItem> items) {
        try (FileWriter writer = new FileWriter("clothing_items.csv")) { //overwrite
            writer.write("id,name,isFavorite,category,wornCount,lastWornDate\n");
            for (ClothingItem item : items) {
                writer.write(item.getId() + ","
                    + item.getName() + ","
                    + item.isFavorite() + ","
                    + item.getCategory() + ","
                    + item.getWornCount() + ","
                    + item.getLastWornDate() + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Method for removing a clothing item from the database.
     * 
     * @param id to be deleted
     */
    public void deleteItem(int id) {
        List<ClothingItem> items = getAllItems();
        items.removeIf(item -> item.getId() == id);
        saveItems(items);
    }
    
    /**
     * Method for changing properties of a clothing item.
     * 
     * @param item to be updated
     */
    public void changeItem(ClothingItem item) {
        List<ClothingItem> items = getAllItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == item.getId()) {
                items.set(i, item);
                break;
            }
        }
        saveItems(items);
    }
    
    /**
     * Get top 5 favorites with highest worn count.
     * 
     * @return 5 favorites with highest worn count
     */
    public List<ClothingItem> getTop5FavouriteItems() {
        List<ClothingItem> items = getAllItems();
        List<ClothingItem> favorites = new ArrayList<>();
        for (ClothingItem item : items) {
            if (item.isFavorite()) {
                favorites.add(item);
            }
        }
        for (int i = 0; i < favorites.size() - 1; i++) {
            for (int j = 0; j < favorites.size() - i - 1; j++) {
                if (favorites.get(j).getWornCount() < favorites.get(j + 1).getWornCount()) {
                    ClothingItem temp = favorites.get(j);
                    favorites.set(j, favorites.get(j + 1));
                    favorites.set(j + 1, temp);
                }
            }
        }
        if (favorites.size() > 5) {
            return favorites.subList(0, 5);
        } else {
            return favorites;
        }
    }
    
    /**
     * Update the wearCount of an item from the search outfit page.
     * 
     * @param id of item whose wearCoutn needs to be updated
     */
    public void markAsWorn(int id) {
        List<ClothingItem> items = getAllItems();
        for (ClothingItem item : items) {
            if (item.getId() == id) {
                item.wear();
                break;
            }
        }
        saveItems(items);
    }
    
    /**
     * Return top 5 most worn items with the highest wearCount.
     * 
     * @return top 5 most worn items with the highest wearCount
     */
    public List<ClothingItem> getTop5Worn() {
        List<ClothingItem> items = getAllItems();
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = 0; j < items.size() - i - 1; j++) {
                if (items.get(j).getWornCount() < items.get(j + 1).getWornCount()) {
                    ClothingItem temp = items.get(j);
                    items.set(j, items.get(j + 1));
                    items.set(j + 1, temp);
                }
            }
        }
        if (items.size() > 5) {
           return items.subList(0, 5);
        } else {
            return items;
        }
    }
    
    /**
     * Return top 5 least worn items with the lowest wearCount.
     * 
     * @return top 5 least worn items with the lowest wearCount
     */
    public List<ClothingItem> getLeast5Worn() {
        List<ClothingItem> items = getAllItems();
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = 0; j < items.size() - i - 1; j++) {
                if (items.get(j).getWornCount() < items.get(j + 1).getWornCount()) {
                    ClothingItem temp = items.get(j);
                    items.set(j, items.get(j + 1));
                    items.set(j + 1, temp);
                }
            }
        }
        if (items.size() > 5) {
           return items.subList(items.size() - 5, items.size());
        } else {
            return items;
        }
    }
    
}