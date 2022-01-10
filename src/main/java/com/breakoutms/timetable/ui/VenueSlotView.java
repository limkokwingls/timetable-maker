package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.model.beans.Slot;

public class VenueSlotView extends SlotView{

	public VenueSlotView(Slot slot) {
		super(slot);
	}

	@Override
	protected void setProperties(Slot slot) {
		topLabel.setText(slot.getCourse().toString());
		leftLabel.setText(slot.getLecturerIndex().getName());
		rightLabel.setText(slot.getStudentClassIndex().getName());
	}
}