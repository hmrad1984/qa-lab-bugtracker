package com.qalab.bugtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qalab.bugtracker.model.BugReport;

public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    // Custom query methods can be added here later, like:
    // List<BugReport> findByStatus(String status);
}
