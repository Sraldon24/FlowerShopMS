package com.champsoft.services.employee.DataLayer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(columnNames = "supplierIdentifier"))
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Keep this as primary key

    @Column(unique = true, nullable = false)
    private String employeeIdentifier; // Ensure uniqueness

    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;

    @PrePersist
    public void prePersist() {
        if (this.employeeIdentifier == null || this.employeeIdentifier.isEmpty()) {
            this.employeeIdentifier = "EMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
    }
}

