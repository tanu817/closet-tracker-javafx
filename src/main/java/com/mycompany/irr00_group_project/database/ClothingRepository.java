package com.mycompany.irr00_group_project.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;

/**
 * Repository for {@link ClothingItem} objects persisted in
 * {@code clothing_items.csv}.
 *
 * <p>
 * This class is purely I/O: it reads and writes CSV files and performs no
 * business logic.</p>
 *
 * <h2>CSV schema</h2>
 * <pre>
 *   clothing_items.csv – id, name, isFavorite, category, wornCount
 *   outfit_items.csv   – outfitId, clothingId  (one row per item per outfit)
 *   clothing_tags.csv  – clothingId, tagId
 * </pre>
 *
 * <p>
 * Note: tags are <em>not</em> stored inside {@code clothing_items.csv}. They
 * are stored as relationships in {@code clothing_tags.csv} and loaded
 * separately by the service layer via {@link TagRepository}.</p>
 *
 * @author Aleksandra Levenkova
 */
public class ClothingRepository {

    private static final String ITEMS_FILE = "clothing_items.csv";
    private static final String OUTFIT_ITEMS_FILE = "outfit_items.csv";
    private static final String CLOTHING_TAGS_FILE = "clothing_tags.csv";

    /**
     * Appends a new clothing item to {@code clothing_items.csv}.
     *
     * @param item the item to persist
     */
    public void addItem(ClothingItem item) {
        try (FileWriter writer = new FileWriter(ITEMS_FILE, true)) {
            writer.write(item.getId() + ","
                    + escapeCsv(item.getName()) + ","
                    + item.isFavorite() + ","
                    + item.getCategory() + ","
                    + item.getWornCount() + "\n");
        } catch (IOException e) {
            System.err.println("ClothingRepository.addItem: " + e.getMessage());
        }
    }

    /**
     * Generates the next available item ID (max existing ID + 1).
     *
     * @return next available ID
     */
    public int generateNextId() {
        int maxId = 0;
        for (ClothingItem item : getAllItems()) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Returns all clothing items stored in {@code clothing_items.csv}. Tags are
     * NOT loaded here; use the service layer to hydrate them.
     *
     * @return list of all {@link ClothingItem} objects; empty list on error
     */
    public List<ClothingItem> getAllItems() {
        List<ClothingItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ITEMS_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 5) {
                    continue; // malformed row – skip
                }
                int id = Integer.parseInt(values[0].trim());
                String name = values[1].trim();
                boolean isFavorite = Boolean.parseBoolean(values[2].trim());
                ClothingCategory cat = ClothingCategory.valueOf(values[3].trim());
                int wornCount = Integer.parseInt(values[4].trim());
                items.add(new ClothingItem(id, name, isFavorite, cat, wornCount));
            }
        } catch (IOException e) {
            System.err.println("ClothingRepository.getAllItems: " + e.getMessage());
        }
        return items;
    }

    /**
     * Finds a single clothing item by its ID.
     *
     * @param id the item ID to find
     * @return the matching {@link ClothingItem}, or {@code null} if not found
     */
    public ClothingItem getItemById(int id) {
        for (ClothingItem item : getAllItems()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Overwrites {@code clothing_items.csv} with the supplied list.
     *
     * @param items the complete list of items to persist
     */
    public void saveItems(List<ClothingItem> items) {
        try (FileWriter writer = new FileWriter(ITEMS_FILE)) {
            writer.write("id,name,isFavorite,category,wornCount\n");
            for (ClothingItem item : items) {
                writer.write(item.getId() + ","
                        + escapeCsv(item.getName()) + ","
                        + item.isFavorite() + ","
                        + item.getCategory() + ","
                        + item.getWornCount() + "\n");
            }
        } catch (IOException e) {
            System.err.println("ClothingRepository.saveItems: " + e.getMessage());
        }
    }

    /**
     * Updates a single clothing item record identified by its ID.
     *
     * @param item the item with updated values
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
     * Deletes a clothing item by ID and removes all its relationships from
     * {@code outfit_items.csv} and {@code clothing_tags.csv}.
     *
     * @param id the ID of the item to delete
     */
    public void deleteItem(int id) {
        List<ClothingItem> items = getAllItems();
        items.removeIf(item -> item.getId() == id);
        saveItems(items);

        deleteItemFromOutfitRelationship(id);
        deleteItemFromTagRelationship(id);
    }

    /**
     * Increments the {@code wornCount} of the item with the given ID.
     *
     * @param id the ID of the item to mark as worn
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
     * Returns the top 5 favorite items sorted by {@code wornCount} descending.
     *
     * @return up to 5 favorite {@link ClothingItem} objects
     */
    public List<ClothingItem> getTop5FavouriteItems() {
        List<ClothingItem> favorites = new ArrayList<>();
        for (ClothingItem item : getAllItems()) {
            if (item.isFavorite()) {
                favorites.add(item);
            }
        }
        bubbleSortDescending(favorites);
        return favorites.size() > 5 ? favorites.subList(0, 5) : favorites;
    }

    /**
     * Returns the top 5 most-worn items sorted by {@code wornCount} descending.
     *
     * @return up to 5 most-worn {@link ClothingItem} objects
     */
    public List<ClothingItem> getTop5Worn() {
        List<ClothingItem> items = getAllItems();
        bubbleSortDescending(items);
        return items.size() > 5 ? items.subList(0, 5) : items;
    }

    /**
     * Returns the 5 least-worn items sorted by {@code wornCount} ascending.
     *
     * @return up to 5 least-worn {@link ClothingItem} objects
     */
    public List<ClothingItem> getLeast5Worn() {
        List<ClothingItem> items = getAllItems();
        bubbleSortAscending(items);
        return items.size() > 5 ? new ArrayList<>(items.subList(0, 5)) : items;
    }

    /**
     * Removes all rows in {@code outfit_items.csv} that reference the given
     * clothing item ID.
     *
     * @param itemId the clothing item ID to remove
     */
    private void deleteItemFromOutfitRelationship(int itemId) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(OUTFIT_ITEMS_FILE))) {
            String header = reader.readLine();
            if (header != null) {
                rows.add(header);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 2) {
                    continue;
                }
                int clothingId = Integer.parseInt(values[1].trim());
                if (clothingId != itemId) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("ClothingRepository.deleteItemFromOutfitRelationship: " + e.getMessage());
            return;
        }
        writeRows(OUTFIT_ITEMS_FILE, rows);
    }

    /**
     * Removes all rows in {@code clothing_tags.csv} that reference the given
     * clothing item ID.
     *
     * @param itemId the clothing item ID whose tag relationships should be
     * removed
     */
    private void deleteItemFromTagRelationship(int itemId) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLOTHING_TAGS_FILE))) {
            String header = reader.readLine();
            if (header != null) {
                rows.add(header);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 2) {
                    continue;
                }
                int currentItemId = Integer.parseInt(values[0].trim());
                if (currentItemId != itemId) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("ClothingRepository.deleteItemFromTagRelationship: " + e.getMessage());
            return;
        }
        writeRows(CLOTHING_TAGS_FILE, rows);
    }

    /**
     * Atomically rewrites a CSV file with the supplied rows (header included).
     *
     * @param fileName destination file path
     * @param rows all rows (including header as first element)
     */
    private void writeRows(String fileName, List<String> rows) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String row : rows) {
                writer.write(row + "\n");
            }
        } catch (IOException e) {
            System.err.println("ClothingRepository.writeRows(" + fileName + "): " + e.getMessage());
        }
    }

    /**
     * In-place bubble sort — descending by {@code wornCount}.
     */
    private void bubbleSortDescending(List<ClothingItem> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getWornCount() < list.get(j + 1).getWornCount()) {
                    ClothingItem tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }
    }

    /**
     * In-place bubble sort — ascending by {@code wornCount}.
     */
    private void bubbleSortAscending(List<ClothingItem> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getWornCount() > list.get(j + 1).getWornCount()) {
                    ClothingItem tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }
    }

    /**
     * Wraps a field value in double-quotes if it contains a comma, a
     * double-quote, or a newline — minimal RFC 4180 escaping.
     *
     * @param value raw field value
     * @return safe CSV field value
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
