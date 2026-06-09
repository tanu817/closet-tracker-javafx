package com.mycompany.irr00_group_project.database;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;

/**
 * Class for managing tags in the tags file.
 * 
 * @author Aleksandra Levenkova
 */
public class TagRepository {
    
    /**
     * Method to add a new tag to the file.
     * 
     * @param tag to be added
     */
    public void addTag(Tag tag) {
        try (FileWriter writer = new FileWriter("tags.csv", true)) { 
            writer.write(tag.getId() + ","
                + tag.getName() + ","
                + tag.getCategory() + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //USE THE BELOW METHOD IN THE UI
    
    /**
     * Method to generate the next available id for a new tag.
     * 
     * @return next available tag id
     */
    public int generateNextId() {
        List<Tag> tags = getAllTags();
        int maxId = 0;
        for (Tag tag : tags) {
            if (tag.getId() > maxId) {
                maxId = tag.getId();
            }
        }
        return maxId + 1;
    }
    
    /**
     * Method to return all tags.
     * 
     * @return list of all tags
     */
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("tags.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String name = values[1];
                TagCategory category = TagCategory.valueOf(values[2]);
                Tag tag = new Tag(id, name, category);
                tags.add(tag);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tags;
    }
    
    /**
     * Save the updated list of tags in the file.
     * 
     * @param tags that were updated and now need to be saved in the file.
     */
    public void saveTags(List<Tag> tags) {
        try (FileWriter writer = new FileWriter("tags.csv")) { //overwrite
            writer.write("id,name,category\n");
            for (Tag tag : tags) {
                writer.write(tag.getId() + ","
                    + tag.getName() + ","
                    + tag.getCategory() + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Update a tag based on the id.
     * 
     * @param tag to be updated
     */
    public void updateTag(Tag tag) {
        List<Tag> tags = getAllTags();
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getId() == tag.getId()) {
                tags.set(i, tag);
                break;
            }
        }
        saveTags(tags);
    }
    
    /**
     * Delete a tag based on its id.
     * 
     * @param id of the tag that needs to be deleted
     */
    public void deleteTag(int id) {
        List<Tag> tags = getAllTags();
        tags.removeIf(tag -> tag.getId() == id);
        saveTags(tags);
        
        deleteTagRelationships(id);
    }
    
    //METHODS FOR THE ITEM-TAG RELATIONSHIP
    
    /**
     * Method for adding a tag to an item.
     * 
     * @param clothingId of the item to which we assign the tag
     * @param tag tag which we assign to the item
     */
    public void addTagToItem(int clothingId, Tag tag) {
        try (FileWriter writer = new FileWriter("clothing_tags.csv", true)) { 
            writer.write(clothingId + "," + tag.getId() + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Method for getting IDs of tags of an item.
     * (to display correct info on the screen)
     * 
     * @param clothingId of item whose tags IDs needs to be displayed
     * @return tags IDs of item
     */
    public List<Integer> getTagIdsOfItem(int clothingId) {
        List<Integer> tagIds = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("clothing_tags.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int currentItemId = Integer.parseInt(values[0]);
                int tagId = Integer.parseInt(values[1]);
                if (currentItemId == clothingId) {
                    tagIds.add(tagId);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tagIds;
    }
    
    /**
     * Method for deleting a tag from the relationship table.
     * 
     * @param tagID of the tag to delete
     */
    private void deleteTagRelationships(int tagId) {
        List<String> rows = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("clothing_tags.csv"))) {
            String line;
            rows.add(reader.readLine()); // header
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int currentTagId = Integer.parseInt(values[1]);
                if (currentTagId != tagId) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (FileWriter writer = new FileWriter("clothing_tags.csv")) {
            for (String row : rows) {
                writer.write(row + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
                    

