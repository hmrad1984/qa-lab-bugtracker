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
}
