package com.breakoutms.timetable.model.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllocatedVenue extends IndexedItem {

	@Serial
	private static final long serialVersionUID = -4942667264988515081L;
	private Venue venue;
	
	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return venue != null? venue.getName() : "Null venue";
	}
}
