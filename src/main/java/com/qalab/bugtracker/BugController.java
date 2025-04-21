package com.qalab.bugtracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bugs")
public class BugController {

    @GetMapping
    public List<Map<String, String>> getAllBugs() {
        return List.of(
            Map.of("id", "1", "title", "Login issue", "status", "open"),
            Map.of("id", "2", "title", "Crash on submit", "status", "in progress")
        );
    }
}