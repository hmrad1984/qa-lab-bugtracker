package com.qalab.bugtracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    // Custom query methods can be added here later, like:
    // List<BugReport> findByStatus(String status);
}
