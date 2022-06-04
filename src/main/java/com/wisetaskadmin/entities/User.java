package com.wisetaskadmin.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * An entity class representing User.
 * @author Theofanis Gkoufas
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Table(name = "users")
public class User {
	
	@Id
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "username")
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "password")
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "user_type")
	private UserType userType;
	
	@Column(name = "email")
	private String email;
	
	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Entry> entries;
	
	public void addToEntries(Entry entry) {
		entries.add(entry);
	}
	
}