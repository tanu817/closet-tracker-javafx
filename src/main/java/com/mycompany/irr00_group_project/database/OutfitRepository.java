package com.mycompany.irr00_group_project.database;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import com.mycompany.irr00_group_project.model.Outfit;

/**
 * Repository for {@link Outfit} objects persisted in {@code outfits.csv} and
 * the outfit–item relationship table {@code outfit_items.csv}.
 *
 * <p>
 * This class is purely I/O: it reads and writes CSV files and performs no
 * business logic.</p>
 *
 * <h2>CSV schemas</h2>
 * <pre>
 *   outfits.csv      – id,name
 *   outfit_items.csv – outfitId,clothingId1,clothingId2
 * </pre>
 *
 * @author Aleksandra Levenkova
 * @author Arshiya Shaik
 */
public class OutfitRepository {

    private static final String OUTFITS_FILE = "outfits.csv";
    private static final String OUTFIT_ITEMS_FILE = "outfit_items.csv";

    /**
     * Appends a new outfit to {@code outfits.csv}.
     *
     * @param outfit the outfit to persist
     */
    public void addOutfit(Outfit outfit) {
        try (FileWriter writer = new FileWriter(OUTFITS_FILE, true)) {
            writer.write(outfit.getId() + ","
                    + escapeCsv(outfit.getName()) + "\n");
        } catch (IOException e) {
            System.err.println("OutfitRepository.addOutfit: " + e.getMessage());
        }
    }

    /**
     * Generates the next available outfit ID (max existing ID + 1).
     *
     * @return next available ID
     */
    public int generateNextId() {
        int maxId = 0;
        for (Outfit outfit : getAllOutfits()) {
            if (outfit.getId() > maxId) {
                maxId = outfit.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Returns all outfits stored in {@code outfits.csv}. Items are NOT loaded
     * here; use {@link OutfitService} to hydrate them.
     *
     * @return list of all {@link Outfit} objects; empty list on error
     */
    public List<Outfit> getAllOutfits() {
        List<Outfit> outfits = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(OUTFITS_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                // name may contain commas → split on first comma only
                int comma = line.indexOf(',');
                if (comma < 0) {
                    continue;
                }
                int id = Integer.parseInt(line.substring(0, comma).trim());
                String name = unescapeCsv(line.substring(comma + 1).trim());
                outfits.add(new Outfit(id, name));
            }
        } catch (IOException e) {
            System.err.println("OutfitRepository.getAllOutfits: " + e.getMessage());
        }
        return outfits;
    }

    /**
     * Overwrites {@code outfits.csv} with the supplied list.
     *
     * @param outfits the complete list of outfits to persist
     */
    public void saveOutfits(List<Outfit> outfits) {
        try (FileWriter writer = new FileWriter(OUTFITS_FILE)) {
            writer.write("id,name\n");
            for (Outfit outfit : outfits) {
                writer.write(outfit.getId() + ","
                        + escapeCsv(outfit.getName()) + "\n");
            }
        } catch (IOException e) {
            System.err.println("OutfitRepository.saveOutfits: " + e.getMessage());
        }
    }

    /**
     * Updates a single outfit record identified by its ID.
     *
     * @param outfit the outfit with updated values
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
     * Deletes an outfit by ID and removes its item relationships.
     *
     * @param id the ID of the outfit to delete
     */
    public void deleteOutfit(int id) {
        List<Outfit> outfits = getAllOutfits();
        outfits.removeIf(o -> o.getId() == id);
        saveOutfits(outfits);
        deleteOutfitItems(id);
    }

    /**
     * Persists the two-item composition of an outfit. Schema:
     * outfitId,clothingId1,clothingId2
     *
     * @param outfitId the ID of the outfit
     * @param clothingId1 the ID of the first clothing item (e.g. top)
     * @param clothingId2 the ID of the second clothing item (e.g. bottom)
     */
    public void addItemToOutfit(int outfitId, int clothingId1, int clothingId2) {
        try (FileWriter writer = new FileWriter(OUTFIT_ITEMS_FILE, true)) {
            writer.write(outfitId + "," + clothingId1 + "," + clothingId2 + "\n");
        } catch (IOException e) {
            System.err.println("OutfitRepository.addItemToOutfit: " + e.getMessage());
        }
    }

    /**
     * Returns the IDs of the two clothing items that compose the given outfit.
     *
     * @param outfitId the outfit whose item IDs should be returned
     * @return list of exactly 0 or 2 clothing item IDs
     */
    public List<Integer> getItemIdsForOutfit(int outfitId) {
        List<Integer> itemIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(OUTFIT_ITEMS_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 3) {
                    continue;
                }
                int currentOutfitId = Integer.parseInt(values[0].trim());
                if (currentOutfitId == outfitId) {
                    itemIds.add(Integer.parseInt(values[1].trim()));
                    itemIds.add(Integer.parseInt(values[2].trim()));
                    break; // each outfit has exactly one row
                }
            }
        } catch (IOException e) {
            System.err.println("OutfitRepository.getItemIdsForOutfit: " + e.getMessage());
        }
        return itemIds;
    }

    /**
     * Returns {@code true} if an outfit composed of exactly these two clothing
     * items already exists (order-independent).
     *
     * @param clothingId1 ID of the first item
     * @param clothingId2 ID of the second item
     * @return {@code true} when a duplicate outfit is found
     */
    public boolean outfitExists(int clothingId1, int clothingId2) {
        try (BufferedReader reader = new BufferedReader(new FileReader(OUTFIT_ITEMS_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 3) {
                    continue;
                }
                int a = Integer.parseInt(values[1].trim());
                int b = Integer.parseInt(values[2].trim());
                if ((a == clothingId1 && b == clothingId2)
                        || (a == clothingId2 && b == clothingId1)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("OutfitRepository.outfitExists: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns the IDs of all outfits that contain the given clothing item.
     *
     * @param clothingId the clothing item ID to search for
     * @return list of outfit IDs that contain this item
     */
    public List<Integer> getOutfitIdsContainingItem(int clothingId) {
        List<Integer> outfitIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(OUTFIT_ITEMS_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length < 3) {
                    continue;
                }
                int id1 = Integer.parseInt(values[1].trim());
                int id2 = Integer.parseInt(values[2].trim());
                if (id1 == clothingId || id2 == clothingId) {
                    outfitIds.add(Integer.parseInt(values[0].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("OutfitRepository.getOutfitIdsContainingItem: " + e.getMessage());
        }
        return outfitIds;
    }

    /**
     * Removes all rows in {@code outfit_items.csv} for the given outfit ID.
     *
     * @param outfitId the outfit whose item rows should be removed
     */
    private void deleteOutfitItems(int outfitId) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(OUTFIT_ITEMS_FILE))) {
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
                if (values.length < 1) {
                    continue;
                }
                int currentOutfitId = Integer.parseInt(values[0].trim());
                if (currentOutfitId != outfitId) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("OutfitRepository.deleteOutfitItems: " + e.getMessage());
            return;
        }
        try (FileWriter writer = new FileWriter(OUTFIT_ITEMS_FILE)) {
            for (String row : rows) {
                writer.write(row + "\n");
            }
        } catch (IOException e) {
            System.err.println("OutfitRepository.deleteOutfitItems(write): " + e.getMessage());
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

    /**
     * Strips surrounding double-quotes and un-doubles internal ones.
     *
     * @param value raw CSV field (may be quoted)
     * @return unescaped value
     */
    private String unescapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }
}
