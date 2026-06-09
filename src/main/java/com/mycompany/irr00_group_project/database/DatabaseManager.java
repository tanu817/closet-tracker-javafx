package com.mycompany.irr00_group_project.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Creates and initialises the CSV files that act as the application database.
 *
 * <p>Call {@link #initialiseFiles()} once at application startup (e.g. from
 * {@code App.main()}). Each file is only created when it does not already
 * exist, so existing data is never overwritten.</p>
 *
 * <h2>CSV schemas</h2>
 * <pre>
 *   clothing_items.csv  – id, name, isFavorite, category, wornCount
 *   tags.csv            – id, name, category
 *   clothing_tags.csv   – clothingId, tagId
 *   outfits.csv         – id, name
 *   outfit_items.csv    – outfitId, clothingId
 * </pre>
 *
 * <p>Note: {@code outfit_items.csv} stores one row per
 * (outfit, clothing-item) pair, allowing an outfit to contain any number of
 * items without a fixed-column limit.</p>
 *
 * @author Aleksandra Levenkova
 */
public class DatabaseManager {

    private DatabaseManager() {
        // utility class — not instantiated
    }

    /**
     * Creates all CSV files with their header rows if they do not yet exist.
     */
    public static void initialiseFiles() {
        createFileIfMissing("clothing_items.csv", "id,name,isFavorite,category,wornCount");
        createFileIfMissing("tags.csv",            "id,name,category");
        createFileIfMissing("clothing_tags.csv",   "clothingId,tagId");
        createFileIfMissing("outfits.csv",         "id,name");
        createFileIfMissing("outfit_items.csv",    "outfitId,clothingId");
    }

    /**
     * Creates {@code fileName} and writes {@code header} as the first line.
     * Does nothing when the file already exists.
     *
     * @param fileName name (or path) of the CSV file
     * @param header   comma-separated column header, without a trailing newline
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
            System.err.println("DatabaseManager: could not create " + fileName + " – " + e.getMessage());
        }
    }
}