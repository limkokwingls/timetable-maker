package com.breakoutms.timetable.model;

public interface Matrix {

	public static int row(int index, int size) {
		return index / size;
	}
	
	public static int column(int index, int row, int size) {
		return index - (row * size);
	}
	
	public static int index(int column, int row, int size) {
		return (row * size) + column;
	}
}
