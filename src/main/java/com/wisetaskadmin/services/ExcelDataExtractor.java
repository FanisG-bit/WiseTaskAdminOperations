package com.wisetaskadmin.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.wisetaskadmin.entities.*;
import com.wisetaskadmin.entities.Module;

/**
 * The class that is responsible for extracting the data of an excel file.
 * @author Theofanis Gkoufas
 *
 */
@Data
@NoArgsConstructor
public class ExcelDataExtractor {

	/* A reference to the workbook that we are using for the extraction of the data.
	   We use this object for the purpose of scanning the each excel tab, get each 
	   tab's name etc.
	*/
	private XSSFWorkbook workbook;
	
	/**
	 * Extracts all the data in regards to the modules, given the tab that should be
	 * scanned.
	 * @param tab The tab that contains the data that should be extracted.
	 * @return A list containing all the modules that were extracted from the tab.
	 */
	public List<Module> getModulesList(int tab) {
		List<Module> list = new ArrayList<>();
		XSSFSheet customerSheet = workbook.getSheetAt(tab);
		Iterator<Row> rowIterator = customerSheet.iterator();
		while (rowIterator.hasNext()) {
			Module module = new Module();
			Row currentRow = rowIterator.next();
			Cell cell0 = currentRow.getCell(0);
			if (cell0 != null) {
				int cellType = cell0.getCellType();
				if (cellType == Cell.CELL_TYPE_STRING) {
					if (cell0.getStringCellValue().contains("-") && cell0.getStringCellValue().length() >= 3) {
						String moduleNameAndCode = cell0.getStringCellValue();
						String[] moduleNameAndCodeSplit = moduleNameAndCode.split("-");
						module.setCurriculum(workbook.getSheetName(tab));
						module.setModuleName(moduleNameAndCodeSplit[0]);
						module.setModuleCode(moduleNameAndCodeSplit[1]);
						Row nextRow = customerSheet.getRow((int) currentRow.getRowNum() + 1);
						if (nextRow.getCell(0).getStringCellValue().contains(",")) {
							String teachers = nextRow.getCell(0).getStringCellValue();
							String[] teachersSplit = teachers.split(",");
							module.setPrimaryLecturer(teachersSplit[0]);
							module.setModeratorLecturer(teachersSplit[1]);
						} else {
							String teacher = nextRow.getCell(0).getStringCellValue();
							module.setPrimaryLecturer(teacher);
						}
						list.add(module);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Extracts the assessments from a specific excel tab, and maps them to the corresponding modules.
	 * This operation should take place after the process of extracting the modules from the tab. That 
	 * way we extract the assessments and map them to their respective modules.
	 * @param modules A list containing all the modules that were extracted on the same tab.
	 * @param entry The entry that "owns" all the modules and the assessments that are being extracted.
	 * @param tab The tab that we wish to scan for the purpose of extracting its data.
	 * @return A list containing the modules with their matched assessments.
	 */
	public List<Module> getModulesWithAssessmentsMapped(List<Module> modules, Entry entry, int tab) {
		List<Assessment> list = new ArrayList<>();
		XSSFSheet customerSheet = workbook.getSheetAt(tab);
		Iterator<Row> rowIterator = customerSheet.iterator();
		int moduleCounter = -1;
		Module module = null;
		boolean isModuleComplete = false;
		while (rowIterator.hasNext()) {
			Assessment assessment = new Assessment();
			Row currentRow = rowIterator.next();
			Cell cell1 = currentRow.getCell(1);
			boolean condition1 = false;
			boolean condition2 = false;
			boolean condition3 = false;
			Cell cell0 = currentRow.getCell(0);
			if(cell0 != null) {
				if (cell0.getCellType() == Cell.CELL_TYPE_STRING
						&& cell0.getStringCellValue().equals("#")) {
					isModuleComplete = true;
				}
			}
			if (isModuleComplete) {
				try {
					module = modules.get(++moduleCounter);
					// we add the entry id (of the entry that these modules 
					// belong to) as a foreign key on each module
					module.setForeighEntryId(entry);
					module.setAssessments(new ArrayList<>());
				}catch(IndexOutOfBoundsException e) {}
				isModuleComplete = false;
			}
			if (cell1 != null) {
				int cellType = cell1.getCellType();
				if (cellType == Cell.CELL_TYPE_NUMERIC) {
					assessment.setAssessmentWeight((int) (cell1.getNumericCellValue() * 100));
					condition1 = true;
				}
			}
			Cell cell3 = currentRow.getCell(3);
			if (cell3 != null) {
				int cellType = cell3.getCellType();
				if (cellType == Cell.CELL_TYPE_STRING) {
					if (cell3.getStringCellValue().length() <= 15
							&& !cell3.getStringCellValue().equals("Assessment Type")) {
						AssessmentType type = mapAssessmentType(cell3.getStringCellValue());
						assessment.setAssessmentType(type);
						condition2 = true;
					}
				}
			}
			String weeks = new String();
			if (currentRow.getCell(0) != null) {
				if (currentRow.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
					for (int column = 5; column <= 21; column++) {
						Cell cell = currentRow.getCell(column);
						if (cell != null) {
							if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
								weeks += "1"; 
								condition3 = true;
							} else {
								weeks += "0";
							}
						}
					}
				}
			}
			if (condition1 && condition2 && condition3) {
				assessment.setWeeks(weeks);
				// extremely important!! we always need to pass a reference to the @ManyToOne field
				assessment.setForeignModuleId(module);
				list.add(assessment);
				try {
					module.addAssessment(assessment);
				}catch(NullPointerException e) {}
			}
		}
		return modules;
	}

	/**
	 * Given the type of the assessment in String data type, it returns the corresponding
	 * enum value.
	 * @param assessmentString The assessment type.
	 * @return The enum value that matches the given "assessmentString".
	 */
	public AssessmentType mapAssessmentType(String assessmentString) {
		AssessmentType type = null;
		if (assessmentString.equalsIgnoreCase("Assessed Lab")) {
			type = AssessmentType.ASSESSED_LAB;
		}
		else if (assessmentString.equalsIgnoreCase("Debate")) {
			type = AssessmentType.DEBATE;
		}
		else if (assessmentString.equalsIgnoreCase("Demo Teaching")) {
			type = AssessmentType.DEMO_TEACHING;
		}
		else if (assessmentString.equalsIgnoreCase("Final Exam")) {
			type = AssessmentType.FINAL_EXAM;
		}
		else if (assessmentString.equalsIgnoreCase("Oral examination")) {
			type = AssessmentType.ORAL_EXAMINATION;
		}
		else if (assessmentString.equalsIgnoreCase("Portfolio")) {
			type = AssessmentType.PORTFOLIO;
		}
		else if (assessmentString.equalsIgnoreCase("Presentation")) {
			type = AssessmentType.PRESENTATION;
		}
		else if (assessmentString.equalsIgnoreCase("Project")) {
			type = AssessmentType.PROJECT;
		}
		else if (assessmentString.equalsIgnoreCase("Quiz")) {
			type = AssessmentType.QUIZ;
		}
		else if (assessmentString.equalsIgnoreCase("Report")) {
			type = AssessmentType.REPORT;
		}
		else if (assessmentString.equalsIgnoreCase("Self-reflection")) {
			type = AssessmentType.SELF_REFLECTION;
		} 
		else {
			type = AssessmentType.OTHER;
		}
		return type;
	}
	
}
