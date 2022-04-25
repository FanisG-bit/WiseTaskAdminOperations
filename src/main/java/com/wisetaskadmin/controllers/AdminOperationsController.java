package com.wisetaskadmin.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetaskadmin.entities.*;
import com.wisetaskadmin.entities.Module;
import com.wisetaskadmin.services.AdminServices;

@RestController
@CrossOrigin(origins = {"http://localhost:8025", "http://localhost:8027", "http://localhost:8028"})
@RequestMapping(value = "/admin")
public class AdminOperationsController {

	@Autowired
	private AdminServices adminServices;
	
	@Autowired
	ApplicationContext context;
	
	@RequestMapping(value = "/{id}/entries", method = RequestMethod.GET)
	public Entries entries(@PathVariable int id) {
		return adminServices.getEntries(id);
	}
	
	@RequestMapping(value = "/isEntryNameUnique", method = RequestMethod.POST)
	public Map<String, Boolean> isEntryNameUnique(@RequestBody Map<String, Object> details) {
		return adminServices.isEntryNameUnique(details) 
				? Collections.singletonMap("unique", true)
				: Collections.singletonMap("unique", false);
	}
	
	@RequestMapping(value = "/entries", method = RequestMethod.POST)
	public void addEntry(@RequestBody Map<String, Object> newEntryRequiredInfo) {
		adminServices.addNewEntry(newEntryRequiredInfo);
	}

	@RequestMapping(value = "/getSettings", method = RequestMethod.POST)
	public Settings getSettings(@RequestBody Map<String, Integer> entryID) {
		Settings settings;
		try {
			settings = adminServices.getSettings(entryID.get("id"));
		}catch (NoSuchElementException e) {
			settings = null;
		}
		return settings != null ? settings : Settings.builder().settingsId(-1).build();
	}
	
	/*
	 * Sending an object as well as a file at the same time, was challenging due to
	 * the fact that it needs a specific way for it to work, that you only know if
	 * you have implemented a similar thing in the past.
	 * After a lot of search, I found out a solution that worked as expected.
	 * Basically when this web service is called, you need not to specify any headers
	 * and you have to work with "form-data", instead of "raw" (look at postman).
	 * See -> 
	 * https://stackoverflow.com/questions/51938056/spring-boot-upload-form-data-and-file
	 */
	@RequestMapping(value = "/uploadData", method = RequestMethod.POST)
	public void uploadData(@RequestParam(name = "settingsJSON") String settingsJSON, 
						   @RequestParam(value = "excelFile") MultipartFile file) {
		ObjectMapper mapper = context.getBean(ObjectMapper.class);
		Settings settings = null;
		try {
			settings = mapper.readValue(settingsJSON, Settings.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		adminServices.uploadSettings(settings);
		adminServices.extractExcelData(settings, file);
	}
	
	@RequestMapping(value = "/assessments/{id}", method = RequestMethod.GET)
	public Assessment getAssessment(@PathVariable int id) {
		return adminServices.retrieveAssessment(id);
	}
	
	@RequestMapping(value = "/getEntryFKOfAssessment/{id}", method = RequestMethod.GET)
	public Map<String, Integer> getEntryFKOfAssessment(@PathVariable(name = "id") int assessmentId) {
		int moduleID = adminServices.getModuleFKOfAssessment(assessmentId);
		return Collections.singletonMap("entryID", adminServices.getEntryFKOfModule(moduleID));
	}
	
	@RequestMapping(value = "/getModuleOfAssessment/{id}", method = RequestMethod.GET)
	public Module getModuleOfAssessment(@PathVariable(name = "id") int assessmentID) {
		return adminServices.retrieveModule(adminServices.getModuleFKOfAssessment(assessmentID));
	}
	
}
