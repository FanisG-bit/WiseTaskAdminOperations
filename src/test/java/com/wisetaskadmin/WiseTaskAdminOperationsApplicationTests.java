package com.wisetaskadmin;

import org.junit.jupiter.api.Assertions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.wisetaskadmin.entities.*;
import com.wisetaskadmin.entities.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisetaskadmin.controllers.AdminOperationsController;
import com.wisetaskadmin.services.AdminServices;
import static org.hamcrest.Matchers.*;

/**
 * Test class used for the purpose of testing a number of methods that are being provided
 * in the REST API.
 * @author Theofanis Gkoufas
 *
 */
@WebMvcTest(controllers = AdminOperationsController.class)
class WiseTaskAdminOperationsApplicationTests {
	
	@MockBean
	private AdminServices adminServices;
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void testGetEntriesBasedOnUserId() throws Exception {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Mockito.when(adminServices.getEntries(1)).thenReturn(Entries.builder()
			.list(Arrays.asList((Entry.builder()
			.entryId(162)
			.entryName("Spring Semester 2022")
			.userId(User.builder().userId(0).build())
			.dateCreated(sdf.parse("2022-04-26")).build())
			)).build());
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/admin/1/entries"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.list[0].entryId").value(162))
					.andExpect(MockMvcResultMatchers.jsonPath("$.list[0].entryName").value("Spring Semester 2022"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.list[0].userId.userId").value(0))
					.andExpect(MockMvcResultMatchers.jsonPath("$.list[0].dateCreated").value("2022-04-25T21:00:00.000+00:00")).andReturn();
		Assertions.assertEquals("application/json", response.getResponse().getContentType());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testIsEntryNameUnique() throws Exception {
		Mockito.when(adminServices.isEntryNameUnique(new HashMap<String, Object>()
			{{put("userPK", 1);put("entry_name", "New Entry Name");
			}})).thenReturn(true);
		Map<String, Object> requestBody = new HashMap<String, Object>() 
										  {{put("userPK", 1);put("entry_name", "New Entry Name");}};
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/admin/isEntryNameUnique")
				.content(new ObjectMapper().writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.unique").value(true))
				.andReturn();
		Assertions.assertEquals("application/json", response.getResponse().getContentType());
	}
	
	@Test
	public void testGetAssessment() throws Exception {
		Assessment assessment = new Assessment();
		assessment.setAssessmentId(1);
		assessment.setAssessmentType(AssessmentType.ASSESSED_LAB);
		assessment.setAssessmentWeight(35);
		assessment.setWeeks("00010000000000000");
		assessment.setUploadDate(null);
		assessment.setDeadlineDate(null);
		Mockito.when(adminServices.retrieveAssessment(1)).thenReturn(assessment);
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/admin/assessments/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.assessmentId").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.assessmentType").value("ASSESSED_LAB"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.assessmentWeight").value(35))
				.andReturn();
		Assertions.assertEquals("application/json", response.getResponse().getContentType());
	}
	
	@Test
	public void testGetModuleOfAssessment() throws Exception {
		int assessmentId = 25;
		int moduleId = 1;
		Module module = new Module();
		module.setModuleId(moduleId);
		module.setModuleName("Continuous and Agile Software Engineering");
		module.setModuleCode("CCP6418");
		module.setPrimaryLecturer("Dr Varsamidis");
		module.setModeratorLecturer(null);
		module.setCurriculum("WM");
		Mockito.when(adminServices.getModuleFKOfAssessment(assessmentId)).thenReturn(moduleId);
		Mockito.when(adminServices.retrieveModule(moduleId)).thenReturn(module);
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/admin/getModuleOfAssessment/25"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.moduleId").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.primaryLecturer").value("Dr Varsamidis"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(6)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(6)))
				.andReturn();
		/*
		 	$.length() and $.* are two of the ways for counting the number of fields in an object.
		*/
		Assertions.assertEquals("application/json", response.getResponse().getContentType());
	}
	
}
