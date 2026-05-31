package com.mycompany.irr00_group_project.model;

import java.util.Date;

/**
 * Class for recording the events in which an outfit was worn.
 *
 * @author Priyanshu Guha
 */
public class WearEvent {
    private Outfit outfitId;
    private Date wornDate;  
    
    /**
     * Create a WearEvent.
     * 
     * @param outfitId is the ID of the outfit worn.
     * @param wornDate is the date when the outfit was worn.
     */
    public WearEvent(Outfit outfitId, Date wornDate) {
        this.outfitId = outfitId;
        this.wornDate = wornDate;
    }
}
