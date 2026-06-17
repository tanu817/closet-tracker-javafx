package com.mycompany.irr00_group_project.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DatabaseManager}.
 *
 * @author Alexandra Varapai
 */
public class DatabaseManagerTest {

    private static final String[] CSV_FILES = {
        "clothing_items.csv", "tags.csv", "clothing_tags.csv",
        "outfits.csv", "outfit_items.csv"
    };

    private final Map<String, String> backups = new HashMap<>();

    /**
     * Backs up all CSV files and deletes them so initialiseFiles() begins with
     * a clean state.
     *
     * @throws IOException if backup or deletion fails
     */
    @BeforeEach
    void setUp() throws IOException {
        for (String name : CSV_FILES) {
            File f = new File(name);
            backups.put(name, f.exists()
                    ? new String(Files.readAllBytes(f.toPath())) : null);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    /**
     * Restores CSV files to their original state.
     *
     * @throws IOException if restore fails
     */
    @AfterEach
    void restoreFile() throws IOException {
        for (String name : CSV_FILES) {
            String original = backups.get(name);
            if (original == null) {
                new File(name).delete();
            } else {
                try (FileWriter fw = new FileWriter(name)) {
                    fw.write(original);
                }
            }
        }
    }

    @Test
    @DisplayName("initialiseFiles() creates all CSV files")
    void initialiseFilesCreatesAllFiles() {
        DatabaseManager.initialiseFiles();
        for (String name : CSV_FILES) {
            assertTrue(new File(name).exists(),
                    name + " should be created by initialiseFiles()");
        }
    }

    @Test
    @DisplayName("initialiseFiles() seeds default tags into tags.csv")
    void initialiseFilesSeedsDefaultTags() throws IOException {
        DatabaseManager.initialiseFiles();
        List<String> lines = Files.readAllLines(new File("tags.csv").toPath());
        assertTrue(lines.size() > 1, "tags.csv should contain seeded (default)tag rows");
    }

    @Test
    @DisplayName("initialiseFiles() does not overwrite existing data")
    void initialiseFilesDoesNotOverwrite() throws IOException {
        try (FileWriter fw = new FileWriter("clothing_items.csv")) {
            fw.write("id,name,isFavorite,category,wornCount\n1,Shirt,false,TOP,5\n");
        }
        DatabaseManager.initialiseFiles();
        List<String> lines = Files.readAllLines(new File("clothing_items.csv").toPath());
        assertTrue(lines.stream().anyMatch(l -> l.contains("Shirt")),
                "Existing data must not be overwritten");
    }

    @Test
    @DisplayName("initialiseFiles() can be run multiple times safely (twice)")
    void initialiseFilesIdempotent() throws IOException {
        DatabaseManager.initialiseFiles();
        long sizeFirst = new File("tags.csv").length();
        DatabaseManager.initialiseFiles();
        long sizeSecond = new File("tags.csv").length();
        assertEquals(sizeFirst, sizeSecond,
                "initialiseFiles() can safely run twice without adding duplicate seed data");
    }
}
