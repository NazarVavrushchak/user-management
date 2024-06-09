package com.nazar.usermanagement.entity;

import java.util.HashSet;
import java.util.Set;

import com.nazar.usermanagement.DTO.RoleDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //say it to do smth with hibernate
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
	public void setRole(RoleDTO.RoleType role) {
		this.role = RoleType.valueOf(role.name());
	}

	public void setRole(RoleType role) {
	}

	public enum RoleType {
		STUDENT,
		WORKER,
		RETIRED,
		PUPIL,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;

	@Setter
    @Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RoleType role;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
	private Set<Person> persons = new HashSet<>();

	public void addPerson(Person person) {
		this.persons.add(person);
		person.setRole(this);
	}
}
