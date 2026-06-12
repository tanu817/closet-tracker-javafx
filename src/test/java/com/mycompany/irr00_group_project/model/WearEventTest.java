package com.mycompany.irr00_group_project.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link WearEvent}.
 *
 * @author Alexandra Varapai
 */
public class WearEventTest {

    @Test
    @DisplayName("Constructor stores outfit ID and wornDate correctly")
    void constructorStoresFields() {
        LocalDate date = LocalDate.of(2026, 6, 12);
        WearEvent event = new WearEvent(7, date);
        assertEquals(7, event.getOutfitId());
        assertEquals(date, event.getWornDate());
    }

    @Test
    @DisplayName("getWornDate() returns the exact date passed in")
    void getWornDate() {
        LocalDate today = LocalDate.now();
        WearEvent event = new WearEvent(1, today);
        assertEquals(today, event.getWornDate());
    }

    @Test
    @DisplayName("Different outfit IDs produce different events")
    void differentOutfitIds() {
        LocalDate date = LocalDate.of(2024, 6, 1);
        WearEvent e1 = new WearEvent(1, date);
        WearEvent e2 = new WearEvent(2, date);
        assertNotEquals(e1.getOutfitId(), e2.getOutfitId());
    }
}
