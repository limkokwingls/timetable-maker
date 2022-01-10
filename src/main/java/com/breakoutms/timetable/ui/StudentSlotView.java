package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.model.beans.Slot;

public class StudentSlotView extends SlotView{

	public StudentSlotView(Slot slot) {
		super(slot);
	}

	@Override
	protected void setProperties(Slot slot) {
		topLabel.setText(slot.getCourse().toString());
		leftLabel.setText(slot.getLecturerIndex().getName());
		rightLabel.setText(slot.getAllocatedVenue().getName());
	}
	
}