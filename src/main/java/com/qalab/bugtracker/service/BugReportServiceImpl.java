package com.qalab.bugtracker.service;

import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.model.BugReport.Severity;
import com.qalab.bugtracker.repository.BugReportRepository;
import com.qalab.bugtracker.service.BugReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<BugReport> getBugReportById(Long id) {
        return bugReportRepository.findById(id);
    }

    @Override
    public List<BugReport> getFilteredBugReports(String status, Severity severity) {
        if (status != null && severity != null) {
            return bugReportRepository.findByStatusAndSeverity(status, severity);
        } else if (status != null) {
            return bugReportRepository.findByStatus(status);
        } else if (severity != null) {
            return bugReportRepository.findBySeverity(severity);
        } else {
            return bugReportRepository.findAll();
        }
    }
}
