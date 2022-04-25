package com.wisetaskadmin.entities;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PendingTaskToSet {

	private String moduleName;
	
	private String curriculum;
	
	private String assessmentType;
	
	private String assessmentWeeks;
	
	private int assessmentId;
	
	private Date week1BeginDate;
	
	private int assessmentWeight;
	
}