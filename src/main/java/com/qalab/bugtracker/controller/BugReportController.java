package com.qalab.bugtracker.controller;

import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.model.BugReport.Severity;
import com.qalab.bugtracker.service.BugReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugReportController {

    private final BugReportService bugReportService;
    private final com.qalab.bugtracker.repository.BugReportRepository bugReportRepository;

    public BugReportController(BugReportService bugReportService,
            com.qalab.bugtracker.repository.BugReportRepository bugReportRepository) {
        this.bugReportService = bugReportService;
        this.bugReportRepository = bugReportRepository;
    }

    @PostMapping
    public ResponseEntity<BugReport> createBugReport(@Valid @RequestBody BugReport bugReport) {
        BugReport savedReport = bugReportService.saveBugReport(bugReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }

    @GetMapping
    public ResponseEntity<List<BugReport>> getAllBugReports(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity) {
        List<BugReport> reports;
        
        if (status != null || severity != null) {
            // Handle filtering
            Severity severityEnum = null;
            if (severity != null && !severity.isBlank()) {
                try {
                    severityEnum = Severity.valueOf(severity.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid severity value: " + severity);
                }
            }
            reports = bugReportService.getFilteredBugReports(
                (status != null && !status.isBlank()) ? status.toUpperCase() : null, 
                severityEnum
            );
        } else {
            reports = bugReportService.getAllBugReports();
        }
        
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BugReport> getBugReportById(@PathVariable Long id) {
        return bugReportService.getBugReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public List<BugReport> getBugReports(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity) {
        try {
            // Treat blank as null
            if (status != null && status.isBlank())
                status = null;
            if (severity != null && severity.isBlank())
                severity = null;

            if (status != null && severity != null) {
                return bugReportRepository.findByStatusAndSeverity(status, Severity.valueOf(severity.toUpperCase()));
            } else if (status != null) {
                return bugReportRepository.findByStatus(status);
            } else if (severity != null) {
                return bugReportRepository.findBySeverity(Severity.valueOf(severity.toUpperCase()));
            }
            return bugReportRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter input", e);
        }
    }

}
