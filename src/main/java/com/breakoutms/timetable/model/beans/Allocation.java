package com.breakoutms.timetable.model.beans;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Allocation implements Serializable {

	@Serial
	private static final long serialVersionUID = 1923382236795470951L;
	private final Lecturer lecturer;
	private final Course course;
	private final StudentClass studentClass;
	@EqualsAndHashCode.Exclude
	private final Venue.VenueType venueType;
	@EqualsAndHashCode.Exclude
	private Venue venue;
	@EqualsAndHashCode.Exclude
	private boolean strictPreferredTime;
	@EqualsAndHashCode.Exclude
	private Integer timeIndex;
	
	public Allocation(Lecturer lecturer, Course course, StudentClass studentClass, Venue.VenueType venueType) {
		this.lecturer = lecturer;
		this.course = course;
		this.studentClass = studentClass;
		this.venueType = venueType;
	}
	
	public String toString() {
		return lecturer +" ("+ course+" -> "+studentClass+")";
	}
}
