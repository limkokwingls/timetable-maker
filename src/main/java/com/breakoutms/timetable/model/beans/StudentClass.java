package com.breakoutms.timetable.model.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serial;

@Data @Entity
@NoArgsConstructor
public class StudentClass extends IndexedItem {

	@Serial
	private static final long serialVersionUID = -3168713782397130748L;
	@Id @GeneratedValue
	private Integer id;

	@Column(unique = true)
	@NotBlank
	private String name;
	public StudentClass(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
