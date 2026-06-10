package com.mycompany.irr00_group_project.database;

import com.mycompany.irr00_group_project.model.ClothingCategory;
import com.mycompany.irr00_group_project.model.ClothingItem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Alexandra Varapai
 */
public class OutfitRepositoryTest {
    //Original CSV file contents saved before each test
    private String originalItems;
    private String originalOutfitItems;
    private String originalClothingTags;

    /**
     * Saves original CSV files and replaces them with empty test versions.
     *
     * @throws IOException if reading or writing a file fails
     */
    @BeforeEach
    void setUp() throws IOException {
        originalItems = readOrEmpty("clothing_items.csv");
        originalOutfitItems = readOrEmpty("outfit_items.csv");
        originalClothingTags = readOrEmpty("clothing_tags.csv");
        writeFile("clothing_items.csv", "id,name,isFavorite,category,wornCount\n");
        writeFile("outfit_items.csv", "outfitId,clothingId1,clothingId2\n");
        writeFile("clothing_tags.csv", "clothingId,tagId\n");
    }

    /**
     * Restores original CSV files after each test.
     *
     * @throws IOException if reading or writing a file fails
     */
    @AfterEach
    void restoreFile() throws IOException {
        writeFile("clothing_items.csv", originalItems);
        writeFile("outfit_items.csv", originalOutfitItems);
        writeFile("clothing_tags.csv", originalClothingTags);
    }

    // ==================
    // TESTS
    // ==================
    
    
    // ==================
    // Helper methods
    // ==================
    private String readOrEmpty(String name) throws IOException {
        File f = new File(name);
        if (!f.exists()) {
            return "";
        }
        return new String(Files.readAllBytes(f.toPath()));
    }

    private void writeFile(String name, String content) throws IOException {
        try (FileWriter fw = new FileWriter(name)) {
            fw.write(content);
        }
    }
    
}
