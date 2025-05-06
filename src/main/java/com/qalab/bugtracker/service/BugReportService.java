package com.qalab.bugtracker.service;

import com.qalab.bugtracker.model.BugReport;
import java.util.List;

public interface BugReportService {
    BugReport saveBugReport(BugReport bugReport);
    List<BugReport> getAllBugReports();
}
