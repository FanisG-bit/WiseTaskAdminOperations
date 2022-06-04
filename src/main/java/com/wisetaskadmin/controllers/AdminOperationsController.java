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

/**
 * The rest controller (resource class) that contains url-endpoints for all the operations
 * that can be performed by user of account type ADMIN. 
 * @author Theofanis Gkoufas
 *
 */
@RestController
@CrossOrigin(origins = {"http://localhost:8025", "http://localhost:8027", "http://localhost:8028"})
@RequestMapping(value = "/admin")
public class AdminOperationsController {

	@Autowired
	private AdminServices adminServices;
	
	@Autowired
	ApplicationContext context;
	
	/**
	 * Returns all the entries that have been created by an admin.
	 * @param id The id (meaning the corresponding primary key) of the admin user.
	 * @return An Entries instance which has a list of Entry objects.
	 */
	@RequestMapping(value = "/{id}/entries", method = RequestMethod.GET)
	public Entries entries(@PathVariable int id) {
		return adminServices.getEntries(id);
	}
	
	/**
	 * Checks whether an entry name is unique.
	 * @param details Is expected to consist of two elements; "userPK" and "newEntryName".
	 * Retrieving a Map is a nice way for getting multiple values at once that may even
	 * be of different data type (hence why the second argument in the generic of the 
	 * Map is declared as Object).
	 * @return
	 */
	@RequestMapping(value = "/isEntryNameUnique", method = RequestMethod.POST)
	public Map<String, Boolean> isEntryNameUnique(@RequestBody Map<String, Object> details) {
		return adminServices.isEntryNameUnique(details) 
				? Collections.singletonMap("unique", true)
				: Collections.singletonMap("unique", false);
	}
	
	/**
	 * Creates a new entry that is being uploaded in the database.
	 * @param newEntryRequiredInfo A Map that should consist of two elements; "userPK"
	 * and "newEntryName".
	 */
	@RequestMapping(value = "/entries", method = RequestMethod.POST)
	public void addEntry(@RequestBody Map<String, Object> newEntryRequiredInfo) {
		adminServices.addNewEntry(newEntryRequiredInfo);
	}

	/**
	 * Retrieves the settings that have been set for a particular entry.
	 * @param entryID A Map that should consist of one element; the desired entry's "id".
	 * Retrospectively speaking, it could be changed into a query (url) parameter.
	 * @return A settings instance.
	 */
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
	
	/**
	 * Uploads the settings that are being set by the admin, along with an excel file (whose
	 * data are extracted and stored as well).
	 * Sending an object as well as a file at the same time, was challenging due to the fact
	 * that it needs a specific way for it to work. Basically in order to call this servlet 
	 * (url-endpoint) the body of the request should be send as "form-data", instead of "raw"
	 * (look at postman).
	 * @see https://stackoverflow.com/questions/51938056/spring-boot-upload-form-data-and-file
	 * @param settingsJSON The settings are being retrieved as a JSON string which is then
	 * converted into a Settings object using the ObjectMapper class. This is (I believe) the 
	 * only way to send a JSON object along with a MultipartFile file).
	 * @param file The excel file that should contain the modules and their respective assessments.
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
	
	/**
	 * Retrieves a specific assessment based on its id.
	 * @param id The corresponding primary key of the assessment that we want to get.
	 * @return The assessment.
	 */
	@RequestMapping(value = "/assessments/{id}", method = RequestMethod.GET)
	public Assessment getAssessment(@PathVariable int id) {
		return adminServices.retrieveAssessment(id);
	}
	
	/**
	 * Retrieves the entry id of the entry that contains an assessment with a given id (given as a 
	 * query parameter).
	 * @param assessmentId The id of the assessment that belongs to an entry. That entry's id is 
	 * what is being searched.
	 * @return A singleton map containing the entry's id.
	 */
	@RequestMapping(value = "/getEntryFKOfAssessment/{id}", method = RequestMethod.GET)
	public Map<String, Integer> getEntryFKOfAssessment(@PathVariable(name = "id") int assessmentId) {
		int moduleID = adminServices.getModuleFKOfAssessment(assessmentId);
		return Collections.singletonMap("entryID", adminServices.getEntryFKOfModule(moduleID));
	}
	
	/**
	 * Given the id of a particular assessment, it retrieves the module that contains (conceptually)
	 * that assessment (since the relationship between these tables; modules-assessments
	 * are of type OneToMany-ManyToOne).
	 * @param assessmentID The id of the assessment.
	 * @return The module that contains the assessment with the given id.
	 */
	@RequestMapping(value = "/getModuleOfAssessment/{id}", method = RequestMethod.GET)
	public Module getModuleOfAssessment(@PathVariable(name = "id") int assessmentID) {
		return adminServices.retrieveModule(adminServices.getModuleFKOfAssessment(assessmentID));
	}
	
}
