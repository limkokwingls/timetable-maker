package com.breakoutms.timetable.model.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Course implements Serializable {

	@Serial
	private static final long serialVersionUID = 6037994522154635957L;
	@Id @GeneratedValue
	private Integer id;

	@NotBlank
	private String name;

	@Column(unique = true)
	@NotBlank
	private String code;

	public Course(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Course(String courseName) {
		String extractedCode = null;
		if(courseName.contains("(") && courseName.contains(")")){
			int start = courseName.lastIndexOf("(");
			int end = courseName.lastIndexOf(")");
			extractedCode = courseName.substring(start+1, end);
			if(extractedCode.matches(".*\\d.*")){
				this.code = extractedCode.trim();
				this.name = courseName.substring(0, start).trim();
			}
		}
		if(this.code == null){
			this.name = courseName.trim();
		}
	}

	public String toString() {
		if(StringUtils.isNoneBlank(code)){
			return String.format("%s (%s)", name, code);
		}
		return name;
	}
}
