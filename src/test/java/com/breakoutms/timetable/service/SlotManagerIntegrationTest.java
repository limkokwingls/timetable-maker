package com.breakoutms.timetable.service;

import com.breakoutms.timetable.model.Properties;
import com.breakoutms.timetable.model.beans.*;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SlotManagerIntegrationTest {

    private final SlotManager slotManager = new SlotManager();

    @Test
    void creates_unique_slots(){
        List<Integer> slotIndexes = new ArrayList<>();
        for(int i = 0; i < Properties.totalTimeSlots(); i++){
            Allocation allocation = new Allocation(new Lecturer("mr", "lecturer"),
                    new Course("course", "course"),
                    new StudentClass("class"), Venue.VenueType.ANY);
            var slot = slotManager.allocate(allocation);
            assertThat(slotIndexes).doesNotContain(slot.getTimeIndex());
            slotIndexes.add(slot.getTimeIndex());
        }
    }

    @Test
    void allocates_venues_using_preferred_venue_types(){
        var venueType = Venue.VenueType.CLASS_ROOM;
        for(int i = 0; i < Properties.totalTimeSlots(); i++){
            Allocation allocation = new Allocation(new Lecturer("Mr","lecturer"),
                    new Course("course","course"),
                    new StudentClass("class"), venueType);
            var allocatedVenue = slotManager.allocate(allocation).getAllocatedVenue();
            assertThat(allocatedVenue.getVenue().getVenueType()).isEqualTo(venueType);
        }
    }
}