package com.breakoutms.timetable.model.beans;

import java.io.Serial;

public class VenueIndex extends IndexedItem {

    @Serial
    private static final long serialVersionUID = -2216880802834248301L;
    private Venue venue;

    @Override
    public String toString() {
        return venue.getName();
    }
}
