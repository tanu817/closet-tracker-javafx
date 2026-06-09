package com.mycompany.irr00_group_project.model;

/**
 * Class for tags for clothing items.
 * 
 * Tags are defined by a name and have a category as defined in the enum TagCategory.
 * 
 * @author Priyanshu Guha
 */
public class Tag {
    private final int id;
    private String name;
    private TagCategory category;
    
    /**
     * Make a new tag.
     * 
     * @param id is the id of the tag.
     * @param name is the name of the tag.
     * @param category is the type of tag.
     */
    public Tag(int id, String name, TagCategory category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }
    
    /**
     * Make a new tag. Set the tag category to {@code TagCategory.OTHER} if no category was
     * specified.
     * 
     * @param id is the id of the tag.
     * @param name is the name of the tag.
     */
    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
        this.category = TagCategory.OTHER;
    }
    
    // =======
    // Getters
    // =======
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public TagCategory getCategory() {
        return category;
    }
    
    // =======
    // Setters
    // =======
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCategory(TagCategory category) {
        this.category = category;
    }
}