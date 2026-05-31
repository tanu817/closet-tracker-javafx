package com.mycompany.irr00_group_project.model;

/**
 * Class for tags for clothing items.
 * 
 * Tags are defined by a name and have a category as defined in the enum TagCategory.
 * 
 * @author Priyanshu Guha
 */
public class Tag {
    private String name;
    private TagCategory category;
    private User user; // Tags are also user-specific.
    
    /**
     * Make a new tag.
     * 
     * @param name is the name of the tag.
     * @param category is the type of tag.
     */
    public Tag(String name, TagCategory category, User user) {
        this.name = name;
        this.category = category;
        this.user = user;
    }
    
    /**
     * Make a new tag. Set the tag category to {@code TagCategory.OTHER} if no category was
     * specified.
     * 
     * @param name is the name of the tag.
     */
    public Tag(String name, User user) {
        this.name = name;
        this.category = TagCategory.OTHER;
        this.user = user;
    }
    
    // =======
    // Getters
    // =======
    
    public String getName() {
        return name;
    }
    
    public TagCategory getCategory() {
        return category;
    }
    
    public User getUser() {
        return user;
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