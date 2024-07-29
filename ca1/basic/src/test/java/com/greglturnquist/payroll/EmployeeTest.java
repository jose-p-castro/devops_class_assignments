package com.greglturnquist.payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "ring bearer";
        int jobYears = 1;
        String email = "email@gmail.com";
        employee = new Employee(firstName, lastName, description, jobYears, email);
    }

    @Test
    void testEmployee() {
        assertEquals("Frodo", employee.getFirstName());
        assertEquals("Baggins", employee.getLastName());
        assertEquals("ring bearer", employee.getDescription());
        assertEquals(1, employee.getJobYears());
        assertEquals("email@gmail.com", employee.getEmail());
    }

    @Test
    void testEmployeeEquality() {
//        Arrange
        Employee employee1 = new Employee("Frodo", "Baggins", "ring bearer", 1, "email@gmail.com");
        Employee employee2 = new Employee("Frodo", "Baggins", "ring bearer", 1, "email@gmail.com");
//        Act + Assert
        assertEquals(employee1, employee2);
    }

    @Test
    void testEmployeeInequality() {
//        Arrange
        Employee employee1 = new Employee("Frodo", "Baggins", "ring bearer", 1, "email@gmail.com");
        Employee employee2 = new Employee("Frodo", "Baggins", "ring bearer", 2, "email@gmail.com");
//        Act + Assert
        assertNotEquals(employee1, employee2);
    }

    @Test
    void testEmployeeHashCode() {
        assertEquals(-1026807203, employee.hashCode());
    }

    @Test
    void testEmployeeToString() {
        assertEquals("Employee{id=null, firstName='Frodo', lastName='Baggins', description='ring bearer', jobYears=1, email='email@gmail.com'}", employee.toString());
    }

    @Test
    void testEmployeeId() {
//        Arrange
        long newId = 10L;
//        Act
        employee.setId(newId);
//        Assert
        assertEquals(newId, employee.getId());
    }

    @Test
    void testEmployeeFirstName() {
//        Arrange
        String newFirstName = "Sam";
//        Act
        employee.setFirstName(newFirstName);
//        Assert
        assertEquals(newFirstName, employee.getFirstName());
    }

    @Test
    void testEmployeeLastName() {
//        Arrange
        String newLastName = "Gamgee";
//        Act
        employee.setLastName(newLastName);
//        Assert
        assertEquals(newLastName, employee.getLastName());
    }

    @Test
    void testEmployeeDescription() {
//        Arrange
        String newDescription = "gardener";
//        Act
        employee.setDescription(newDescription);
//        Assert
        assertEquals(newDescription, employee.getDescription());
    }

    @Test
    void testEmployeeJobYears() {
//        Arrange
        int newJobYears = 2;
//        Act
        employee.setJobYears(newJobYears);
//        Assert
        assertEquals(newJobYears, employee.getJobYears());
    }

    @Test
    void testEmployeeEmail() {
//        Arrange
        String newEmail = "email@hotmail.com";
//        Act
        employee.setEmail(newEmail);
//        Assert
        assertEquals(newEmail, employee.getEmail());
    }

    @Test
    void testEmployeeNullName() {
//        Arrange
        String firstName = null;
        String lastName = "baggins";
        String description = "ring bearer";
        String email = "email@gmail.com";
        int jobYears = 1;
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeEmptyName() {
//        Arrange
        String firstName = "";
        String lastName = "baggins";
        String description = "ring bearer";
        int jobYears = 1;
        String email = "email@gmail.com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeNullLastName() {
//        Arrange
        String firstName = "frodo";
        String lastName = null;
        String description = "ring bearer";
        int jobYears = 1;
        String email = "email@gmail.com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeEmptyLastName() {
//        Arrange
        String firstName = "frodo";
        String lastName = "";
        String description = "ring bearer";
        int jobYears = 1;
        String email = "email@gmail.com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeNullDescription() {
//        Arrange
        String firstName = "frodo";
        String lastName = "baggins";
        String description = null;
        int jobYears = 1;
        String email = "email@gmail.com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeEmptyDescription() {
//        Arrange
        String firstName = "frodo";
        String lastName = "baggins";
        String description = "";
        int jobYears = 1;
        String email = "email@gmail.com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeNegativeJobYears() {
//        Arrange
        String firstName = "frodo";
        String lastName = "baggins";
        String description = "ring bearer";
        int jobYears = -1;
        String email = "email@gmail.com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeNullEmail() {
//        Arrange
        String firstName = "frodo";
        String lastName = "baggins";
        String description = "ring bearer";
        int jobYears = 1;
        String email = null;
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeEmptyEmail() {
//        Arrange
        String firstName = "frodo";
        String lastName = "baggins";
        String description = "ring bearer";
        int jobYears = 1;
        String email = " ";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testEmployeeSetNullFirstName() {
//        Arrange
        String firstName = null;
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setFirstName(firstName));
    }

    @Test
    void testEmployeeSetEmptyFirstName() {
//        Arrange
        String firstName = "";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setFirstName(firstName));
    }

    @Test
    void testEmployeeSetNullLastName() {
//        Arrange
        String lastName = null;
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setLastName(lastName));
    }

    @Test
    void testEmployeeSetEmptyLastName() {
//        Arrange
        String lastName = "";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setLastName(lastName));
    }

    @Test
    void testEmployeeSetNullDescription() {
//        Arrange
        String description = null;
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setDescription(description));
    }

    @Test
    void testEmployeeSetEmptyDescription() {
//        Arrange
        String description = "";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setDescription(description));
    }

    @Test
    void testEmployeeSetNegativeJobYears() {
//        Arrange
        int jobYears = -1;
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setJobYears(jobYears));
    }

    @Test
    void testEmployeeSetNullEmail() {
//        Arrange
        String email = null;
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setEmail(email));
    }

    @Test
    void testEmployeeSetEmptyEmail() {
//        Arrange
        String email = " ";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setEmail(email));
    }

    @Test
    void testEmployeeSetInvalidEmail() {
//        Arrange
        String email = "emailemail.com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setEmail(email));
    }

    @Test
    void testEmployeeSetInvalidEmailSecondTest() {
//        Arrange
        String email = "email@com";
//        Act + Assert
        assertThrows(IllegalArgumentException.class, () -> employee.setEmail(email));
    }

    @Test
    void testCreateEmployeeWithInvalidEmail() {
//         Arrange
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "ring bearer";
        int jobYears = 1;
        String email = "emailemail.com";
//         Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void testCreateEmployeeWithInvalidEmailSecondTest() {
//         Arrange
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "ring bearer";
        int jobYears = 1;
        String email = "email@com";
//         Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }
}