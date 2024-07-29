/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.payroll;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Entity // <1>
public class Employee {

	private @Id @GeneratedValue Long id; // <2>
	private String firstName;
	private String lastName;
	private String description;
	private int jobYears;
	private String email;

	private Employee() {}

	public Employee(String firstName, String lastName, String description, int jobYears, String email) {
		if (!validateArguments(firstName, lastName, description, jobYears, email)) {
			throw new IllegalArgumentException("Invalid arguments");
		}
		this.firstName = firstName;
		this.lastName = lastName;
		this.description = description;
		this.jobYears = jobYears;
		this.email = email;
	}

	private boolean validateArguments(String firstName, String lastName, String description, int jobYears, String email) {
		if (firstName == null || lastName == null || description == null || firstName.trim().isEmpty() || lastName.trim().isEmpty() || description.trim().isEmpty() || jobYears < 0) {
			return false;
		}
		if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return jobYears == employee.jobYears && Objects.equals(id, employee.id) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(description, employee.description) && Objects.equals(email, employee.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, description, jobYears, email);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if (firstName == null || firstName.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid first name");
		} else {
			this.firstName = firstName;
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (lastName == null || lastName.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid last name");
		} else {
			this.lastName = lastName;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null || description.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid description");
		} else {
			this.description = description;
		}
	}

	public int getJobYears() {
		return jobYears;
	}

	public void setJobYears(int jobYears) {
		if (jobYears < 0) {
			throw new IllegalArgumentException("Invalid job years");
		} else {
			this.jobYears = jobYears;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
			throw new IllegalArgumentException("Invalid email");
		} else {
			this.email = email;
		}
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", description='" + description + '\'' +
				", jobYears=" + jobYears +
				", email='" + email + '\'' +
				'}';
	}
}
// end::code[]
