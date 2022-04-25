package com.wisetaskadmin.entities;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Scope("prototype")
@Entity
@Table(name = "tasks")
@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "task_id")
	private int taskId;
	
	@Column(name = "task_description")
	private String taskDescription;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "date_to_send")
	private LocalDate dateToSend;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "isCompleted")
	private boolean isCompleted;
	
	/*
	 * This field was only added for the purpose of not creating a new entity. When
	 * retrieving the tasks that needs to be sent each day, we get them as an object
	 * Tasks, which includes a list of type Task, as a field. Having that in mind we
	 * haven't created the table in a way that we store the email, since there is no
	 * need (because all the tables are connected with each other because of the
	 * relationships that they have). The easiest solution for keeping that approach
	 * is to add this attribute which will only serve that purpose (we are also not
	 * annotating it as a column because we are not changing our table's fields).
	 */
	@JsonInclude
	@Transient
	private String emailAddressToSend;
	
	/*
	 * For the sake of convenience we are also doing the same as above, for the
	 * field below.
	 */
	
	@JsonInclude
	@Transient
	private String timeToSendEmail;
	
	/*
	 * By having the cascade in the ManyToOne relationship as "MERGE", we fix an
	 * error with message: "org.hibernate.PersistentObjectException: detached entity
	 * passed to persist" Solution found ->
	 * https://stackoverflow.com/questions/6378526/org-hibernate-
	 * persistentobjectexception-detached-entity-passed-to-persist
	 */	
	@JsonIgnore
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
	@JoinColumn(name = "task_belongs_to_assessment", referencedColumnName = "assessment_id")
	private Assessment taskBelongsToAssessment;
	
}
