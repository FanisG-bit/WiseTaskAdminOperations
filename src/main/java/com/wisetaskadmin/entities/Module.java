package com.wisetaskadmin.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity(name = "modules")
@Table(name = "modules")
public class Module {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "module_id")
	private int moduleId;
	
	@Column(name = "module_name")
	private String moduleName;
	
	@Column(name = "module_code")
	private String moduleCode;
	
	@Column(name = "primary_lecturer")
	private String primaryLecturer;
	
	@Column(name = "moderator_lecturer")
	private String moderatorLecturer;
	
	@Column(name = "curriculum")
	private String curriculum;
	
	/*
	 * In the files within the entities package, when it comes to jpa relationships
	 * like @OneToMany etc. I configure 'cascade' based on a guide found here ->
	 * https://www.baeldung.com/hibernate-unsaved-transient-instance-error That way,
	 * we avoid errors regarding "Object References an Unsaved Transient Instance".
	 */
	
	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "foreignModuleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Assessment> assessments;

	public void addAssessment(Assessment assessment) {
		assessments.add(assessment);
	}
	
	@JsonIgnore
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_belongsTo_entry", referencedColumnName = "entry_id")
	private Entry foreighEntryId = new Entry();
	
}