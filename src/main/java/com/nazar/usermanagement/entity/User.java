package com.nazar.usermanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //say it to do smth with hibernate
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Pattern(regexp = "^[a-zA-Z]{3,}$", message = "First name must contain only letters and have at least 3 characters")
	private String firstName;

	@Pattern(regexp = "^[a-zA-Z]{3,}$", message = "Last name must contain only letters and have at least 3 characters")
	@Column(nullable = false)
	private String lastName;

	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
	@Column(nullable = false)
	private String email;

	@Digits(integer = 2, fraction = 0, message = "Age must be a number")
	@Min(value = 5, message = "Age must be at least 5")
	@Column(nullable = false)
	private int age;

	@ManyToOne
	@JoinColumn(name = "role_id" , nullable = false)
	private Role role;

	public void addRole(Role role) {
		this.role = role;
	    role.getUsers().add(this); 
	}
}