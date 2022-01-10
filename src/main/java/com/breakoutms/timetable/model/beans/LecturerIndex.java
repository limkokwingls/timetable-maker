package com.breakoutms.timetable.model.beans;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class LecturerIndex extends IndexedItem {

    @Serial
    private static final long serialVersionUID = -3790888773882288461L;
    private Lecturer lecturer;

    public String getName(){
        return lecturer != null? lecturer.getName() : "Null lecturer";
    }

    @Override
    public String toString() {
        return getName();
    }
}
