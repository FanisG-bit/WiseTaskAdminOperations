package com.wisetaskadmin;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;
import com.wisetaskadmin.entities.Entry;
import com.wisetaskadmin.entities.PendingTaskToSet;
import com.wisetaskadmin.entities.Task;
import com.wisetaskadmin.entities.TaskToDo;
import com.wisetaskadmin.services.ExcelDataExtractor;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Configuration
public class AppConfig {

	@Bean("dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dmds = new DriverManagerDataSource();
		dmds.setUsername("newuser");
		dmds.setPassword("9876543!@");
		dmds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dmds.setUrl("jdbc:mysql://localhost:3306/catmms");
		return dmds;
	}
	
	@Bean("entriesList")
	@Scope("prototype")
	public List<Entry> getEntriesList() {
		List<Entry> list = new ArrayList<Entry>();
		return list;
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean("excelDataExtractor")
	public ExcelDataExtractor getExcelDataExtractor() {
		ExcelDataExtractor extractor = new ExcelDataExtractor();
		extractor.setWorkbook(new XSSFWorkbook());
		return extractor;
	}

	@Bean
	public ServletFileUpload getServletFileUpload() {
		ServletFileUpload upload = new ServletFileUpload(getDiskFileItemFactory());
		return upload;
	}

	@Bean
	public DiskFileItemFactory getDiskFileItemFactory() {
		return new DiskFileItemFactory();
	}
	
	@Bean("pendingTasksList")
	@Scope("prototype")
	public List<PendingTaskToSet> getPendingTaskToSetList() {
		return new ArrayList<PendingTaskToSet>();
	}
	
	@Bean("tasksList")
	@Scope("prototype")
	public List<Task> getTasksList() {
		return new ArrayList<Task>();
	}
	
	@Bean("stringList")
	@Scope("prototype")
	public List<String> getStringList() {
		return new ArrayList<String>();
	}
	
	@Bean
	@Scope("prototype")
	public Timer getTimer() {
		return new Timer();
	}
	
	@Bean("tasksToDoList")
	@Scope("prototype")
	public List<TaskToDo> getTasksToDoList() {
		return new ArrayList<TaskToDo>();
	}
	
}
