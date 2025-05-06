package com.qalab.bugtracker.service;

import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.repository.BugReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugReportServiceImpl implements BugReportService {

    private final BugReportRepository bugReportRepository;

    public BugReportServiceImpl(BugReportRepository bugReportRepository) {
        this.bugReportRepository = bugReportRepository;
    }

    @Override
    public BugReport saveBugReport(BugReport bugReport) {
        return bugReportRepository.save(bugReport);
    }

    @Override
    public List<BugReport> getAllBugReports() {
        return bugReportRepository.findAll();
    }
}
