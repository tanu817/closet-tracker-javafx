package com.mycompany.irr00_group_project.model;

import java.time.LocalDate;

/**
 * Class for recording the events in which an outfit was worn.
 *
 * @author Priyanshu Guha
 */
public class WearEvent {

    private int outfitId;
    private LocalDate wornDate;

    /**
     * Create a WearEvent.
     *
     * @param outfitId is the ID of the outfit worn for this wearEvent.
     * @param wornDate is the date when the outfit was worn.
     */
    public WearEvent(int outfitId, LocalDate wornDate) {
        this.outfitId = outfitId;
        this.wornDate = wornDate;
    }

    // =======
    // Getters
    // =======
    public int getOutfitId() {
        return outfitId;
    }

    public LocalDate getWornDate() {
        return wornDate;
    }
}
