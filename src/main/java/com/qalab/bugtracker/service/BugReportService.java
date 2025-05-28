package com.qalab.bugtracker.service;

import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.model.BugReport.Severity;

import java.util.List;
import java.util.Optional;

public interface BugReportService {
    BugReport saveBugReport(BugReport bugReport);

    List<BugReport> getAllBugReports();

    Optional<BugReport> getBugReportById(Long id);

    List<BugReport> getFilteredBugReports(String status, Severity severity);

}
