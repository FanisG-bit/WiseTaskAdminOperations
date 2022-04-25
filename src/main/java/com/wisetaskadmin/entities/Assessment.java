package com.wisetaskadmin.entities;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity(name = "assessments")
@Table(name = "assessments")
public class Assessment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "assessment_id")
	private int assessmentId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "assessment_type")
	private AssessmentType assessmentType;
	
	@Column(name = "assessment_weight")
	private int assessmentWeight;
	
	@Column(name = "assessment_weeks")
	private String weeks;
	
	@Column(name = "assessment_upload_date")
	private LocalDate uploadDate;
	
	@Column(name = "assessment_deadline_date")
	private LocalDate deadlineDate;
	
	@JsonIgnore
	@ToString.Exclude //this fixes the exception 'stackoverflow' which is related to recursive calls
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "assessment_belongsTo_module", referencedColumnName = "module_id")
	private Module foreignModuleId = new Module();
	
	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "taskBelongsToAssessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Task> tasks;
}