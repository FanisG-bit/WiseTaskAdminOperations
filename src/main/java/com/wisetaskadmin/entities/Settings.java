package com.wisetaskadmin.entities;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * An entity class representing Settings.
 * @author Theofanis Gkoufas
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "settings")
@Table(name = "settings")
@Builder
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "settings_id")
	private int settingsId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "week1_begin_date")
	private Date week1BeginDate;
	
	@Column(name = "time_to_send_notif")
	private String timeToSendNotif;
	
	@Column(name = "assessedL_step1_preDaysUntilReady")
	private int assessedLStep1PreDaysUntilReady;
	
	@Column(name = "assessedL_step2_preModSend")
	private int assessedLStep2PreModSend;
	
	@Column(name = "assessedL_step3_postCorrectionDays")
	private int assessedLStep3PostCorrectionDays;
	
	@Column(name = "assessedL_step4_postModSend")
	private int assessedLStep4PostModSend;
	
	@Column(name = "assessedL_step5_gradesUpload")
	private int assessedLStep5GradesUpload;
	
	@Column(name = "debate_step1_preDaysUntilReady")
	private int debateStep1PreDaysUntilReady;
	
	@Column(name = "debate_step2_preModSend")
	private int debateStep2PreModSend;
	
	@Column(name = "debate_step3_postCorrectionDays")
	private int debateStep3PostCorrectionDays;
	
	@Column(name = "debate_step4_postModSend")
	private int debateStep4PostModSend;
	
	@Column(name = "debate_step5_gradesUpload")
	private int debateStep5GradesUpload;
	
	@Column(name = "demoT_step1_preDaysUntilReady")
	private int demoTStep1PreDaysUntilReady;
	
	@Column(name = "demoT_step2_preModSend")
	private int demoTStep2PreModSend;
	
	@Column(name = "demoT_step3_postCorrectionDays")
	private int demoTStep3PostCorrectionDays;
	
	@Column(name = "demoT_step4_postModSend")
	private int demoTStep4PostModSend;
	
	@Column(name = "demoT_step5_gradesUpload")
	private int demoTStep5GradesUpload;
	
	@Column(name = "finalExam_step1_preDaysUntilReady")
	private int finalExamStep1PreDaysUntilReady;
	
	@Column(name = "finalExam_step2_preModSend")
	private int finalExamStep2PreModSend;
	
	@Column(name = "finalExam_step3_postCorrectionDays")
	private int finalExamStep3PostCorrectionDays;
	
	@Column(name = "finalExam_step4_postModSend")
	private int finalExamStep4PostModSend;
	
	@Column(name = "finalExam_step5_gradesUpload")
	private int finalExamStep5GradesUpload;
	
	@Column(name = "oralExam_step1_preDaysUntilReady")
	private int oralExamStep1PreDaysUntilReady;
	
	@Column(name = "oralExam_step2_preModSend")
	private int oralExamStep2PreModSend;
	
	@Column(name = "oralExam_step3_postCorrectionDays")
	private int oralExamStep3PostCorrectionDays;
	
	@Column(name = "oralExam_step4_postModSend")
	private int oralExamStep4PostModSend;
	
	@Column(name = "oralExam_step5_gradesUpload")
	private int oralExamStep5GradesUpload;
	
	@Column(name = "portfolio_step1_preDaysUntilReady")
	private int portfolioStep1PreDaysUntilReady;
	
	@Column(name = "portfolio_step2_preModSend")
	private int portfolioStep2PreModSend;
	
	@Column(name = "portfolio_step3_postCorrectionDays")
	private int portfolioStep3PostCorrectionDays;
	
	@Column(name = "portfolio_step4_postModSend")
	private int portfolioStep4PostModSend;
	
	@Column(name = "portfolio_step5_gradesUpload")
	private int portfolioStep5GradesUpload;
	
	@Column(name = "presentation_step1_preDaysUntilReady")
	private int presentationStep1PreDaysUntilReady;
	
	@Column(name = "presentation_step2_preModSend")
	private int presentationStep2PreModSend;
	
	@Column(name = "presentation_step3_postCorrectionDays")
	private int presentationStep3PostCorrectionDays;
	
	@Column(name = "presentation_step4_postModSend")
	private int presentationStep4PostModSend;
	
	@Column(name = "presentation_step5_gradesUpload")
	private int presentationStep5GradesUpload;
	
	@Column(name = "project_step1_preDaysUntilReady")
	private int projectStep1PreDaysUntilReady;
	
	@Column(name = "project_step2_preModSend")
	private int projectStep2PreModSend;
	
	@Column(name = "project_step3_postCorrectionDays")
	private int projectStep3PostCorrectionDays;
	
	@Column(name = "project_step4_postModSend")
	private int projectStep4PostModSend;
	
	@Column(name = "project_step5_gradesUpload")
	private int projectStep5GradesUpload;
	
	@Column(name = "quiz_step1_preDaysUntilReady")
	private int quizStep1PreDaysUntilReady;
	
	@Column(name = "quiz_step2_preModSend")
	private int quizStep2PreModSend;
	
	@Column(name = "quiz_step3_postCorrectionDays")
	private int quizStep3PostCorrectionDays;
	
	@Column(name = "quiz_step4_postModSend")
	private int quizStep4PostModSend;
	
	@Column(name = "quiz_step5_gradesUpload")
	private int quizStep5GradesUpload;
	
	@Column(name = "report_step1_preDaysUntilReady")
	private int reportStep1PreDaysUntilReady;
	
	@Column(name = "report_step2_preModSend")
	private int reportStep2PreModSend;
	
	@Column(name = "report_step3_postCorrectionDays")
	private int reportStep3PostCorrectionDays;
	
	@Column(name = "report_step4_postModSend")
	private int reportStep4PostModSend;
	
	@Column(name = "report_step5_gradesUpload")
	private int reportStep5GradesUpload;
	
	@Column(name = "selfReflect_step1_preDaysUntilReady")
	private int selfReflectStep1PreDaysUntilReady;
	
	@Column(name = "selfReflect_step2_preModSend")
	private int selfReflectStep2PreModSend;
	
	@Column(name = "selfReflect_step3_postCorrectionDays")
	private int selfReflectStep3PostCorrectionDays;
	
	@Column(name = "selfReflect_step4_postModSend")
	private int selfReflectStep4PostModSend;
	
	@Column(name = "selfReflect_step5_gradesUpload")
	private int selfReflectStep5GradesUpload;
	
	@Column(name = "other_step1_preDaysUntilReady")
	private int otherStep1PreDaysUntilReady;
	
	@Column(name = "other_step2_preModSend")
	private int otherStep2PreModSend;
	
	@Column(name = "other_step3_postCorrectionDays")
	private int otherStep3PostCorrectionDays;
	
	@Column(name = "other_step4_postModSend")
	private int otherStep4PostModSend;
	
	@Column(name = "other_step5_gradesUpload")
	private int otherStep5GradesUpload;
	
	@JsonIgnore
	@ToString.Exclude
	@OneToOne(targetEntity = Entry.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "entry_FK", referencedColumnName = "entry_id")
	private Entry entryIdFK;
}