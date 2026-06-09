package com.mycompany.irr00_group_project.database;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import com.mycompany.irr00_group_project.model.Outfit;

/**
 * Class for managing outfits in the outfits file.
 * 
 * @author Aleksandra Levenkova
 */
public class OutfitRepository {
    
    /**
     * Method to add a new outfit to the file.
     * 
     * @param outfit to be added
     */
    public void addOutfit(Outfit outfit) {
        try (FileWriter writer = new FileWriter("outfits.csv", true)) { 
            writer.write(outfit.getId() + ","
                + outfit.getName() + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //USE THE BELOW METHOD IN THE UI
    
    /**
     * Method to generate the next available id for a new outfit.
     * 
     * @return next available outfit id
     */
    public int generateNextId() {
        List<Outfit> outfits = getAllOutfits();
        int maxId = 0;
        for (Outfit outfit : outfits) {
            if (outfit.getId() > maxId) {
                maxId = outfit.getId();
            }
        }
        return maxId + 1;
    }
    
    /**
     * Method to return all outfits.
     * 
     * @return list of all outfits
     */
    public List<Outfit> getAllOutfits() {
        List<Outfit> outfits = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("outfits.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String name = values[1];
                Outfit outfit = new Outfit(id, name);
                outfits.add(outfit);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return outfits;
    }
    
    /**
     * Save the updated list of outfits in the file.
     * 
     * @param outfits that were updated and now need to be saved in the file.
     */
    public void saveOutfits(List<Outfit> outfits) {
        try (FileWriter writer = new FileWriter("outfits.csv")) { //overwrite
            writer.write("id,name\n");
            for (Outfit outfit : outfits) {
                writer.write(outfit.getId() + ","
                    + outfit.getName() + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Update a outfit based on the id.
     * 
     * @param outfit to be updated
     */
    public void updateOutfit(Outfit outfit) {
        List<Outfit> outfits = getAllOutfits();
        for (int i = 0; i < outfits.size(); i++) {
            if (outfits.get(i).getId() == outfit.getId()) {
                outfits.set(i, outfit);
                break;
            }
        }
        saveOutfits(outfits);
    }
    
    /**
     * Delete an outfit based on its id.
     * 
     * @param id of the outfit that needs to be deleted
     */
    public void deleteOutfit(int id) {
        List<Outfit> outfits = getAllOutfits();
        outfits.removeIf(outfit -> outfit.getId() == id);
        saveOutfits(outfits);
        
        deleteOutfitItems(id);
    }
    
    //METHODS FOR THE ITEM-OUTFIT RELATIONSHIP
    
    /**
     * Method for adding an item (top/bottom) to an outfit.
     * 
     * @param outfitId of the outfit to which we are adding the item
     * @param clothingId1 of the first item that needs to be added to the outfit
     * @param clothingId2 of the second item that needs to be added to the outfit
     */
    public void addItemToOutfit(int outfitId, int clothingId1, int clothingId2) {
        try (FileWriter writer = new FileWriter("outfit_items.csv", true)) { 
            writer.write(outfitId + "," + clothingId1 + "," + clothingId2 + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Method for getting IDs of items that form the outfit.
     * (to display correct info on the screen)
     * 
     * @param outfitId whose item IDs we need to return
     * @return the two IDs of the items that form an outfit
     */
    public List<Integer> getItemIdsForOutfit(int outfitId) {
        List<Integer> itemIds = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("outfit_items.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int outfitID = Integer.parseInt(values[0]);
                int clothingID1 = Integer.parseInt(values[1]);
                int clothingID2 = Integer.parseInt(values[2]);
                if (outfitID == outfitId) {
                    itemIds.add(clothingID1);
                    itemIds.add(clothingID2);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return itemIds;
    }
    
    /**
     * Method for deleting an outfit from the relationship table.
     * 
     * @param outfitId of the outfit to delete
     */
    private void deleteOutfitItems(int outfitId) {
        List<String> rows = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("outfit_items.csv"))) {
            String line;
            rows.add(reader.readLine()); // header
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int currentOutfitId = Integer.parseInt(values[0]);
                if (currentOutfitId != outfitId) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (FileWriter writer = new FileWriter("outfit_items.csv")) {
            for (String row : rows) {
                writer.write(row + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

