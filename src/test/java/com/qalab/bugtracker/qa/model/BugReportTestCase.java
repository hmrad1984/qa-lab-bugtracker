package com.qalab.bugtracker.qa.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.qalab.bugtracker.model.BugReport.Severity;

public class BugReportTestCase {

    private String testName;
    private String title;
    private String description;
    private String status;
    private String severity;
    private int expectedStatus;
    private String expectedResponseFragment;

    // Getters and setters

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getExpectedStatus() {
        return expectedStatus;
    }

    public void setExpectedStatus(int expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    public String getExpectedResponseFragment() {
        return expectedResponseFragment;
    }

    public void setExpectedResponseFragment(String expectedResponseFragment) {
        this.expectedResponseFragment = expectedResponseFragment;
    }

    @Override
    public String toString() {
        return testName != null ? testName : String.format("BugReport(title=%s, severity=%s)", title, severity);
    }
}
