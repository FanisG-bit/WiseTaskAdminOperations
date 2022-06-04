package com.wisetaskadmin.entities;

import java.util.Date;
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
 * An entity class representing Entry.
 * @author Theofanis Gkoufas
 * 
 * For this class and the Settings class, I replaced Lombok's Data annotation with Setter, Getter
 * etc. because for some unexplained reason it kept producing a "java.lang.StackOverflowError"
 * even though I have the @ToString.Exclude on all the required attributes (which in every 
 * other case, it is enough to fix the problem).
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity(name = "entries")
@Table(name = "entries")
@AllArgsConstructor
@Builder
public class Entry { 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "entry_id")
	private int entryId;
	
	@Column(name = "entry_name")
	private String entryName;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User userId = new User();
	
	@JsonIgnore
	@ToString.Exclude
	// initially it was fetch = FetchType.LAZY but it was changed 
	@OneToMany(mappedBy = "foreighEntryId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Module> modules;
	
	public void addModule(Module module) {
		modules.add(module);
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_created")
	private Date dateCreated;
	
	@JsonIgnore
	@ToString.Exclude
	@OneToOne(mappedBy = "entryIdFK")
	private Settings settings;
	
}