package com.breakoutms.timetable.model.beans;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentClassIndex extends IndexedItem {

    @Serial
    private static final long serialVersionUID = -3617741378351469302L;
    private StudentClass studentClass;

    public String getName() {
        return studentClass != null? studentClass.getName() : "Null studentClass";
    }

    @Override
    public String toString() {
        return getName();
    }
}
