package com.breakoutms.timetable.model.beans;

import com.breakoutms.timetable.model.Properties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Slot implements Serializable {

	@Serial
	private static final long serialVersionUID = -7553597255929332094L;
	private LecturerIndex lecturerIndex;
	private StudentClassIndex studentClassIndex;
	private AllocatedVenue allocatedVenue;
	private Course course;
	
	public Integer getTimeIndex() {
		if(lecturerIndex.getTimeIndex() == studentClassIndex.getTimeIndex()
				&& lecturerIndex.getTimeIndex() == allocatedVenue.getTimeIndex()) {
			return lecturerIndex.getTimeIndex();
		}
		throw new IllegalStateException("TimeIndex not the same for "
				+ "lecturer, studentClass and venue");
	}
	
	@Override
	public String toString() {
		return  course.getName() + " ["+ lecturerIndex +" + "+ studentClassIndex +" + "+allocatedVenue+"] @ "+ getTimeIndex();
	}

	public int col() {
		return lecturerIndex.col();
	}

	public int row() {
		return lecturerIndex.row();
	}

	public String getStudentClassName(){
		return studentClassIndex.getName();
	}

	public StudentClass getStudentClass() {
		return studentClassIndex.getStudentClass();
	}

	public Lecturer getLecturer() {
		return lecturerIndex.getLecturer();
	}

	public String getLecturerName() {
		return lecturerIndex.getName();
	}

	public String getVenueName(){
		return allocatedVenue.getName();
	}

	public String getTime(){
		String day = Properties.dayLabel(row()+1);
		String time = Properties.sessionLabel(col()+1);
		return String.format("%s (%s)", day, time);
	}
}
