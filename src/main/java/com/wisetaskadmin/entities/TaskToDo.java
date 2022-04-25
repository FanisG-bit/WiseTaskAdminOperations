package com.wisetaskadmin.entities;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class TaskToDo {
	
	private int taskId;

	private String taskDescription;
	
	private LocalDate dateToSend;
	
	private String moduleName;
	
	private String curriculum;
	
	private String assessmentType;
	
}
