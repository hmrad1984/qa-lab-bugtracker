package com.qalab.bugtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "bug_report") // Optional, but recommended for clarity
@JsonIgnoreProperties(ignoreUnknown = true)
public class BugReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotEmpty(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotEmpty(message = "Status is required")
    private String status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Severity is required")
    private Severity severity;

    public BugReport() {
    }

    public BugReport(String title, String description, String status, Severity severity) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.severity = severity;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public enum Severity {
        LOW, MEDIUM, HIGH
    }

}