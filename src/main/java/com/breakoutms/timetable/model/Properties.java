package com.breakoutms.timetable.model;


import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Properties {

	public static int totalSessions() {
		return 4;
	}

	public static int totalDays() {
		return 6;
	}
	
	public static int totalTimeSlots() {
		return (totalSessions() * totalDays());
	}

	public static String sessionLabel(int col){
		String[] labels = {"08:00 - 10:00", "10:00 - 12:00", "12:00 - 14:00", "14:00 - 16:00"};
		// String[] labels = {"08:30 - 11:30", "11:30 - 14:30", "14:30 - 17:30"};
		return labels[col-1];
	}

	public static List<String> allSessionLabels() {
		List<String> list = new ArrayList<>();
		for (int i = 1; i <= totalSessions(); i++) {
			list.add(sessionLabel(i));
		}
		return list;
	}

	public static String dayLabel(int row){
		DayOfWeek day = DayOfWeek.of(row);
		return day.getDisplayName(TextStyle.FULL, Locale.US);
	}

	public static List<String> allDayLabels(){
		List<String> list = new ArrayList<>();
		for (int i = 1; i <= totalDays(); i++) {
			list.add(dayLabel(i));
		}
		return list;
	}

}
