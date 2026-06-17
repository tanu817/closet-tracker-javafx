package com.mycompany.irr00_group_project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for tests that read and write CSV files.
 *
 * Backs up all CSV files before each test, replaces them with empty versions so
 * tests run in isolation, then restores the originals after each test
 * completes.
 *
 * Test classes that need isolated CSV state should extend this class instead of
 * repeating the backup and restore logic themselves.
 *
 * @author Alexandra Varapai
 */
public abstract class CsvTestBase {

    private static final String[] ALL_CSVS = {
        "clothing_items.csv", "tags.csv", "clothing_tags.csv",
        "outfits.csv", "outfit_items.csv"
    };

    private final Map<String, String> backups = new HashMap<>();

    /**
     * Backs up existing CSVs and writes clean empty headers.
     *
     * @throws IOException if backup or write fails
     */
    @BeforeEach
    void csvSetUp() throws IOException {
        for (String name : ALL_CSVS) {
            File f = new File(name);
            backups.put(name, f.exists()
                    ? new String(Files.readAllBytes(f.toPath())) : null);
        }
        writeFile("clothing_items.csv", "id,name,isFavorite,category,wornCount\n");
        writeFile("tags.csv", "id,name,category\n");
        writeFile("clothing_tags.csv", "clothingId,tagId\n");
        writeFile("outfits.csv", "id,name\n");
        writeFile("outfit_items.csv", "outfitId,clothingId1,clothingId2\n");
    }

    /**
     * Restores all CSV files to their pre-test state.
     *
     * @throws IOException if restore fails
     */
    @AfterEach
    void csvRestoreFile() throws IOException {
        for (String name : ALL_CSVS) {
            String original = backups.get(name);
            if (original == null) {
                new File(name).delete();
            } else {
                writeFile(name, original);
            }
        }
    }

    /**
     * Writes content to a CSV file in the working directory.
     *
     * @param name file name
     * @param content content to write
     * @throws IOException if write fails
     */
    protected void writeFile(String name, String content) throws IOException {
        try (FileWriter fw = new FileWriter(name)) {
            fw.write(content);
        }
    }

    /**
     * Reads the contents of the specified file. Returns an empty string if the
     * file does not exist.
     *
     * @param name the file name
     * @return the file contents, or an empty string if the file does not exist
     * @throws IOException if write fails
     */
    protected String readOrEmpty(String name) throws IOException {
        File f = new File(name);
        if (!f.exists()) {
            return "";
        }
        return new String(Files.readAllBytes(f.toPath()));
    }
}
