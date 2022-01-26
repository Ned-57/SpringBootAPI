package com.qa.user_app.data.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

//hibernate requires annotation of domain entities with @Entity
@Entity
//optional but allows our own sql files be used
@Table(name = "user")
public class User {

	@Id // required if @Entity present, signifies PK field.
	@GeneratedValue(strategy = GenerationType.IDENTITY) // tells hibernate to autoincrement IDs
	private Long id;

	@NotNull // validation
	@Length(min = 1, message = "Type in a name")
	private String forename;

	@NotNull
	@Length(min = 1)
	private String surname;

	@Max(130) // validation, (business logic, adults only)
	@Min(18)
	private Integer age;

	// used by hibernate (jpa)
	public User() {
		super();
	}

	public User(String forename, String surname, Integer age) {
		super();
		this.forename = forename;
		this.surname = surname;
		this.age = age;
	}

	public User(long id, String forename, String surname, Integer age) {
		super();
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", forename=" + forename + ", surname=" + surname + ", age=" + age + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(age, forename, id, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(age, other.age) && Objects.equals(forename, other.forename)
				&& Objects.equals(id, other.id) && Objects.equals(surname, other.surname);
	}

}
