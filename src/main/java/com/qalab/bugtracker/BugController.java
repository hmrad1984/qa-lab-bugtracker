package com.qalab.bugtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/bugs")
public class BugController {

    @Autowired
    private BugReportRepository bugReportRepository;

    @GetMapping
    public List<BugReport> getAllBugs() {
        return bugReportRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createBug(@Valid @RequestBody BugReport bugReport, BindingResult result) {
        if (result.hasErrors()) {
            // Handle validation errors
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        BugReport savedBugReport = bugReportRepository.save(bugReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBugReport);
    }
}