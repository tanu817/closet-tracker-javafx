package com.mycompany.irr00_group_project.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for creating CSV files.
 *
 * @author Aleksandra Levenkova
 */
public class DatabaseManager {
    
    /**
     * Method to create CSV files to create a database with items, tags, outfits and relationships.
     */
    public static void initialiseFiles() {
        
        createFileIfMissing("clothing_items.csv");
        createFileIfMissing("tags.csv");
        createFileIfMissing("clothing_tags.csv");
        createFileIfMissing("outfits.csv");
        createFileIfMissing("outfit_items.csv");
    }
    
    /**
     * Helper method to create files if not present, including the columns.
     * @param fileName for the CSV file
     */
    private static void createFileIfMissing(String fileName) {
        
        File file = new File(fileName);
        
        try {
            
            if (file.createNewFile()) {
                
                FileWriter writer = new FileWriter(file);
                
                if (fileName.equals("clothing_items.csv")) {
                    writer.write("id,name,isFavorite,category,wornCount\n");
                    
                } else if (fileName.equals("tags.csv")) {
                    writer.write("id, name,category\n");
                    
                } else if (fileName.equals("clothing_tags.csv")) {
                    writer.write("clothingId,tagId\n");
                    
                } else if (fileName.equals("outfits.csv")) {
                    writer.write("id,name\n");
                    
                } else if (fileName.equals("outfit_items.csv")) {
                    writer.write("outfitId,clothingId1,clothingId2\n");
                }
                
                writer.close();  
            }
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
