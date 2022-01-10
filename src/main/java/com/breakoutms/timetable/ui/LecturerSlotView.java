package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.model.beans.Slot;

public class LecturerSlotView extends SlotView{

	public LecturerSlotView(Slot slot) {
		super(slot);
	}

	@Override
	protected void setProperties(Slot slot) {
		topLabel.setText(slot.getCourse().toString());
		leftLabel.setText(slot.getStudentClassIndex().getName());
		rightLabel.setText(slot.getAllocatedVenue().getName());
	}
	
}
