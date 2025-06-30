package com.qalab.bugtracker.qa.model;

import com.qalab.bugtracker.model.BugReport.Severity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

public class BugReportDTO {
    private String title;
    private String description;
    private String status;
    private Severity severity;

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

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

}
