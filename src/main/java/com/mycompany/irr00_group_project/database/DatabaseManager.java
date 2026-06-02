package com.mycompany.irr00_group_project.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for creating a new SQLite database and tables.
 *
 * @author Aleksandra Levenkova
 */
public class DatabaseManager {

    private static final String URL =  "jdbc:sqlite:myCloset.db";
    
    /**
     * Method to open a connection to the SQLite database.
     * @return active connection to myCloset database
     * @throws SQLException if database connection fails
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    } 
    
    /**
     * Method to create database tables.
     */
    public static void initialiseDatabase() {
        
        String clothingItemsTable = """
            CREATE TABLE IF NOT EXISTS clothing_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                wear_count INTEGER DEFAULT 0,
                category TEXT NOT NULL                           
            );
            """;
        
        String tagsTable = """
            CREATE TABLE IF NOT EXISTS tags (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT UNIQUE NOT NULL,
                type TEXT NOT NULL
            );
            """;
        
        //many to many relationship between items and tags
        String clothingTagsTable = """
            CREATE TABLE IF NOT EXISTS clothing_tags (
                clothing_id INTEGER,
                tag_id INTEGER,
                PRIMARY KEY (clothing_id, tag_id),
                FOREIGN KEY (clothing_id) REFERENCES clothing_items(id),
                FOREIGN KEY (tag_id) REFERENCES tags(id)
            );
            """;
        
        String outfitsTable = """
            CREATE TABLE IF NOT EXISTS outfits (
                id INTEGER PRIMARY KEY AUTOINCREMENT
            );
            """;
        
        //many to many relationship between outfits and items
        //not needed with tags as it would be duplicate
        String outfitItemsTable = """
            CREATE TABLE IF NOT EXISTS outfit_items (
                outfit_id INTEGER,
                clothing_id INTEGER,
                PRIMARY KEY (outfit_id, clothing_id),
                FOREIGN KEY (outfit_id) REFERENCES outfits(id),
                FOREIGN KEY (clothing_id) REFERENCES clothing_items(id)
            );
            """;
                         
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            
            stmt.execute(clothingItemsTable);
            stmt.execute(tagsTable);
            stmt.execute(clothingTagsTable);
            stmt.execute(outfitsTable);
            stmt.execute(outfitItemsTable);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());   
        }
    }
}
