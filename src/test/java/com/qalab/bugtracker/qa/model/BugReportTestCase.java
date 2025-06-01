package com.qalab.bugtracker.qa.model;

import lombok.Data;

@Data
public class BugReportTestCase {
    private String title;
    private String description;
    private String status;
    private String severity;
    private int expectedStatus;
    private String expectedFragment;

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getSeverity() { return severity; }
    public int getExpectedStatus() { return expectedStatus; }
    public String getExpectedFragment() { return expectedFragment; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setExpectedStatus(int expectedStatus) { this.expectedStatus = expectedStatus; }
    public void setExpectedFragment(String expectedFragment) { this.expectedFragment = expectedFragment; }
}
