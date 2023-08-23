package com.breakoutms.timetable.db;

import com.breakoutms.timetable.model.beans.Lecturer;
import com.breakoutms.timetable.model.beans.Venue;

import static com.breakoutms.timetable.model.beans.Venue.VenueType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VenueDAO extends DAO<Venue> {

    public VenueDAO() {
        super(Venue.class);
    }

    public List<Venue> facultyAll() {
        var list = Arrays.asList(
                new Venue("MM1", MULTIMEDIA_ROOM),
                new Venue("MM2", MULTIMEDIA_ROOM),
                new Venue("MM3", MULTIMEDIA_ROOM),
                new Venue("MM4", MULTIMEDIA_ROOM),
                new Venue("MM5", MULTIMEDIA_ROOM),
                new Venue("MM6", MULTIMEDIA_ROOM),
                new Venue("MM7", MULTIMEDIA_ROOM),
                new Venue("Room 1", CLASS_ROOM),
                new Venue("Hall 6", LECTURE_HALL),
                new Venue("Net Lab", NET_LAB),
                new Venue("Workshop", WORK_SHOP));
        return new ArrayList<Venue>(list);
    }

    @Override
    public List<Venue> all() {
        List<Venue> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            list.add(new Venue("Room " + i, CLASS_ROOM));
        }
        for (int i = 1; i <= 7; i++) {
            list.add(new Venue("MM" + i, MULTIMEDIA_ROOM));
        }
        for (int i = 1; i <= 12; i++) {
            list.add(new Venue("Hall " + i, LECTURE_HALL));
        }

        list.add(new Venue("Photo Lab", PHOTO_LAB));
        list.add(new Venue("Mac Lab", MAC_LAB));
        list.add(new Venue("Net Lab", NET_LAB));
        list.add(new Venue("Workshop", WORK_SHOP));
        list.add(new Venue("No Venue", OTHER));
        list.add(new Venue("Sound Studio", OTHER));
        list.add(new Venue("TV Studio", OTHER));
        list.add(new Venue("MM9", OTHER));
        list.add(new Venue("MM10", OTHER));
        list.add(new Venue("MP Campus", OTHER));
        list.add(new Venue("?", OTHER));

        return list;
    }
}
