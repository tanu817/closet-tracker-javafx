package com.mycompany.irr00_group_project.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Creates and initialises the CSV files that act as the application database,
 * and seeds a standard set of tags on first run.
 *
 * <p>
 * Call {@link #initialiseFiles()} once at application startup (e.g. from
 * {@code App.start()}). Each file is only created when it does not already
 * exist, so existing data is never overwritten.</p>
 *
 * <h2>CSV schemas</h2>
 * <pre>
 *   clothing_items.csv  – id,name,isFavorite,category,wornCount
 *   tags.csv            – id,name,category
 *   clothing_tags.csv   – clothingId,tagId
 *   outfits.csv         – id,name
 *   outfit_items.csv    – outfitId,clothingId1,clothingId2
 * </pre>
 *
 * <h2>Seeded tags (stable IDs)</h2>
 * <p>
 * A fixed set of common tags is written to {@code tags.csv} the first time the
 * file is created. Their IDs are stable integers (1–21) so that
 * {@code clothing_tags.csv} relationships always resolve correctly.</p>
 *
 * @author Aleksandra Levenkova
 */
public class DatabaseManager {

    private DatabaseManager() {
        // utility class — not instantiated
    }

    /**
     * Creates all CSV files (with headers) if they do not yet exist, then seeds
     * the standard tag list into {@code tags.csv} if it is still empty.
     */
    public static void initialiseFiles() {
        createFileIfMissing("clothing_items.csv", "id,name,isFavorite,category,wornCount");
        createFileIfMissing("tags.csv", "id,name,category");
        createFileIfMissing("clothing_tags.csv", "clothingId,tagId");
        createFileIfMissing("outfits.csv", "id,name");
        createFileIfMissing("outfit_items.csv", "outfitId,clothingId1,clothingId2");

        seedDefaultTagsIfEmpty();
    }

    /**
     * Creates {@code fileName} and writes {@code header} as the first line.
     * Does nothing when the file already exists.
     */
    private static void createFileIfMissing(String fileName, String header) {
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(header + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println(
                    "DatabaseManager: could not create " + fileName + " – " + e.getMessage());
        }
    }

    /**
     * Checks whether {@code tags.csv} contains any data rows (beyond the
     * header). If it is empty, writes the standard predefined tags with fixed
     * stable IDs starting at 1.
     *
     * <p>
     * These IDs are reserved. Any user-created tags receive IDs ≥ 100 (the
     * {@link TagRepository#generateNextId()} starts after the max existing ID,
     * so as long as the seed IDs are below 100 there is no collision with
     * future user tags either).</p>
     */
    private static void seedDefaultTagsIfEmpty() {
        File tagsFile = new File("tags.csv");
        if (!tagsFile.exists()) {
            return;
        }

        // Count data rows (skip header)
        int dataRows = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(tagsFile))) {
            reader.readLine(); // header
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    dataRows++;
                }
            }
        } catch (IOException e) {
            System.err.println("DatabaseManager.seedDefaultTagsIfEmpty (read): " + e.getMessage());
            return;
        }

        if (dataRows > 0) {
            return; // already has data – do not overwrite
        }

        // Write predefined tags. Format: id,name,category
        // IDs 1-4   → clothing type (stored as OTHER because ClothingCategory
        //              covers type; these tags let users filter by type in Search)
        // IDs 5-12  → colours
        // IDs 13-16 → weather
        // IDs 17-19 → events
        // IDs 20-21 → details
        String[] seeds = {
            // Type tags  (category = OTHER so they appear in the "Other" section
            //             of AddItemsPage — the Type row is handled by ClothingCategory)
            // We put these under OTHER so they don't pollute dedicated category rows.
            "1,Top,OTHER",
            "2,Bottom,OTHER",
            "3,One Piece,OTHER",
            "4,Accessory,OTHER",
            // Colour tags
            "5,White,COLOR",
            "6,Black,COLOR",
            "7,Blue,COLOR",
            "8,Pink,COLOR",
            "9,Green,COLOR",
            "10,Red,COLOR",
            "11,Beige,COLOR",
            "12,Brown,COLOR",
            // Weather tags
            "13,Summer,WEATHER",
            "14,Winter,WEATHER",
            "15,Spring,WEATHER",
            "16,Autumn,WEATHER",
            // Event tags
            "17,Casual,EVENT",
            "18,Formal,EVENT",
            "19,Party,EVENT",
            // Detail tags
            "20,Minimal,DETAIL",
            "21,Vintage,DETAIL"};

        try (FileWriter writer = new FileWriter(tagsFile, true)) { // append after header
            for (String row : seeds) {
                writer.write(row + "\n");
            }
        } catch (IOException e) {
            System.err.println("DatabaseManager.seedDefaultTagsIfEmpty (write): " + e.getMessage());
        }
    }
}
