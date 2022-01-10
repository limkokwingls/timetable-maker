package com.breakoutms.timetable.model.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venue implements Serializable {

	@Serial
	private static final long serialVersionUID = -7113201442414720571L;

	public enum VenueType {
		ANY,
		MULTIMEDIA_ROOM,
		CLASS_ROOM,
		LECTURE_HALL,
		NET_LAB,
		WORK_SHOP,
		PHOTO_LAB,
		MAC_LAB
	}
	
	@Id @GeneratedValue
	private int id;

	@Column(unique = true)
	@NotBlank
	private String name;

	/* TODO: VanueType should be converted to a class so that a vanue
	types can be provided by the user */
	private VenueType venueType;

	public Venue(String name, VenueType venueType) {
		this.name = name;
		this.venueType = venueType;
	}

	@Override
	public String toString() {
		return name;
	}
}
