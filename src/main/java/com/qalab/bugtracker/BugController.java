package com.qalab.bugtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BugReport createBug(@RequestBody BugReport bugReport) {
        return bugReportRepository.save(bugReport);
    }
}
