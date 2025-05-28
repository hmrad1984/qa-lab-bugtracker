package com.qalab.bugtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.model.BugReport.Severity;

public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    List<BugReport> findByStatus(String status);

    List<BugReport> findBySeverity(Severity severity);

    List<BugReport> findByStatusAndSeverity(String status, Severity severity);
}
