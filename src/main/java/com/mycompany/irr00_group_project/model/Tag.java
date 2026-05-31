package com.mycompany.irr00_group_project.model;

/**
 * Class for tags for clothing items.
 * 
 * @author Priyanshu Guha
 */
public class Tag {
    private String name;
    private TagCategory category;
    
    /**
     * Make a new tag.
     * 
     * @param name is the name of the tag.
     * @param category is the type of tag.
     */
    public Tag(String name, TagCategory category) {
        this.name = name;
        this.category = category;
    }
    
    /**
     * Make a new tag. Set the tag category to {@code TagCategory.OTHER} if no category was
     * specified.
     * 
     * @param name is the name of the tag.
     */
    public Tag(String name) {
        this.name = name;
        this.category = TagCategory.OTHER;
    }
}