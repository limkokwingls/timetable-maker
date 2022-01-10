package com.breakoutms.timetable.model.beans;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class Project implements Serializable {
    @Serial
    private static final long serialVersionUID = 5187319183816806991L;
    public static final transient Project INSTANCE = new Project();

    private final Set<Slot> slots = new HashSet<>();
    private String name;

    private Project(){}
}
