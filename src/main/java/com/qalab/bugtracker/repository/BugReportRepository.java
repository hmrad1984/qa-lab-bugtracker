package com.qalab.bugtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.qalab.bugtracker.model.BugReport;

public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    List<BugReport> findByStatus(String status);

    List<BugReport> findBySeverity(String severity);

    List<BugReport> findByStatusAndSeverity(String status, String severity);
}
