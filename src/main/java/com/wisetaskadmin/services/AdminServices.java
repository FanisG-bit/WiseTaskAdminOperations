package com.wisetaskadmin.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.wisetaskadmin.entities.*;
import com.wisetaskadmin.entities.Module;
import com.wisetaskadmin.repositories.AssessmentsRepository;
import com.wisetaskadmin.repositories.EntriesRepository;
import com.wisetaskadmin.repositories.ModulesRepository;
import com.wisetaskadmin.repositories.SettingsRepository;
import java.util.Date;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AdminServices {

	@Autowired
	EntriesRepository entriesRepository;
	
	@Autowired
	SettingsRepository settingsRepository;
	
	@Autowired
	ModulesRepository modulesRepository;
	
	@Autowired
	AssessmentsRepository assessmentsRepository;
	
	@Autowired
	ApplicationContext context;
	
	public Entries getEntries(int userPK) {
		DataSource ds = context.getBean("dataSource", DataSource.class);
		Entries entries = context.getBean(Entries.class);
		Connection connection;
		Statement statement;
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM entries WHERE entries.user_id = "
					+ userPK + " ORDER BY date_created DESC, entry_id DESC");
			while(resultSet.next()) {
				entries.getList().add(Entry.builder()
									.entryId(resultSet.getInt(1))
									.entryName(resultSet.getString(2))
									.dateCreated(resultSet.getDate(4))
									.build());
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entries;
	}
	
	public boolean isEntryNameUnique(Map<String, Object> details) {
		DataSource ds = context.getBean("dataSource", DataSource.class);
		Connection connection;
		Statement statement;
		boolean isUnique = true;
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery
					("SELECT entry_name FROM entries WHERE entries.user_id = " + details.get("userPK"));
			List<String> entryNames = new ArrayList<String>();
			while(resultSet.next()) {
				entryNames.add(resultSet.getString("entry_name"));
			}
			for(String s : entryNames) {
				if(s.equals(details.get("newEntryName"))) {
					isUnique = false;
					break;
				}
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUnique;
	}
	
	public void addNewEntry(Map<String, Object> newEntryRequiredInfo) {
		User user = requestUser((int) newEntryRequiredInfo.get("userPK"));
		DataSource ds = context.getBean("dataSource", DataSource.class);
		Connection connection;
		Statement statement;
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			Entry newEntry = Entry.builder()
					.entryName((String)newEntryRequiredInfo.get("newEntryName"))
					.userId(user)
					.dateCreated(new Date())
					.build();
			entriesRepository.save(newEntry);
			Settings entrySettings = Settings.builder().build();
			entrySettings.setEntryIdFK(newEntry);
			settingsRepository.save(entrySettings);
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private User requestUser(int id) {
		RestTemplate restTemplate = context.getBean(RestTemplate.class);
		User user = restTemplate.getForObject("http://localhost:8028/users/" + id, User.class);
		return user;
	}
	
	public Settings getSettings(int entryID) throws NoSuchElementException {
		DataSource ds = context.getBean("dataSource", DataSource.class);
		Connection connection;
		Statement statement;
		Settings settings = null;
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM settings WHERE entry_FK =" + entryID);
			while(resultSet.next()) {
				settings = Settings.builder()
						.settingsId(resultSet.getInt(1))
						.week1BeginDate(resultSet.getDate(2))
						.timeToSendNotif(resultSet.getString(3))
						.assessedLStep1PreDaysUntilReady(resultSet.getInt(4))
						.assessedLStep2PreModSend(resultSet.getInt(5))
						.assessedLStep3PostCorrectionDays(resultSet.getInt(6))
						.assessedLStep4PostModSend(resultSet.getInt(7))
						.assessedLStep5GradesUpload(resultSet.getInt(8))
						.debateStep1PreDaysUntilReady(resultSet.getInt(9))
						.debateStep2PreModSend(resultSet.getInt(10))
						.debateStep3PostCorrectionDays(resultSet.getInt(11))
						.debateStep4PostModSend(resultSet.getInt(12))
						.debateStep5GradesUpload(resultSet.getInt(13))
						.demoTStep1PreDaysUntilReady(resultSet.getInt(14))
						.demoTStep2PreModSend(resultSet.getInt(15))
						.demoTStep3PostCorrectionDays(resultSet.getInt(16))
						.demoTStep4PostModSend(resultSet.getInt(17))
						.demoTStep5GradesUpload(resultSet.getInt(18))
						.finalExamStep1PreDaysUntilReady(resultSet.getInt(19))
						.finalExamStep2PreModSend(resultSet.getInt(20))
						.finalExamStep3PostCorrectionDays(resultSet.getInt(21))
						.finalExamStep4PostModSend(resultSet.getInt(22))
						.finalExamStep5GradesUpload(resultSet.getInt(23))
						.oralExamStep1PreDaysUntilReady(resultSet.getInt(24))
						.oralExamStep2PreModSend(resultSet.getInt(25))
						.oralExamStep3PostCorrectionDays(resultSet.getInt(26))
						.oralExamStep4PostModSend(resultSet.getInt(27))
						.oralExamStep5GradesUpload(resultSet.getInt(28))
						.portfolioStep1PreDaysUntilReady(resultSet.getInt(29))
						.portfolioStep2PreModSend(resultSet.getInt(30))
						.portfolioStep3PostCorrectionDays(resultSet.getInt(31))
						.portfolioStep4PostModSend(resultSet.getInt(32))
						.portfolioStep5GradesUpload(resultSet.getInt(33))
						.presentationStep1PreDaysUntilReady(resultSet.getInt(34))
						.presentationStep2PreModSend(resultSet.getInt(35))
						.presentationStep3PostCorrectionDays(resultSet.getInt(36))
						.presentationStep4PostModSend(resultSet.getInt(37))
						.presentationStep5GradesUpload(resultSet.getInt(38))
						.projectStep1PreDaysUntilReady(resultSet.getInt(39))
						.projectStep2PreModSend(resultSet.getInt(40))
						.projectStep3PostCorrectionDays(resultSet.getInt(41))
						.projectStep4PostModSend(resultSet.getInt(42))
						.projectStep5GradesUpload(resultSet.getInt(43))
						.quizStep1PreDaysUntilReady(resultSet.getInt(44))
						.quizStep2PreModSend(resultSet.getInt(45))
						.quizStep3PostCorrectionDays(resultSet.getInt(46))
						.quizStep4PostModSend(resultSet.getInt(47))
						.quizStep5GradesUpload(resultSet.getInt(48))
						.reportStep1PreDaysUntilReady(resultSet.getInt(49))
						.reportStep2PreModSend(resultSet.getInt(50))
						.reportStep3PostCorrectionDays(resultSet.getInt(51))
						.reportStep4PostModSend(resultSet.getInt(52))
						.reportStep5GradesUpload(resultSet.getInt(53))
						.selfReflectStep1PreDaysUntilReady(resultSet.getInt(54))
						.selfReflectStep2PreModSend(resultSet.getInt(55))
						.selfReflectStep3PostCorrectionDays(resultSet.getInt(56))
						.selfReflectStep4PostModSend(resultSet.getInt(57))
						.selfReflectStep5GradesUpload(resultSet.getInt(58))
						.otherStep1PreDaysUntilReady(resultSet.getInt(59))
						.otherStep2PreModSend(resultSet.getInt(60))
						.otherStep3PostCorrectionDays(resultSet.getInt(61))
						.otherStep4PostModSend(resultSet.getInt(62))
						.otherStep5GradesUpload(resultSet.getInt(63))
						.entryIdFK(entriesRepository.findById(entryID).get())
						.build();
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return settings;
	}
	
	/*
		For some reason when updating a settings record using JPA (save method) the foreign key,
		deletes itself (reminder that a foreign key already exists on the settings records prior
		to this operation). That is why I use vanilla SQL knowing for sure that it works without
		deleting the foreign key which is essential.
		An idea for why this is happening may have to do with the fact that the individual
		mapping on the rest controller that is calling this method should be changed regarding
		the 'method', meaning from POST to PUT. That is because based on the rest architecture, 
		operations about update should be denoted as PUT (whereas insert operations as POST). 
	*/
	public void uploadSettings(Settings settings) {
		
		String date = settings.getWeek1BeginDate().toString();
		String[] words = date.split(" ");
		String[] months = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", 
				"Dec"};
		int monthPrior = Arrays.asList(months).indexOf(words[1]);
		int month = ++monthPrior;
		String monthStr = String.valueOf(month);
		String day = words[2];
		String year = words[5];
		String newdate = year + "-" + monthStr + "-" + day;
		DataSource ds = context.getBean("dataSource", DataSource.class);
		Connection connection;
		Statement statement;
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate("UPDATE settings"
								  + " SET week1_begin_date = '" +  newdate
								  + "', time_to_send_notif = '" +  settings.getTimeToSendNotif()
								  + "', assessedL_step1_preDaysUntilReady = " +  settings.getAssessedLStep1PreDaysUntilReady()
								  + ", assessedL_step2_preModSend = " +  settings.getAssessedLStep2PreModSend()
								  + ", assessedL_step3_postCorrectionDays = " +  settings.getAssessedLStep3PostCorrectionDays()
								  + ", assessedL_step4_postModSend = " +  settings.getAssessedLStep4PostModSend()
								  + ", assessedL_step5_gradesUpload = " +  settings.getAssessedLStep5GradesUpload()
								  + ", debate_step1_preDaysUntilReady = " +  settings.getDebateStep1PreDaysUntilReady()
								  + ", debate_step2_preModSend = " +  settings.getDebateStep2PreModSend()
								  + ", debate_step3_postCorrectionDays = " +  settings.getDebateStep3PostCorrectionDays()
								  + ", debate_step4_postModSend = " +  settings.getDebateStep4PostModSend()
								  + ", debate_step5_gradesUpload = " +  settings.getDebateStep5GradesUpload()
								  + ", demoT_step1_preDaysUntilReady = " +  settings.getDemoTStep1PreDaysUntilReady()
								  + ", demoT_step2_preModSend = " +  settings.getDemoTStep2PreModSend()
								  + ", demoT_step3_postCorrectionDays = " +  settings.getDemoTStep3PostCorrectionDays()
								  + ", demoT_step4_postModSend = " +  settings.getDemoTStep4PostModSend()
								  + ", demoT_step5_gradesUpload = " +  settings.getDemoTStep5GradesUpload()
								  + ", finalExam_step1_preDaysUntilReady = " +  settings.getFinalExamStep1PreDaysUntilReady()
								  + ", finalExam_step2_preModSend = " +  settings.getFinalExamStep2PreModSend()
								  + ", finalExam_step3_postCorrectionDays = " +  settings.getFinalExamStep3PostCorrectionDays()
								  + ", finalExam_step4_postModSend = " +  settings.getFinalExamStep4PostModSend()
								  + ", finalExam_step5_gradesUpload = " +  settings.getFinalExamStep5GradesUpload()
								  + ", oralExam_step1_preDaysUntilReady = " +  settings.getOralExamStep1PreDaysUntilReady()
								  + ", oralExam_step2_preModSend = " +  settings.getOralExamStep2PreModSend()
								  + ", oralExam_step3_postCorrectionDays = " +  settings.getOralExamStep3PostCorrectionDays()
								  + ", oralExam_step4_postModSend = " +  settings.getOralExamStep4PostModSend()
								  + ", oralExam_step5_gradesUpload = " +  settings.getOralExamStep5GradesUpload()
								  + ", portfolio_step1_preDaysUntilReady = " +  settings.getPortfolioStep1PreDaysUntilReady()
								  + ", portfolio_step2_preModSend = " +  settings.getPortfolioStep2PreModSend()
								  + ", portfolio_step3_postCorrectionDays = " +  settings.getPortfolioStep3PostCorrectionDays()
								  + ", portfolio_step4_postModSend = " +  settings.getPortfolioStep4PostModSend()
								  + ", portfolio_step5_gradesUpload = " +  settings.getPortfolioStep5GradesUpload()
								  + ", presentation_step1_preDaysUntilReady = " +  settings.getPresentationStep1PreDaysUntilReady()
								  + ", presentation_step2_preModSend = " +  settings.getPresentationStep2PreModSend()
								  + ", presentation_step3_postCorrectionDays = " +  settings.getPresentationStep3PostCorrectionDays()
								  + ", presentation_step4_postModSend = " +  settings.getPresentationStep4PostModSend()
								  + ", presentation_step5_gradesUpload = " +  settings.getPresentationStep5GradesUpload()
								  + ", project_step1_preDaysUntilReady = " +  settings.getProjectStep1PreDaysUntilReady()
								  + ", project_step2_preModSend = " +  settings.getProjectStep2PreModSend()
								  + ", project_step3_postCorrectionDays = " +  settings.getProjectStep3PostCorrectionDays()
								  + ", project_step4_postModSend = " +  settings.getProjectStep4PostModSend()
								  + ", project_step5_gradesUpload = " +  settings.getProjectStep5GradesUpload()
								  + ", quiz_step1_preDaysUntilReady = " +  settings.getQuizStep1PreDaysUntilReady()
								  + ", quiz_step2_preModSend = " +  settings.getQuizStep2PreModSend()
								  + ", quiz_step3_postCorrectionDays = " +  settings.getQuizStep3PostCorrectionDays()
								  + ", quiz_step4_postModSend = " +  settings.getQuizStep4PostModSend()
								  + ", quiz_step5_gradesUpload = " +  settings.getQuizStep5GradesUpload()
								  + ", report_step1_preDaysUntilReady = " +  settings.getReportStep1PreDaysUntilReady()
								  + ", report_step2_preModSend = " +  settings.getReportStep2PreModSend()
								  + ", report_step3_postCorrectionDays = " +  settings.getReportStep3PostCorrectionDays()
								  + ", report_step4_postModSend = " +  settings.getReportStep4PostModSend()
								  + ", report_step5_gradesUpload = " +  settings.getReportStep5GradesUpload()
								  + ", selfReflect_step1_preDaysUntilReady = " +  settings.getSelfReflectStep1PreDaysUntilReady()
								  + ", selfReflect_step2_preModSend = " +  settings.getSelfReflectStep2PreModSend()
								  + ", selfReflect_step3_postCorrectionDays = " +  settings.getSelfReflectStep3PostCorrectionDays()
								  + ", selfReflect_step4_postModSend = " +  settings.getSelfReflectStep4PostModSend()
								  + ", selfReflect_step5_gradesUpload = " +  settings.getSelfReflectStep5GradesUpload()
								  + ", other_step1_preDaysUntilReady = " +  settings.getOtherStep1PreDaysUntilReady()
								  + ", other_step2_preModSend = " +  settings.getOtherStep2PreModSend()
								  + ", other_step3_postCorrectionDays = " +  settings.getOtherStep3PostCorrectionDays()
								  + ", other_step4_postModSend = " +  settings.getOtherStep4PostModSend()
								  + ", other_step5_gradesUpload = " +  settings.getOtherStep5GradesUpload()
								  + " WHERE settings_id = " + settings.getSettingsId() + ";");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void extractExcelData(Settings settings, MultipartFile excel) {
		XSSFWorkbook excelWorkBook = null;
		try {
			excelWorkBook = new XSSFWorkbook(excel.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		ExcelDataExtractor dataExtractor = context.getBean("excelDataExtractor", ExcelDataExtractor.class);
		dataExtractor.setWorkbook(excelWorkBook);
		int numberOfSheets = excelWorkBook.getNumberOfSheets();
		List<Integer> sheetsToCheck = new ArrayList<Integer>();
		for(int i=0; i<numberOfSheets; i++) {
			if (excelWorkBook.getSheetName(i).equalsIgnoreCase("instructions")
					|| excelWorkBook.getSheetName(i).equalsIgnoreCase("example")
					|| excelWorkBook.getSheetName(i).equalsIgnoreCase("ranges")) {
				continue;
			} else {
				sheetsToCheck.add(i);
			}
		}
		List<Module> allTabModulesWithAssessments = new ArrayList<Module>();
		Entry entry = getEntryBasedOnID(getEntryIDFK(settings));
		for(Integer tab : sheetsToCheck) {			
			List<Module> modulesList = dataExtractor.getModulesList(tab);
			List<Module> modulesWithAssessmentsMapped = dataExtractor.getModulesWithAssessmentsMapped(modulesList,
					entry, tab);
			allTabModulesWithAssessments.addAll(modulesWithAssessmentsMapped);
		}
		uploadExcelData(allTabModulesWithAssessments);
	}
	
	public int getEntryIDFK(Settings settings) {
		DataSource ds = context.getBean("dataSource", DataSource.class);
		Connection connection;
		Statement statement;
		int entryIDFK = -1;
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet  = statement.executeQuery
				("SELECT entry_FK FROM settings WHERE settings_id = " + settings.getSettingsId());
			while(resultSet.next()) {
				entryIDFK = resultSet.getInt(1);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entryIDFK;
	}
	
	public Entry getEntryBasedOnID(int entryID) {
		return entriesRepository.findById(entryID).get();
	}
	
	public void uploadExcelData(List<Module> allTabModulesWithAssessments) {
		for(Module d : allTabModulesWithAssessments) {
			modulesRepository.save(d);
		}
	}
	
	public Assessment retrieveAssessment(int assessmentID) {
		return assessmentsRepository.findById(assessmentID).get();
	}
	
	public int getModuleFKOfAssessment(int assessmentID) {
		DataSource bean = context.getBean(DataSource.class);
		Connection conn;
		Statement statement;
		int moduleFK = -1;
		try {
			conn = bean.getConnection();
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery
								  ("SELECT assessment_belongsTo_module FROM assessments "
								 + "WHERE assessment_id = " + assessmentID);
			while(resultSet.next()) {
				moduleFK = resultSet.getInt(1);
				break;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return moduleFK;
	}
	
	public int getEntryFKOfModule(int moduleID) {
		DataSource bean = context.getBean(DataSource.class);
		Connection conn;
		Statement statement;
		int entryFK = -1;
		try {
			conn = bean.getConnection();
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery
								  ("SELECT module_belongsTo_entry FROM modules "
								 + "WHERE module_id = " + moduleID);
			while(resultSet.next()) {
				entryFK = resultSet.getInt(1);
				break;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entryFK;
	}
	
	public Module retrieveModule(int moduleID) {
		return modulesRepository.findById(moduleID).get();
	}
	
}