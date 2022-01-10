package com.breakoutms.timetable.model.beans;

import com.breakoutms.timetable.model.Matrix;
import com.breakoutms.timetable.model.Properties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class IndexedItem implements Serializable {

	@Serial
	private static final long serialVersionUID = -729516261736319255L;
	private int timeIndex;
	
	public int row() {
		return Matrix.row(timeIndex, Properties.totalSessions());
	}
	
	public int col() {
		return Matrix.column(timeIndex, row(), Properties.totalSessions());
	}
}
