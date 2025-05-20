package com.qalab.bugtracker.controller;

import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.service.BugReportService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugReportController {

    private final BugReportService bugReportService;

    public BugReportController(BugReportService bugReportService) {
        this.bugReportService = bugReportService;
    }

    @PostMapping
    public ResponseEntity<BugReport> createBugReport(@Valid @RequestBody BugReport bugReport) {
        BugReport savedReport = bugReportService.saveBugReport(bugReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }

    @GetMapping
    public ResponseEntity<List<BugReport>> getAllBugReports() {
        List<BugReport> reports = bugReportService.getAllBugReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BugReport> getBugReportById(@PathVariable Long id) {
        return bugReportService.getBugReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
