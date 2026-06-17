package com.mycompany.irr00_group_project.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Outfit}.
 *
 * @author Alexandra Varapai
 */
public class OutfitTest {

    private ClothingItem makeItem(int id, String name, ClothingCategory cat) {
        return new ClothingItem(id, name, cat);
    }

    @Test
    @DisplayName("Constructor with name stores id and name")
    void constructorWithName() {
        Outfit outfit = new Outfit(1, "Summer Look");
        assertEquals(1, outfit.getId());
        assertEquals("Summer Look", outfit.getName());
    }

    @Test
    @DisplayName("Constructor with items auto-generates name from item names")
    void constructorAutoName() {
        Set<ClothingItem> items = new HashSet<>();
        items.add(makeItem(1, "Shirt", ClothingCategory.TOP));
        items.add(makeItem(2, "Jeans", ClothingCategory.BOTTOM));
        Outfit outfit = new Outfit(10, items);
        String name = outfit.getName();
        assertTrue(name.contains("Shirt") || name.contains("Jeans"),
                "Auto-generated name should contain item names");
    }

    @Test
    @DisplayName("rename() updates the outfit name")
    void rename() {
        Outfit outfit = new Outfit(1, "Shirt");
        outfit.rename("Shorts");
        assertEquals("Shorts", outfit.getName());
    }

    @Test
    @DisplayName("addItem() adds a clothing item to the outfit")
    void addItem() {
        Outfit outfit = new Outfit(1, "Look");
        ClothingItem item = makeItem(5, "Blazer", ClothingCategory.TOP);
        outfit.addItem(item);
        outfit.removeItem(item);
    }

    @Test
    @DisplayName("removeItem() removes an item previously added")
    void removeItem() {
        Set<ClothingItem> items = new HashSet<>();
        ClothingItem top = makeItem(1, "Shirt", ClothingCategory.TOP);
        ClothingItem bot = makeItem(2, "Jeans", ClothingCategory.BOTTOM);
        items.add(top);
        items.add(bot);
        Outfit outfit = new Outfit(1, "Casual", items);
        outfit.removeItem(top);
        outfit.removeItem(bot);
    }

    @Test
    @DisplayName("Constructor (id, name, items) stores name and id")
    void constructorStoresnameAndID() {
        Set<ClothingItem> items = new HashSet<>();
        items.add(makeItem(1, "Shirt", ClothingCategory.TOP));
        Outfit outfit = new Outfit(42, "Evening", items);
        assertEquals(42, outfit.getId());
        assertEquals("Evening", outfit.getName());
    }
}
