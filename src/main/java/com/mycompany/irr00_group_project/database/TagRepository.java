package com.mycompany.irr00_group_project.database;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.mycompany.irr00_group_project.model.Tag;
import com.mycompany.irr00_group_project.model.TagCategory;

/**
 * Repository for {@link Tag} objects persisted in {@code tags.csv} and the
 * clothing–tag relationship table {@code clothing_tags.csv}.
 *
 * <p>This class is purely I/O: it reads and writes CSV files and performs
 * no business logic.</p>
 *
 * <h2>CSV schemas</h2>
 * <pre>
 *   tags.csv           – id,name,category
 *   clothing_tags.csv  – clothingId,tagId
 * </pre>
 *
 * <h2>ID is the single source of truth</h2>
 * <p>Tag IDs are stable integer keys. All relationships are stored by ID.
 * Tag names are only for display and are resolved via
 * {@link #getTagById(int)} or {@link #getTagIdByName(String)}.</p>
 *
 * @author Aleksandra Levenkova
 * @author Arshiya Shaik
 */
public class TagRepository {

    private static final String TAGS_FILE         = "tags.csv";
    private static final String CLOTHING_TAGS_FILE = "clothing_tags.csv";

    /**
     * Appends a new tag to {@code tags.csv}.
     *
     * @param tag the tag to persist
     */
    public void addTag(Tag tag) {
        try (FileWriter writer = new FileWriter(TAGS_FILE, true)) {
            writer.write(tag.getId() + ","
                    + escapeCsv(tag.getName()) + ","
                    + tag.getCategory() + "\n");
        } catch (IOException e) {
            System.err.println("TagRepository.addTag: " + e.getMessage());
        }
    }

    /**
     * Generates the next available tag ID (max existing ID + 1).
     *
     * @return next available ID
     */
    public int generateNextId() {
        int maxId = 0;
        for (Tag tag : getAllTags()) {
            if (tag.getId() > maxId) {
                maxId = tag.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Returns all tags stored in {@code tags.csv}.
     *
     * @return list of all {@link Tag} objects; empty list on error
     */
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TAGS_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 3) {
                    continue; // malformed row – skip
                }
                int id = Integer.parseInt(values[0].trim());
                String name = values[1].trim();
                TagCategory category = TagCategory.valueOf(values[2].trim());
                tags.add(new Tag(id, name, category));
            }
        } catch (IOException e) {
            System.err.println("TagRepository.getAllTags: " + e.getMessage());
        }
        return tags;
    }

    /**
     * Looks up a tag by its ID.
     *
     * @param id the tag ID to find
     * @return the matching {@link Tag}, or {@code null} if not found
     */
    public Tag getTagById(int id) {
        for (Tag tag : getAllTags()) {
            if (tag.getId() == id) {
                return tag;
            }
        }
        return null;
    }

    /**
     * Looks up the ID of a tag by its name (case-insensitive).
     *
     * @param name the tag name to search for
     * @return the tag ID, or {@code -1} if no tag with that name exists
     */
    public int getTagIdByName(String name) {
        for (Tag tag : getAllTags()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag.getId();
            }
        }
        return -1;
    }

    /**
     * Overwrites {@code tags.csv} with the supplied list.
     *
     * @param tags the complete list of tags to persist
     */
    public void saveTags(List<Tag> tags) {
        try (FileWriter writer = new FileWriter(TAGS_FILE)) {
            writer.write("id,name,category\n");
            for (Tag tag : tags) {
                writer.write(tag.getId() + ","
                        + escapeCsv(tag.getName()) + ","
                        + tag.getCategory() + "\n");
            }
        } catch (IOException e) {
            System.err.println("TagRepository.saveTags: " + e.getMessage());
        }
    }

    /**
     * Updates a single tag record identified by its ID.
     *
     * @param tag the tag with updated values
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
     * Deletes a tag by ID and removes all its relationships from
     * {@code clothing_tags.csv}.
     *
     * @param id the ID of the tag to delete
     */
    public void deleteTag(int id) {
        List<Tag> tags = getAllTags();
        tags.removeIf(tag -> tag.getId() == id);
        saveTags(tags);
        deleteTagRelationships(id);
    }

    // =========================================
    // Clothing–Tag relationship (clothing_tags.csv)
    // =========================================

    /**
     * Persists the association between a clothing item and a tag.
     * Does nothing if the relationship already exists (prevents duplicates).
     *
     * @param clothingId the ID of the clothing item
     * @param tagId      the ID of the tag to associate
     */
    public void addTagToItem(int clothingId, int tagId) {
        // Guard against duplicate entries
        List<Integer> existing = getTagIdsOfItem(clothingId);
        if (existing.contains(tagId)) {
            return;
        }
        try (FileWriter writer = new FileWriter(CLOTHING_TAGS_FILE, true)) {
            writer.write(clothingId + "," + tagId + "\n");
        } catch (IOException e) {
            System.err.println("TagRepository.addTagToItem: " + e.getMessage());
        }
    }

    /**
     * Removes the association between a clothing item and a specific tag.
     * Cleans up the relationship row from {@code clothing_tags.csv}.
     *
     * @param clothingId the ID of the clothing item
     * @param tagId      the ID of the tag to disassociate
     */
    public void removeTagFromItem(int clothingId, int tagId) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLOTHING_TAGS_FILE))) {
            String header = reader.readLine();
            if (header != null) {
                rows.add(header);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 2) {
                    continue;
                }
                int currentClothingId = Integer.parseInt(values[0].trim());
                int currentTagId      = Integer.parseInt(values[1].trim());
                // Keep the row unless it matches the pair we want to remove
                if (!(currentClothingId == clothingId && currentTagId == tagId)) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("TagRepository.removeTagFromItem: " + e.getMessage());
            return;
        }
        writeRows(CLOTHING_TAGS_FILE, rows);
    }

    /**
     * Returns the IDs of all tags associated with a given clothing item.
     *
     * @param clothingId the ID of the clothing item
     * @return list of tag IDs; empty list when the item has no tags
     */
    public List<Integer> getTagIdsOfItem(int clothingId) {
        List<Integer> tagIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLOTHING_TAGS_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 2) {
                    continue;
                }
                int currentItemId = Integer.parseInt(values[0].trim());
                int tagId         = Integer.parseInt(values[1].trim());
                if (currentItemId == clothingId) {
                    tagIds.add(tagId);
                }
            }
        } catch (IOException e) {
            System.err.println("TagRepository.getTagIdsOfItem: " + e.getMessage());
        }
        return tagIds;
    }

    /**
     * Returns the fully resolved {@link Tag} objects for a given clothing item.
     * Tags whose IDs no longer exist in {@code tags.csv} are silently skipped
     * (orphan-safe).
     *
     * @param clothingId the ID of the clothing item
     * @return list of resolved {@link Tag} objects
     */
    public List<Tag> getTagsOfItem(int clothingId) {
        List<Integer> ids = getTagIdsOfItem(clothingId);
        List<Tag> result  = new ArrayList<>();
        for (int id : ids) {
            Tag tag = getTagById(id);
            if (tag != null) {
                result.add(tag);
            }
        }
        return result;
    }

    // ==================
    // Private helpers
    // ==================

    /**
     * Removes all {@code clothing_tags.csv} rows that reference the given
     * {@code tagId} (called when a tag is deleted).
     *
     * @param tagId the tag ID whose relationships should be removed
     */
    private void deleteTagRelationships(int tagId) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLOTHING_TAGS_FILE))) {
            String header = reader.readLine();
            if (header != null) {
                rows.add(header);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 2) {
                    continue;
                }
                int currentTagId = Integer.parseInt(values[1].trim());
                if (currentTagId != tagId) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("TagRepository.deleteTagRelationships: " + e.getMessage());
            return;
        }
        writeRows(CLOTHING_TAGS_FILE, rows);
    }

    /**
     * Atomically rewrites a CSV file with the supplied rows (header included).
     *
     * @param fileName destination file path
     * @param rows     all rows (including header as first element)
     */
    private void writeRows(String fileName, List<String> rows) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String row : rows) {
                writer.write(row + "\n");
            }
        } catch (IOException e) {
            System.err.println("TagRepository.writeRows(" + fileName + "): " + e.getMessage());
        }
    }

    /**
     * Wraps a field value in double-quotes if it contains a comma, a
     * double-quote, or a newline — minimal RFC 4180 escaping.
     *
     * @param value raw field value
     * @return safe CSV field value
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}