package com.wisetaskadmin.entities;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Scope("prototype")
@NoArgsConstructor
@Data
public class TasksToDo {
	
	@Autowired
	@Qualifier("tasksToDoList")
	private List<TaskToDo> tasksToDo;

}
